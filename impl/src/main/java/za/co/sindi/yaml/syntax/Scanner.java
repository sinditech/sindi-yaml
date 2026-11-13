package za.co.sindi.yaml.syntax;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import za.co.sindi.yaml.stream.YamlParsingException;
import za.co.sindi.yaml.syntax.Token.Kind;

/**
 * Converts characters into {@link Token}s (stage 1 of the pipeline Scanner → EventParser →
 * AliasExpander → typed parser/reader). The algorithm is the well-known indentation-stack /
 * "possible simple key" scanner: a plain or quoted scalar is only recognised as a mapping key
 * once the following {@code ':'} is seen, at which point KEY (and BLOCK-MAPPING-START) tokens
 * are inserted retroactively.
 *
 * <p>YAML 1.2 line breaks are LF, CR and CRLF. Directives ({@code %YAML}, {@code %TAG}) are skipped.
 */
public final class Scanner {

    private record SimpleKey(int tokenNumber, boolean required, int index, int line, int column, Mark mark) { }

    private final char[] buf;
    private final int len;
    private int ptr;
    private int line;
    private int column;

    private boolean done;
    private int flowLevel;
    private final ArrayList<Token> tokens = new ArrayList<>();
    private int tokensTaken;
    private int indent = -1;
    private final ArrayDeque<Integer> indents = new ArrayDeque<>();
    private boolean allowSimpleKey = true;
    private Token last;
    private final Map<Integer, SimpleKey> possibleSimpleKeys = new HashMap<>();

    public Scanner(char[] input) {
        this.buf = input;
        this.len = input.length;
        checkPrintable();
        Mark m = mark();
        emit(Token.of(Kind.STREAM_START, m, m));
    }

    // ------------------------------------------------------------------ public token API

    public boolean check(Kind kind) {
        ensureTokens();
        return !tokens.isEmpty() && tokens.get(0).kind() == kind;
    }

    public boolean check(Kind a, Kind b) { return check(a) || check(b); }

    public boolean check(Kind a, Kind b, Kind c) { return check(a) || check(b) || check(c); }

    public boolean check(Kind a, Kind b, Kind c, Kind d) { return check(a) || check(b) || check(c) || check(d); }

    public Token peekToken() {
        ensureTokens();
        if (tokens.isEmpty()) throw new IllegalStateException("Scanner is exhausted");
        return tokens.get(0);
    }

    public Token nextToken() {
        ensureTokens();
        if (tokens.isEmpty()) throw new IllegalStateException("Scanner is exhausted");
        tokensTaken++;
        return tokens.remove(0);
    }

    private void emit(Token t) {
        tokens.add(t);
        last = t;
    }

    private void ensureTokens() {
        while (needMoreTokens()) fetchMoreTokens();
    }

    // ------------------------------------------------------------------ character handling

    private static boolean isBreak(char c) { return c == '\n' || c == '\r'; }

    private static boolean isBreakZ(char c) { return c == '\0' || isBreak(c); }

    private static boolean isBlankOrBreakZ(char c) { return c == ' ' || c == '\t' || isBreakZ(c); }

    private static boolean isFlowIndicator(char c) { return c == ',' || c == '[' || c == ']' || c == '{' || c == '}'; }

    private char peek() { return ptr < len ? buf[ptr] : '\0'; }

    private char peek(int n) { int i = ptr + n; return i < len ? buf[i] : '\0'; }

    private Mark mark() { return new Mark(ptr, line, column); }

    private void forward(int n) {
        for (; n > 0; n--) {
            char ch = buf[ptr++];
            if (ch == '\n' || (ch == '\r' && peek() != '\n')) {
                line++;
                column = 0;
            } else if (ch != '\uFEFF') {
                column++;
            }
        }
    }

    /** Consumes one line break (LF, CR or CRLF) if present. */
    private boolean scanLineBreak() {
        char c = peek();
        if (c == '\r') { forward(peek(1) == '\n' ? 2 : 1); return true; }
        if (c == '\n') { forward(1); return true; }
        return false;
    }

    private YamlParsingException error(String message, Mark at) { return new YamlParsingException(message, at); }

    private void checkPrintable() {
        int l = 0, c = 0;
        for (int i = 0; i < len; i++) {
            char ch = buf[i];
            boolean ok;
            if (Character.isHighSurrogate(ch)) {
                ok = i + 1 < len && Character.isLowSurrogate(buf[i + 1]);
                if (ok) { i++; c++; }
            } else {
                ok = ch == '\t' || ch == '\n' || ch == '\r' || (ch >= 0x20 && ch <= 0x7E) || ch == 0x85
                        || (ch >= 0xA0 && ch <= 0xD7FF) || (ch >= 0xE000 && ch <= 0xFFFD);
            }
            if (!ok) {
                throw error("The stream contains a non-printable character (U+"
                        + String.format("%04X", (int) ch) + ")", new Mark(i, l, c));
            }
            if (ch == '\n' || (ch == '\r' && (i + 1 >= len || buf[i + 1] != '\n'))) { l++; c = 0; } else { c++; }
        }
    }

    // ------------------------------------------------------------------ token queue management

    private boolean needMoreTokens() {
        if (done) return false;
        if (tokens.isEmpty()) return true;
        staleSimpleKeys();
        return nextPossibleSimpleKey() == tokensTaken;
    }

    private void fetchMoreTokens() {
        scanToNextToken();
        staleSimpleKeys();
        unwindIndent(column);
        if (ptr >= len) { fetchStreamEnd(); return; }
        char ch = peek();
        if (flowLevel > 0 && column <= indent && ch != ']' && ch != '}' && onlyBlanksBefore()) {
            throw error("The content of a flow collection must be indented more than its parent block", mark());
        }
        if (ch == '%' && column == 0) { fetchDirective(); return; }
        if (ch == '-' && isDocumentMarker('-')) { fetchDocumentIndicator(Kind.DOCUMENT_START); return; }
        if (ch == '.' && isDocumentMarker('.')) { fetchDocumentIndicator(Kind.DOCUMENT_END); return; }
        if (ch == '[') { fetchFlowCollectionStart(Kind.FLOW_SEQUENCE_START); return; }
        if (ch == '{') { fetchFlowCollectionStart(Kind.FLOW_MAPPING_START); return; }
        if (ch == ']') { fetchFlowCollectionEnd(Kind.FLOW_SEQUENCE_END); return; }
        if (ch == '}') { fetchFlowCollectionEnd(Kind.FLOW_MAPPING_END); return; }
        if (ch == ',') { fetchFlowEntry(); return; }
        if (ch == '-' && isBlankOrBreakZ(peek(1))) { fetchBlockEntry(); return; }
        if (ch == '?' && isBlankOrBreakZ(peek(1))) { fetchKey(); return; }
        if (ch == ':' && isValueIndicator()) { fetchValue(); return; }
        if (ch == '*') { fetchAnchor(Kind.ALIAS); return; }
        if (ch == '&') { fetchAnchor(Kind.ANCHOR); return; }
        if (ch == '!') { fetchTag(); return; }
        if (ch == '|' && flowLevel == 0) { fetchBlockScalar('|'); return; }
        if (ch == '>' && flowLevel == 0) { fetchBlockScalar('>'); return; }
        if (ch == '\'') { fetchFlowScalar('\''); return; }
        if (ch == '"') { fetchFlowScalar('"'); return; }
        if (checkPlain()) { fetchPlain(); return; }
        throw error("Found character '" + ch + "' that cannot start any token", mark());
    }

    /** ':' is a value indicator before a blank, in flow context before a flow indicator or right after a JSON-like key. */
    private boolean isValueIndicator() {
        char n = peek(1);
        if (isBlankOrBreakZ(n)) return true;
        if (flowLevel == 0) return false;
        if (isFlowIndicator(n)) return true;
        return last != null && ((last.kind() == Kind.SCALAR && !last.plain())
                || last.kind() == Kind.FLOW_SEQUENCE_END || last.kind() == Kind.FLOW_MAPPING_END);
    }

    private boolean onlyBlanksBefore() {
        int i = ptr - 1;
        while (i >= 0 && (buf[i] == ' ' || buf[i] == '\t')) i--;
        return i < 0 || buf[i] == '\n' || buf[i] == '\r';
    }

    private boolean isDocumentMarker(char c) {
        return column == 0 && peek(1) == c && peek(2) == c && isBlankOrBreakZ(peek(3));
    }

    // ------------------------------------------------------------------ simple keys

    private int nextPossibleSimpleKey() {
        int min = -1;
        for (SimpleKey k : possibleSimpleKeys.values()) {
            if (min == -1 || k.tokenNumber() < min) min = k.tokenNumber();
        }
        return min;
    }

    private void staleSimpleKeys() {
        var it = possibleSimpleKeys.entrySet().iterator();
        while (it.hasNext()) {
            var entry = it.next();
            SimpleKey key = entry.getValue();
            boolean otherLine = entry.getKey() == 0 && key.line() != line;   // flow keys may span lines
            if (otherLine || ptr - key.index() > 1024) {
                if (key.required()) throw error("While scanning a simple key: could not find expected ':'", key.mark());
                it.remove();
            }
        }
    }

    private void savePossibleSimpleKey() {
        boolean required = flowLevel == 0 && indent == column;
        if (allowSimpleKey) {
            removePossibleSimpleKey();
            possibleSimpleKeys.put(flowLevel,
                    new SimpleKey(tokensTaken + tokens.size(), required, ptr, line, column, mark()));
        }
    }

    private void removePossibleSimpleKey() {
        SimpleKey key = possibleSimpleKeys.remove(flowLevel);
        if (key != null && key.required()) {
            throw error("While scanning a simple key: could not find expected ':'", key.mark());
        }
    }

    // ------------------------------------------------------------------ indentation

    private void unwindIndent(int col) {
        if (flowLevel > 0) return;
        while (indent > col) {
            Mark m = mark();
            indent = indents.pop();
            emit(Token.of(Kind.BLOCK_END, m, m));
        }
    }

    private boolean addIndent(int col) {
        if (indent < col) {
            indents.push(indent);
            indent = col;
            return true;
        }
        return false;
    }

    // ------------------------------------------------------------------ fetchers

    private void fetchStreamEnd() {
        unwindIndent(-1);
        removePossibleSimpleKey();
        allowSimpleKey = false;
        possibleSimpleKeys.clear();
        Mark m = mark();
        emit(Token.of(Kind.STREAM_END, m, m));
        done = true;
    }

    private void fetchDirective() {
        unwindIndent(-1);
        removePossibleSimpleKey();
        allowSimpleKey = false;
        Mark start = mark();
        forward(1);
        StringBuilder sb = new StringBuilder();
        while (!isBreakZ(peek())) {
            if (peek() == '#' && sb.length() > 0 && (sb.charAt(sb.length() - 1) == ' ' || sb.charAt(sb.length() - 1) == '\t')) {
                while (!isBreakZ(peek())) forward(1);
                break;
            }
            sb.append(peek());
            forward(1);
        }
        emit(Token.named(Kind.DIRECTIVE, sb.toString().strip(), start, mark()));
    }

    private void fetchDocumentIndicator(Kind kind) {
        unwindIndent(-1);
        removePossibleSimpleKey();
        allowSimpleKey = false;
        Mark s = mark();
        forward(3);
        Mark e = mark();
        if (kind == Kind.DOCUMENT_END) {
            int k = 0;
            while (peek(k) == ' ' || peek(k) == '\t') k++;
            char n = peek(k);
            if (!isBreakZ(n) && n != '#') throw error("Unexpected content after the document end marker", mark());
        }
        emit(Token.of(kind, s, e));
    }

    private void fetchFlowCollectionStart(Kind kind) {
        savePossibleSimpleKey();
        flowLevel++;
        allowSimpleKey = true;
        Mark s = mark();
        forward(1);
        emit(Token.of(kind, s, mark()));
    }

    private void fetchFlowCollectionEnd(Kind kind) {
        if (flowLevel == 0) throw error("Found unexpected '" + peek() + "'", mark());
        removePossibleSimpleKey();
        flowLevel--;
        allowSimpleKey = false;
        Mark s = mark();
        forward(1);
        emit(Token.of(kind, s, mark()));
    }

    private void fetchFlowEntry() {
        allowSimpleKey = true;
        removePossibleSimpleKey();
        Mark s = mark();
        forward(1);
        emit(Token.of(Kind.FLOW_ENTRY, s, mark()));
    }

    private void fetchBlockEntry() {
        if (flowLevel == 0) {
            if (!allowSimpleKey) throw error("Sequence entries are not allowed here", mark());
            if (addIndent(column)) {
                Mark m = mark();
                emit(Token.of(Kind.BLOCK_SEQUENCE_START, m, m));
            }
        }
        allowSimpleKey = true;
        removePossibleSimpleKey();
        Mark s = mark();
        forward(1);
        emit(Token.of(Kind.BLOCK_ENTRY, s, mark()));
    }

    private void fetchKey() {
        if (flowLevel == 0) {
            if (!allowSimpleKey) throw error("Mapping keys are not allowed here", mark());
            if (addIndent(column)) {
                Mark m = mark();
                emit(Token.of(Kind.BLOCK_MAPPING_START, m, m));
            }
        }
        allowSimpleKey = flowLevel == 0;
        removePossibleSimpleKey();
        Mark s = mark();
        forward(1);
        emit(Token.of(Kind.KEY, s, mark()));
    }

    private void fetchValue() {
        SimpleKey key = possibleSimpleKeys.remove(flowLevel);
        if (key != null) {
            int at = key.tokenNumber() - tokensTaken;
            tokens.add(at, Token.of(Kind.KEY, key.mark(), key.mark()));
            if (flowLevel == 0 && addIndent(key.column())) {
                tokens.add(at, Token.of(Kind.BLOCK_MAPPING_START, key.mark(), key.mark()));
            }
            allowSimpleKey = false;
        } else {
            if (flowLevel == 0) {
                if (!allowSimpleKey) throw error("Mapping values are not allowed here", mark());
                if (addIndent(column)) {
                    Mark m = mark();
                    emit(Token.of(Kind.BLOCK_MAPPING_START, m, m));
                }
            }
            allowSimpleKey = flowLevel == 0;
            removePossibleSimpleKey();
        }
        Mark s = mark();
        forward(1);
        emit(Token.of(Kind.VALUE, s, mark()));
    }

    private void fetchAnchor(Kind kind) {
        savePossibleSimpleKey();
        allowSimpleKey = false;
        emit(scanAnchor(kind));
    }

    private void fetchTag() {
        savePossibleSimpleKey();
        allowSimpleKey = false;
        emit(scanTag());
    }

    private void fetchBlockScalar(char style) {
        allowSimpleKey = true;
        removePossibleSimpleKey();
        emit(scanBlockScalar(style));
    }

    private void fetchFlowScalar(char style) {
        savePossibleSimpleKey();
        allowSimpleKey = false;
        emit(scanFlowScalar(style));
    }

    private void fetchPlain() {
        savePossibleSimpleKey();
        allowSimpleKey = false;
        emit(scanPlain());
    }

    private boolean checkPlain() {
        char ch = peek();
        if ("\0 \t\r\n-?:,[]{}#&*!|>'\"%@`".indexOf(ch) < 0) return true;
        if (ch == '-' || ch == '?' || ch == ':') {
            char n = peek(1);
            return !isBlankOrBreakZ(n) && !(flowLevel > 0 && isFlowIndicator(n));
        }
        return false;
    }

    // ------------------------------------------------------------------ scanners

    private boolean onlySpacesBefore() {
        int i = ptr - 1;
        while (i >= 0 && buf[i] == ' ') i--;
        return i < 0 || buf[i] == '\n' || buf[i] == '\r';
    }

    private void scanToNextToken() {
        if (ptr == 0 && peek() == '\uFEFF') forward(1);
        boolean found = false;
        while (!found) {
            boolean sawTab = false;
            for (;;) {
                char c = peek();
                if (c == ' ') {
                    forward(1);
                } else if (c == '\t') {
                    if (flowLevel == 0 && onlySpacesBefore()) checkTabIndentation();
                    sawTab = true;
                    forward(1);
                } else {
                    break;
                }
            }
            if (sawTab && flowLevel == 0 && isBlockIndicatorAhead()) {
                throw error("Tab characters cannot separate block indicators", mark());
            }
            if (peek() == '#') {
                boolean startsComment = ptr == 0 || buf[ptr - 1] == ' ' || buf[ptr - 1] == '\t' || buf[ptr - 1] == '\n' || buf[ptr - 1] == '\r';
                if (!startsComment) throw error("A comment must be separated from other tokens by white space", mark());
                while (!isBreakZ(peek())) forward(1);
            }
            if (scanLineBreak()) {
                if (flowLevel == 0) allowSimpleKey = true;
            } else {
                found = true;
            }
        }
    }

    private boolean isBlockIndicatorAhead() {
        char c = peek();
        return (c == '-' || c == '?' || c == ':') && isBlankOrBreakZ(peek(1));
    }

    /** A tab where indentation is expected is only tolerated as separation inside a value (or before a root flow collection). */
    private void checkTabIndentation() {
        int k = 0;
        while (peek(k) == ' ' || peek(k) == '\t') k++;
        char n = peek(k);
        if (isBreakZ(n) || n == '#') return;
        boolean valuePosition = last != null && (last.kind() == Kind.VALUE || last.kind() == Kind.KEY
                || last.kind() == Kind.BLOCK_ENTRY || last.kind() == Kind.DOCUMENT_START);
        boolean rootFlow = indent == -1 && (n == '[' || n == '{');
        boolean blockIndicator = (n == '-' || n == '?' || n == ':') && isBlankOrBreakZ(peek(k + 1));
        if (blockIndicator || !(valuePosition || rootFlow)) {
            throw error("Tab characters cannot be used for indentation", mark());
        }
    }

    private Token scanAnchor(Kind kind) {
        Mark start = mark();
        forward(1);
        int length = 0;
        while (!isBlankOrBreakZ(peek(length)) && !isFlowIndicator(peek(length))) length++;
        if (length == 0) throw error("Expected a name after '" + (kind == Kind.ALIAS ? "*" : "&") + "'", mark());
        String name = new String(buf, ptr, length);
        forward(length);
        return Token.named(kind, name, start, mark());
    }

    private static boolean isTagChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')
                || "-#;/?:@&=+$_.!~*'()%".indexOf(c) >= 0;
    }

    private Token scanTag() {
        Mark start = mark();
        forward(1);
        StringBuilder sb = new StringBuilder();
        char style = '\0';
        if (peek() == '<') {
            style = '<';
            forward(1);
            while (peek() != '>') {
                if (isBlankOrBreakZ(peek())) throw error("Expected '>' to close a verbatim tag", mark());
                sb.append(peek());
                forward(1);
            }
            forward(1);
            if (sb.isEmpty()) throw error("A verbatim tag must not be empty", mark());
        } else {
            sb.append('!');
            while (isTagChar(peek())) {
                sb.append(peek());
                forward(1);
            }
        }
        char n = peek();
        if (!isBlankOrBreakZ(n) && !(flowLevel > 0 && isFlowIndicator(n))) {
            throw error("Invalid character '" + n + "' in a tag", mark());
        }
        return new Token(Kind.TAG, sb.toString(), false, style, start, mark());
    }

    private Token scanBlockScalar(char style) {
        boolean folded = style == '>';
        StringBuilder out = new StringBuilder();
        Mark start = mark();
        forward(1);
        int chomping = 0;      // -1 strip, 0 clip, +1 keep
        int increment = 0;
        char ch = peek();
        if (ch == '+' || ch == '-') {
            chomping = ch == '+' ? 1 : -1;
            forward(1);
            if (isDigit(peek())) { increment = indentIndicator(peek()); forward(1); }
        } else if (isDigit(ch)) {
            increment = indentIndicator(ch);
            forward(1);
            ch = peek();
            if (ch == '+' || ch == '-') { chomping = ch == '+' ? 1 : -1; forward(1); }
        }
        if (!isBlankOrBreakZ(peek())) throw error("Expected chomping or indentation indicators of a block scalar", mark());
        while (peek() == ' ' || peek() == '\t') forward(1);
        if (peek() == '#') while (!isBreakZ(peek())) forward(1);
        if (!isBreakZ(peek())) throw error("Expected a comment or a line break after the block scalar header", mark());
        scanLineBreak();

        int minIndent = indent + 1;
        int blockIndent;
        int breaks = 0;
        if (increment == 0) {
            int maxIndent = 0;
            while (peek() == ' ' || isBreak(peek())) {
                if (peek() != ' ') { scanLineBreak(); breaks++; }
                else { forward(1); if (column > maxIndent) maxIndent = column; }
            }
            blockIndent = Math.max(minIndent, maxIndent);
        } else {
            blockIndent = minIndent + increment - 1;
            breaks = scanBlockScalarBreaks(blockIndent);
        }

        String lineBreak = "";
        while (column == blockIndent && ptr < len && !(column == 0 && (isDocumentMarker('-') || isDocumentMarker('.')))) {
            out.append("\n".repeat(breaks));
            boolean leadingNonSpace = peek() != ' ' && peek() != '\t';
            int length = 0;
            while (!isBreakZ(peek(length))) length++;
            out.append(buf, ptr, length);
            forward(length);
            lineBreak = scanLineBreak() ? "\n" : "";
            breaks = scanBlockScalarBreaks(blockIndent);
            if (column == blockIndent && ptr < len && !(column == 0 && (isDocumentMarker('-') || isDocumentMarker('.')))) {
                if (folded && lineBreak.equals("\n") && leadingNonSpace && peek() != ' ' && peek() != '\t') {
                    if (breaks == 0) out.append(' ');
                } else {
                    out.append(lineBreak);
                }
            } else {
                break;
            }
        }
        if (chomping != -1) out.append(lineBreak);
        if (chomping == 1) out.append("\n".repeat(breaks));
        return new Token(Kind.SCALAR, out.toString(), false, style, start, mark());
    }

    private static boolean isDigit(char c) { return c >= '0' && c <= '9'; }

    private int indentIndicator(char c) {
        if (c == '0') throw error("The indentation indicator of a block scalar must be 1-9", mark());
        return c - '0';
    }

    private int scanBlockScalarBreaks(int blockIndent) {
        int count = 0;
        while (column < blockIndent && peek() == ' ') forward(1);
        while (isBreak(peek())) {
            scanLineBreak();
            count++;
            while (column < blockIndent && peek() == ' ') forward(1);
        }
        return count;
    }

    private Token scanFlowScalar(char style) {
        boolean dbl = style == '"';
        StringBuilder sb = new StringBuilder();
        Mark start = mark();
        char quote = peek();
        forward(1);
        scanFlowScalarNonSpaces(dbl, start, sb);
        while (peek() != quote) {
            scanFlowScalarSpaces(start, sb);
            scanFlowScalarNonSpaces(dbl, start, sb);
        }
        forward(1);
        return new Token(Kind.SCALAR, sb.toString(), false, style, start, mark());
    }

    private static int escapeReplacement(char c) {
        return switch (c) {
            case '0' -> 0; case 'a' -> 7; case 'b' -> 8; case 't', '\t' -> 9; case 'n' -> 10; case 'v' -> 11;
            case 'f' -> 12; case 'r' -> 13; case 'e' -> 27; case ' ' -> 32; case '"' -> '"'; case '/' -> '/';
            case '\\' -> '\\'; case 'N' -> 0x85; case '_' -> 0xA0; case 'L' -> 0x2028; case 'P' -> 0x2029;
            default -> -1;
        };
    }

    private void scanFlowScalarNonSpaces(boolean dbl, Mark start, StringBuilder sb) {
        for (;;) {
            int length = 0;
            for (;; length++) {
                char c = peek(length);
                if (c == '\'' || c == '"' || c == '\\' || c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\0') break;
            }
            if (length > 0) {
                sb.append(buf, ptr, length);
                forward(length);
            }
            char ch = peek();
            if (!dbl && ch == '\'' && peek(1) == '\'') {
                sb.append('\'');
                forward(2);
            } else if ((dbl && ch == '\'') || (!dbl && (ch == '"' || ch == '\\'))) {
                sb.append(ch);
                forward(1);
            } else if (dbl && ch == '\\') {
                forward(1);
                ch = peek();
                int rep = escapeReplacement(ch);
                if (rep >= 0) {
                    sb.append((char) rep);
                    forward(1);
                } else if (ch == 'x' || ch == 'u' || ch == 'U') {
                    int n = ch == 'x' ? 2 : ch == 'u' ? 4 : 8;
                    forward(1);
                    for (int k = 0; k < n; k++) {
                        if (Character.digit(peek(k), 16) < 0) {
                            throw error("Expected " + n + " hexadecimal digits in an escape sequence", mark());
                        }
                    }
                    long code = Long.parseLong(new String(buf, ptr, n), 16);
                    if (code > 0x10FFFF || (code >= 0xD800 && code <= 0xDFFF)) {
                        throw error("Invalid Unicode code point in an escape sequence", mark());
                    }
                    sb.appendCodePoint((int) code);
                    forward(n);
                } else if (isBreak(ch)) {
                    scanLineBreak();
                    scanFlowScalarBreaks(sb, false);
                } else {
                    throw error("Found unknown escape character '" + ch + "' in a double-quoted scalar", mark());
                }
            } else {
                return;
            }
        }
    }

    private void scanFlowScalarSpaces(Mark start, StringBuilder sb) {
        int length = 0;
        while (peek(length) == ' ' || peek(length) == '\t') length++;
        String whitespace = new String(buf, ptr, length);
        forward(length);
        if (ptr >= len) throw error("Found unexpected end of stream in a quoted scalar starting here", start);
        if (isBreak(peek())) {
            scanLineBreak();
            scanFlowScalarBreaks(sb, true);
        } else {
            sb.append(whitespace);
        }
    }

    /** Folds the line break(s) that were just consumed: one break becomes a space, n&gt;1 become n-1 newlines. */
    private void scanFlowScalarBreaks(StringBuilder sb, boolean fold) {
        int breaks = 0;
        for (;;) {
            if (isDocumentMarker('-') || isDocumentMarker('.')) {
                throw error("Found unexpected document separator inside a quoted scalar", mark());
            }
            while (peek() == ' ' || peek() == '\t') forward(1);
            if (isBreak(peek())) {
                scanLineBreak();
                breaks++;
            } else {
                if (flowLevel == 0 && ptr < len && column <= indent) {
                    throw error("A multi-line quoted scalar must be indented more than its parent block", mark());
                }
                break;
            }
        }
        if (fold) {
            if (breaks == 0) sb.append(' ');
            else sb.append("\n".repeat(breaks));
        } else {
            sb.append("\n".repeat(breaks));
        }
    }

    private Token scanPlain() {
        StringBuilder sb = new StringBuilder();
        Mark start = mark();
        Mark end = start;
        int minIndent = indent + 1;
        String spaces = "";
        for (;;) {
            if (peek() == '#') break;
            int length = 0;
            for (;; length++) {
                char c = peek(length);
                if (isBlankOrBreakZ(c)) break;
                if (c == ':') {
                    char n = peek(length + 1);
                    if (isBlankOrBreakZ(n) || (flowLevel > 0 && isFlowIndicator(n))) break;
                }
                if (flowLevel > 0 && isFlowIndicator(c)) break;
            }
            if (length == 0) break;
            allowSimpleKey = false;
            sb.append(spaces);
            sb.append(buf, ptr, length);
            forward(length);
            end = mark();
            spaces = scanPlainSpaces();
            if (spaces == null || spaces.isEmpty() || peek() == '#' || (flowLevel == 0 && column < minIndent)) break;
        }
        return new Token(Kind.SCALAR, sb.toString(), true, '\0', start, end);
    }

    /** @return the folded whitespace, or {@code null} if a document marker ends the scalar */
    private String scanPlainSpaces() {
        int length = 0;
        while (peek(length) == ' ' || peek(length) == '\t') length++;
        String whitespace = new String(buf, ptr, length);
        forward(length);
        if (isBreak(peek())) {
            scanLineBreak();
            allowSimpleKey = true;
            if (isDocumentMarker('-') || isDocumentMarker('.')) return null;
            int breaks = 0;
            while (peek() == ' ' || peek() == '\t' || isBreak(peek())) {
                if (isBreak(peek())) {
                    scanLineBreak();
                    breaks++;
                    if (isDocumentMarker('-') || isDocumentMarker('.')) return null;
                } else {
                    forward(1);
                }
            }
            return breaks == 0 ? " " : "\n".repeat(breaks);
        }
        return whitespace;
    }
}
