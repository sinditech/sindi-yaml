package za.co.sindi.yaml.syntax;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import za.co.sindi.yaml.stream.YamlParsingException;
import za.co.sindi.yaml.syntax.RawEvent.Kind;

/**
 * Turns the token stream into {@link RawEvent}s (stage 2). Implemented as an explicit state machine
 * (State pattern): every grammar production of the YAML 1.2 syntax is one {@link State} that returns
 * the next event and installs its successor, so the parser needs neither recursion nor look-ahead
 * beyond a single token.
 *
 * <pre>
 * stream            ::= STREAM-START implicit_document? explicit_document* STREAM-END
 * block_mapping     ::= BLOCK-MAPPING-START ((KEY node?)? (VALUE node?)?)* BLOCK-END
 * block_sequence    ::= BLOCK-SEQUENCE-START (BLOCK-ENTRY node?)* BLOCK-END
 * flow_sequence     ::= FLOW-SEQUENCE-START (entry FLOW-ENTRY)* entry? FLOW-SEQUENCE-END
 * </pre>
 */
public final class EventParser {

    @FunctionalInterface
    private interface State { RawEvent run(); }

    private final Scanner scanner;
    private State state = this::parseStreamStart;
    private final Deque<State> states = new ArrayDeque<>();
    private final Deque<Mark> marks = new ArrayDeque<>();
    private static final Pattern YAML_VERSION = Pattern.compile("(\\d+)\\.(\\d+)");
    private static final Pattern TAG_HANDLE = Pattern.compile("!([-0-9A-Za-z]*!)?");
    private Map<String, String> tagHandles = defaultHandles();
    private boolean lastEndExplicit;
    private int documents;

    private static Map<String, String> defaultHandles() {
        Map<String, String> m = new HashMap<>();
        m.put("!", "!");
        m.put("!!", "tag:yaml.org,2002:");
        return m;
    }

    public EventParser(Scanner scanner) { this.scanner = scanner; }

    /** @return the next event, or {@code null} after STREAM_END */
    public RawEvent next() { return state == null ? null : state.run(); }

    // ------------------------------------------------------------------ helpers

    private boolean check(Token.Kind k) { return scanner.check(k); }

    private YamlParsingException error(String message, Mark at) { return new YamlParsingException(message, at); }

    private static RawEvent empty(Mark m) {
        return new RawEvent(Kind.SCALAR, null, null, "", true, '\0', m, m);
    }

    private String resolveTag(Token t) {
        String v = t.value();
        if (t.style() == '<' || v.equals("!")) return v;
        int second = v.indexOf('!', 1);
        String handle = second > 0 ? v.substring(0, second + 1) : "!";
        String suffix = second > 0 ? v.substring(second + 1) : v.substring(1);
        String prefix = tagHandles.get(handle);
        if (prefix == null) throw error("Found undefined tag handle '" + handle + "'", t.start());
        return prefix + percentDecode(suffix);
    }

    private static String percentDecode(String s) {
        if (s.indexOf('%') < 0) return s;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '%' && i + 2 < s.length() + 0 && Character.digit(s.charAt(i + 1), 16) >= 0 && Character.digit(s.charAt(i + 2), 16) >= 0) {
                out.write(Integer.parseInt(s.substring(i + 1, i + 3), 16));
                i += 2;
            } else {
                byte[] b = String.valueOf(c).getBytes(StandardCharsets.UTF_8);
                out.write(b, 0, b.length);
            }
        }
        return out.toString(StandardCharsets.UTF_8);
    }

    // ------------------------------------------------------------------ stream and documents

    private RawEvent parseStreamStart() {
        Token t = scanner.nextToken();
        state = this::parseImplicitDocumentStart;
        return RawEvent.simple(Kind.STREAM_START, t.start(), t.end());
    }

    private RawEvent parseImplicitDocumentStart() {
        if (!scanner.check(Token.Kind.DIRECTIVE, Token.Kind.DOCUMENT_START, Token.Kind.DOCUMENT_END, Token.Kind.STREAM_END)) {
            return startBareDocument();
        }
        return parseDocumentStart();
    }

    private RawEvent startBareDocument() {
        Mark m = scanner.peekToken().start();
        tagHandles = defaultHandles();
        documents++;
        states.push(this::parseDocumentEnd);
        state = this::parseBlockNode;
        return RawEvent.simple(Kind.DOCUMENT_START, m, m);
    }

    private RawEvent parseDocumentStart() {
        while (check(Token.Kind.DOCUMENT_END)) { scanner.nextToken(); lastEndExplicit = true; }
        if (!check(Token.Kind.STREAM_END)) {
            Token first = scanner.peekToken();
            boolean directives = check(Token.Kind.DIRECTIVE);
            if (directives && documents > 0 && !lastEndExplicit) {
                throw error("A directive must be preceded by a document end marker '...'", first.start());
            }
            processDirectives();
            if (!check(Token.Kind.DOCUMENT_START)) {
                Token t = scanner.peekToken();
                if (!directives && lastEndExplicit) return startBareDocument();
                throw error("Expected a document start marker '---' but found " + t.kind(), t.start());
            }
            Token t = scanner.nextToken();
            documents++;
            states.push(this::parseDocumentEnd);
            state = this::parseDocumentContent;
            return RawEvent.simple(Kind.DOCUMENT_START, t.start(), t.end());
        }
        Token t = scanner.nextToken();
        state = null;
        return RawEvent.simple(Kind.STREAM_END, t.start(), t.end());
    }

    private void processDirectives() {
        tagHandles = defaultHandles();
        Set<String> declared = new HashSet<>();
        boolean yaml = false;
        while (check(Token.Kind.DIRECTIVE)) {
            Token t = scanner.nextToken();
            String[] parts = t.value().split("[ \\t]+");
            switch (parts[0]) {
                case "YAML" -> {
                    if (yaml) throw error("Duplicate %YAML directive", t.start());
                    yaml = true;
                    var m = parts.length == 2 ? YAML_VERSION.matcher(parts[1]) : null;
                    if (m == null || !m.matches()) throw error("Malformed %YAML directive", t.start());
                    if (!m.group(1).equals("1")) throw error("Unsupported YAML version " + parts[1], t.start());
                }
                case "TAG" -> {
                    if (parts.length != 3 || !TAG_HANDLE.matcher(parts[1]).matches()) throw error("Malformed %TAG directive", t.start());
                    if (!declared.add(parts[1])) throw error("Duplicate %TAG directive for handle " + parts[1], t.start());
                    tagHandles.put(parts[1], parts[2]);
                }
                default -> { /* reserved directives are ignored, as the specification demands */ }
            }
        }
    }

    private RawEvent parseDocumentEnd() {
        Token t = scanner.peekToken();
        Mark start = t.start(), end = t.start();
        lastEndExplicit = false;
        if (check(Token.Kind.DOCUMENT_END)) {
            t = scanner.nextToken();
            end = t.end();
            lastEndExplicit = true;
        }
        state = this::parseDocumentStart;
        return RawEvent.simple(Kind.DOCUMENT_END, start, end);
    }

    private RawEvent parseDocumentContent() {
        if (scanner.check(Token.Kind.DOCUMENT_START, Token.Kind.DOCUMENT_END, Token.Kind.STREAM_END)) {
            RawEvent e = empty(scanner.peekToken().start());
            state = states.pop();
            return e;
        }
        return parseBlockNode();
    }

    // ------------------------------------------------------------------ nodes

    private RawEvent parseBlockNode() { return parseNode(true, false); }

    private RawEvent parseFlowNode() { return parseNode(false, false); }

    private RawEvent parseBlockNodeOrIndentlessSequence() { return parseNode(true, true); }

    private RawEvent parseNode(boolean block, boolean indentlessSequence) {
        if (check(Token.Kind.ALIAS)) {
            Token t = scanner.nextToken();
            state = states.pop();
            return new RawEvent(Kind.ALIAS, null, null, t.value(), false, '\0', t.start(), t.end());
        }
        String anchor = null, tag = null;
        Mark start = null, end = null;
        if (check(Token.Kind.ANCHOR)) {
            Token t = scanner.nextToken();
            start = t.start(); end = t.end(); anchor = t.value();
            if (check(Token.Kind.TAG)) { t = scanner.nextToken(); end = t.end(); tag = resolveTag(t); }
        } else if (check(Token.Kind.TAG)) {
            Token t = scanner.nextToken();
            start = t.start(); end = t.end(); tag = resolveTag(t);
            if (check(Token.Kind.ANCHOR)) { t = scanner.nextToken(); end = t.end(); anchor = t.value(); }
        }
        if (start == null) { start = scanner.peekToken().start(); end = start; }

        if (indentlessSequence && check(Token.Kind.BLOCK_ENTRY)) {
            end = scanner.peekToken().end();
            state = this::parseIndentlessSequenceEntry;
            return new RawEvent(Kind.SEQUENCE_START, anchor, tag, null, false, '\0', start, end);
        }
        if (check(Token.Kind.SCALAR)) {
            Token t = scanner.nextToken();
            state = states.pop();
            return new RawEvent(Kind.SCALAR, anchor, tag, t.value(), t.plain(), t.style(), start, t.end());
        }
        if (check(Token.Kind.FLOW_SEQUENCE_START)) {
            end = scanner.peekToken().end();
            state = this::parseFlowSequenceFirstEntry;
            return new RawEvent(Kind.SEQUENCE_START, anchor, tag, null, false, '\0', start, end);
        }
        if (check(Token.Kind.FLOW_MAPPING_START)) {
            end = scanner.peekToken().end();
            state = this::parseFlowMappingFirstKey;
            return new RawEvent(Kind.MAPPING_START, anchor, tag, null, false, '\0', start, end);
        }
        if (block && check(Token.Kind.BLOCK_SEQUENCE_START)) {
            end = scanner.peekToken().end();
            state = this::parseBlockSequenceFirstEntry;
            return new RawEvent(Kind.SEQUENCE_START, anchor, tag, null, false, '\0', start, end);
        }
        if (block && check(Token.Kind.BLOCK_MAPPING_START)) {
            end = scanner.peekToken().end();
            state = this::parseBlockMappingFirstKey;
            return new RawEvent(Kind.MAPPING_START, anchor, tag, null, false, '\0', start, end);
        }
        if (anchor != null || tag != null) {
            state = states.pop();
            return new RawEvent(Kind.SCALAR, anchor, tag, "", true, '\0', start, end);
        }
        Token t = scanner.peekToken();
        throw error("Expected the " + (block ? "block" : "flow") + " node content but found " + t.kind(), t.start());
    }

    // ------------------------------------------------------------------ block sequence

    private RawEvent parseBlockSequenceFirstEntry() {
        marks.push(scanner.nextToken().start());
        return parseBlockSequenceEntry();
    }

    private RawEvent parseBlockSequenceEntry() {
        if (check(Token.Kind.BLOCK_ENTRY)) {
            Token t = scanner.nextToken();
            if (!scanner.check(Token.Kind.BLOCK_ENTRY, Token.Kind.BLOCK_END)) {
                states.push(this::parseBlockSequenceEntry);
                return parseBlockNode();
            }
            state = this::parseBlockSequenceEntry;
            return empty(t.end());
        }
        if (!check(Token.Kind.BLOCK_END)) {
            Token t = scanner.peekToken();
            throw error("While parsing a block sequence started at line " + (marks.peek().line() + 1)
                    + ": expected a sequence entry '- ' but found " + t.kind(), t.start());
        }
        Token t = scanner.nextToken();
        state = states.pop();
        marks.pop();
        return RawEvent.simple(Kind.SEQUENCE_END, t.start(), t.end());
    }

    private RawEvent parseIndentlessSequenceEntry() {
        if (check(Token.Kind.BLOCK_ENTRY)) {
            Token t = scanner.nextToken();
            if (!scanner.check(Token.Kind.BLOCK_ENTRY, Token.Kind.KEY, Token.Kind.VALUE, Token.Kind.BLOCK_END)) {
                states.push(this::parseIndentlessSequenceEntry);
                return parseBlockNode();
            }
            state = this::parseIndentlessSequenceEntry;
            return empty(t.end());
        }
        Token t = scanner.peekToken();
        state = states.pop();
        return RawEvent.simple(Kind.SEQUENCE_END, t.start(), t.start());
    }

    // ------------------------------------------------------------------ block mapping

    private RawEvent parseBlockMappingFirstKey() {
        marks.push(scanner.nextToken().start());
        return parseBlockMappingKey();
    }

    private RawEvent parseBlockMappingKey() {
        if (check(Token.Kind.KEY)) {
            Token t = scanner.nextToken();
            if (!scanner.check(Token.Kind.KEY, Token.Kind.VALUE, Token.Kind.BLOCK_END)) {
                states.push(this::parseBlockMappingValue);
                return parseBlockNodeOrIndentlessSequence();
            }
            state = this::parseBlockMappingValue;
            return empty(t.end());
        }
        if (check(Token.Kind.VALUE)) {              // ": value" - an empty key
            state = this::parseBlockMappingValue;
            return empty(scanner.peekToken().start());
        }
        if (!check(Token.Kind.BLOCK_END)) {
            Token t = scanner.peekToken();
            throw error("While parsing a block mapping started at line " + (marks.peek().line() + 1)
                    + ": expected a key but found " + t.kind(), t.start());
        }
        Token t = scanner.nextToken();
        state = states.pop();
        marks.pop();
        return RawEvent.simple(Kind.MAPPING_END, t.start(), t.end());
    }

    private RawEvent parseBlockMappingValue() {
        if (check(Token.Kind.VALUE)) {
            Token t = scanner.nextToken();
            if (!scanner.check(Token.Kind.KEY, Token.Kind.VALUE, Token.Kind.BLOCK_END)) {
                states.push(this::parseBlockMappingKey);
                return parseBlockNodeOrIndentlessSequence();
            }
            state = this::parseBlockMappingKey;
            return empty(t.end());
        }
        state = this::parseBlockMappingKey;
        return empty(scanner.peekToken().start());
    }

    // ------------------------------------------------------------------ flow sequence

    private RawEvent parseFlowSequenceFirstEntry() {
        marks.push(scanner.nextToken().start());
        return parseFlowSequenceEntry(true);
    }

    private RawEvent parseFlowSequenceNextEntry() { return parseFlowSequenceEntry(false); }

    private RawEvent parseFlowSequenceEntry(boolean first) {
        if (!check(Token.Kind.FLOW_SEQUENCE_END)) {
            if (!first) {
                if (check(Token.Kind.FLOW_ENTRY)) {
                    scanner.nextToken();
                } else {
                    Token t = scanner.peekToken();
                    throw error("While parsing a flow sequence: expected ',' or ']' but found " + t.kind(), t.start());
                }
            }
            if (check(Token.Kind.KEY) || check(Token.Kind.VALUE)) {
                Token t = scanner.peekToken();
                state = this::parseFlowSequenceEntryMappingKey;
                return new RawEvent(Kind.MAPPING_START, null, null, null, false, '\0', t.start(), t.end());
            } else if (!check(Token.Kind.FLOW_SEQUENCE_END)) {
                states.push(this::parseFlowSequenceNextEntry);
                return parseFlowNode();
            }
        }
        Token t = scanner.nextToken();
        state = states.pop();
        marks.pop();
        return RawEvent.simple(Kind.SEQUENCE_END, t.start(), t.end());
    }

    private RawEvent parseFlowSequenceEntryMappingKey() {
        if (!check(Token.Kind.KEY)) {              // "[ : value ]" - an empty key
            state = this::parseFlowSequenceEntryMappingValue;
            return empty(scanner.peekToken().start());
        }
        Token t = scanner.nextToken();
        if (!scanner.check(Token.Kind.VALUE, Token.Kind.FLOW_ENTRY, Token.Kind.FLOW_SEQUENCE_END)) {
            states.push(this::parseFlowSequenceEntryMappingValue);
            return parseFlowNode();
        }
        state = this::parseFlowSequenceEntryMappingValue;
        return empty(t.end());
    }

    private RawEvent parseFlowSequenceEntryMappingValue() {
        if (check(Token.Kind.VALUE)) {
            Token t = scanner.nextToken();
            if (!scanner.check(Token.Kind.FLOW_ENTRY, Token.Kind.FLOW_SEQUENCE_END)) {
                states.push(this::parseFlowSequenceEntryMappingEnd);
                return parseFlowNode();
            }
            state = this::parseFlowSequenceEntryMappingEnd;
            return empty(t.end());
        }
        state = this::parseFlowSequenceEntryMappingEnd;
        return empty(scanner.peekToken().start());
    }

    private RawEvent parseFlowSequenceEntryMappingEnd() {
        state = this::parseFlowSequenceNextEntry;
        Token t = scanner.peekToken();
        return RawEvent.simple(Kind.MAPPING_END, t.start(), t.start());
    }

    // ------------------------------------------------------------------ flow mapping

    private RawEvent parseFlowMappingFirstKey() {
        marks.push(scanner.nextToken().start());
        return parseFlowMappingKey(true);
    }

    private RawEvent parseFlowMappingNextKey() { return parseFlowMappingKey(false); }

    private RawEvent parseFlowMappingKey(boolean first) {
        if (!check(Token.Kind.FLOW_MAPPING_END)) {
            if (!first) {
                if (check(Token.Kind.FLOW_ENTRY)) {
                    scanner.nextToken();
                } else {
                    Token t = scanner.peekToken();
                    throw error("While parsing a flow mapping: expected ',' or '}' but found " + t.kind(), t.start());
                }
            }
            if (check(Token.Kind.KEY)) {
                Token t = scanner.nextToken();
                if (!scanner.check(Token.Kind.VALUE, Token.Kind.FLOW_ENTRY, Token.Kind.FLOW_MAPPING_END)) {
                    states.push(this::parseFlowMappingValue);
                    return parseFlowNode();
                }
                state = this::parseFlowMappingValue;
                return empty(t.end());
            } else if (check(Token.Kind.VALUE)) {      // "{ : value }" - an empty key
                state = this::parseFlowMappingValue;
                return empty(scanner.peekToken().start());
            } else if (!check(Token.Kind.FLOW_MAPPING_END)) {
                states.push(this::parseFlowMappingEmptyValue);
                return parseFlowNode();
            }
        }
        Token t = scanner.nextToken();
        state = states.pop();
        marks.pop();
        return RawEvent.simple(Kind.MAPPING_END, t.start(), t.end());
    }

    private RawEvent parseFlowMappingValue() {
        if (check(Token.Kind.VALUE)) {
            Token t = scanner.nextToken();
            if (!scanner.check(Token.Kind.FLOW_ENTRY, Token.Kind.FLOW_MAPPING_END)) {
                states.push(this::parseFlowMappingNextKey);
                return parseFlowNode();
            }
            state = this::parseFlowMappingNextKey;
            return empty(t.end());
        }
        state = this::parseFlowMappingNextKey;
        return empty(scanner.peekToken().start());
    }

    private RawEvent parseFlowMappingEmptyValue() {
        state = this::parseFlowMappingNextKey;
        return empty(scanner.peekToken().start());
    }
}
