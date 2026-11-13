package za.co.sindi.yaml.stream;

import java.util.Set;
import java.util.regex.Pattern;

import za.co.sindi.yaml.YamlString;
import za.co.sindi.yaml.node.ScalarResolver;

/**
 * Decides how a string is written: plain whenever that is unambiguous (also for YAML 1.1 readers, so
 * {@code yes}/{@code no}/{@code on}/{@code off}, {@code 1_000}, {@code 12:30} and date-like strings are quoted), literal block scalar for
 * multi-line text, single quotes otherwise, and double quotes with escapes for control characters.
 * 
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public final class ScalarStyler {
    private ScalarStyler() { }

    public enum Style { PLAIN, SINGLE, DOUBLE, LITERAL }

    private static final Set<String> LEGACY_WORDS =
            Set.of("yes", "Yes", "YES", "no", "No", "NO", "on", "On", "ON", "off", "Off", "OFF");
    private static final Pattern LEGACY = Pattern.compile(
            "[-+]?[0-9][0-9_]*(\\.[0-9_]*)?([eE][-+]?[0-9]+)?"
                    + "|[-+]?[0-9][0-9_]*(:[0-5]?[0-9])+(\\.[0-9_]*)?"
                    + "|[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}([Tt ].*)?"
                    + "|[-+]?0[xXbBoO][0-9a-fA-F_]+", Pattern.DOTALL);

    public static boolean isPrintable(int cp) {
        return cp == 0x9 || cp == 0xA || cp == 0xD || (cp >= 0x20 && cp <= 0x7E) || cp == 0x85
                || (cp >= 0xA0 && cp <= 0xD7FF) || (cp >= 0xE000 && cp <= 0xFFFD && cp != 0xFEFF)
                || (cp >= 0x10000 && cp <= 0x10FFFF);
    }

    private static boolean unicodeBreak(int cp) { return cp == 0x85 || cp == 0x2028 || cp == 0x2029; }

    public static boolean isPlainSafe(String s, boolean flow) {
        int len = s.length();
        if (len == 0) return false;
//        if (ScalarResolver.resolvePlain(s).getValueType() != YamlValue.ValueType.STRING) return false;
        if (!(ScalarResolver.resolvePlain(s) instanceof YamlString)) return false;
        if (LEGACY_WORDS.contains(s) || LEGACY.matcher(s).matches()) return false;
        char first = s.charAt(0), last = s.charAt(len - 1);
        if (first == ' ' || first == '\t' || last == ' ' || last == '\t') return false;
        if ((first == '-' || first == '?') && (len == 1 || s.charAt(1) == ' ')) return false;
        if (":,[]{}#&*!|>'\"%@`".indexOf(first) >= 0) return false;
        if (s.startsWith("---") || s.startsWith("...")) return false;
        for (int i = 0; i < len; ) {
            int cp = s.codePointAt(i);
            if (cp < 0x20 || !isPrintable(cp) || unicodeBreak(cp)) return false;
            if (cp == ':' && (i + 1 == len || s.charAt(i + 1) == ' ')) return false;
            if (cp == '#' && i > 0 && s.charAt(i - 1) == ' ') return false;
            if (flow && ",[]{}".indexOf(cp) >= 0) return false;
            i += Character.charCount(cp);
        }
        return true;
    }

    private static boolean singleQuoteSafe(String s) {
        for (int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            if (cp < 0x20 || !isPrintable(cp) || unicodeBreak(cp)) return false;
            i += Character.charCount(cp);
        }
        return true;
    }

    private static boolean literalSafe(String s) {
        int len = s.length();
        int end = len;
        while (end > 0 && s.charAt(end - 1) == '\n') end--;
        if (end == 0) return false;                       // only line breaks: not worth a block scalar
        for (int i = 0; i < len; ) {
            int cp = s.codePointAt(i);
            if (cp == '\n') {
                if (i > 0 && (s.charAt(i - 1) == ' ' || s.charAt(i - 1) == '\t')) return false;   // trailing blanks
            } else if (cp != '\t' && (cp < 0x20 || !isPrintable(cp) || unicodeBreak(cp))) {
                return false;
            }
            i += Character.charCount(cp);
        }
        char last = s.charAt(end - 1);
        if (last == ' ' || last == '\t') return false;
        // The first non-empty line must not start with a space: it would be taken as indentation.
        int p = 0;
        while (p < len && s.charAt(p) == '\n') p++;
        return s.charAt(p) != ' ';
    }

    public static Style choose(String s, boolean flow, boolean allowLiteral) {
        if (isPlainSafe(s, flow)) return Style.PLAIN;
        if (allowLiteral && s.indexOf('\n') >= 0 && literalSafe(s)) return Style.LITERAL;
        if (singleQuoteSafe(s)) return Style.SINGLE;
        return Style.DOUBLE;
    }

    /** A single-line form: plain, single-quoted or double-quoted. */
    public static String inline(String s, boolean flow) { return render(s, choose(s, flow, false)); }

    /** Like {@link #inline} but also quotes the merge key {@code <<}. */
    public static String key(String s, boolean flow) {
        if (s.equals("<<")) return "'<<'";
        return inline(s, flow);
    }

    public static String render(String s, Style style) {
        return switch (style) {
            case PLAIN -> s;
            case SINGLE -> "'" + s.replace("'", "''") + "'";
            case DOUBLE -> doubleQuoted(s);
            case LITERAL -> throw new IllegalArgumentException("Literal scalars are written by the generator");
        };
    }

    public static String doubleQuoted(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 2).append('"');
        for (int i = 0; i < s.length(); ) {
            int cp = s.codePointAt(i);
            i += Character.charCount(cp);
            switch (cp) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\t' -> sb.append("\\t");
                case '\r' -> sb.append("\\r");
                case 0 -> sb.append("\\0");
                case 7 -> sb.append("\\a");
                case 8 -> sb.append("\\b");
                case 11 -> sb.append("\\v");
                case 12 -> sb.append("\\f");
                case 27 -> sb.append("\\e");
                case 0x85 -> sb.append("\\N");
                case 0xA0 -> sb.append("\\_");
                case 0x2028 -> sb.append("\\L");
                case 0x2029 -> sb.append("\\P");
                default -> {
                    if (isPrintable(cp) && cp >= 0x20) sb.appendCodePoint(cp);
                    else if (cp <= 0xFF) sb.append(String.format("\\x%02X", cp));
                    else if (cp <= 0xFFFF) sb.append(String.format("\\u%04X", cp));
                    else sb.append(String.format("\\U%08X", cp));
                }
            }
        }
        return sb.append('"').toString();
    }
}
