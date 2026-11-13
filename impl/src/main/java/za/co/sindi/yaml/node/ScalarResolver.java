package za.co.sindi.yaml.node;

import java.math.BigDecimal;
import java.math.BigInteger;

import za.co.sindi.yaml.YamlNumber;
import za.co.sindi.yaml.YamlScalar;

/**
 * Resolves scalar text to a typed value using the YAML 1.2 <em>core schema</em>:
 * {@code null|Null|NULL|~|<empty>}, {@code true|True|TRUE|false|False|FALSE}, decimal / {@code 0o} octal /
 * {@code 0x} hex integers, floats, {@code .inf} and {@code .nan}. Quoted and block scalars are always strings
 * unless an explicit {@code !!int}, {@code !!float}, {@code !!bool} or {@code !!null} tag says otherwise.
 * (YAML 1.1 words such as {@code yes}/{@code no} are deliberately <em>not</em> booleans.)
 */
public final class ScalarResolver {
    private ScalarResolver() { }

    private static final String YAML_TAG_PREFIX = "tag:yaml.org,2002:";

    /**
     * @param tag normalised tag or {@code null}
     * @throws IllegalArgumentException if an explicit tag does not fit the text
     */
    public static YamlScalar resolve(String text, boolean plain, String tag) {
        if (tag != null) {
            if (tag.startsWith(YAML_TAG_PREFIX)) {
                switch (tag.substring(YAML_TAG_PREFIX.length())) {
                    case "str" -> { return new YamlString(text); }
                    case "null" -> { return new YamlNull(); }
                    case "bool" -> {
                        return switch (text) {
                            case "true", "True", "TRUE" -> new YamlBoolean(true);
                            case "false", "False", "FALSE" -> new YamlBoolean(false);
                            default -> throw new IllegalArgumentException("Not a boolean: '" + text + "'");
                        };
                    }
                    case "int" -> {
                    	YamlScalar v = text.isEmpty() ? null : parseNumber(text);
                        if (v == null || !((YamlNumber<?>) v).isIntegral()) throw new IllegalArgumentException("Not an integer: '" + text + "'");
                        return v;
                    }
                    case "float" -> {
                    	YamlScalar v = text.isEmpty() ? null : parseNumber(text);
                        if (v == null) throw new IllegalArgumentException("Not a float: '" + text + "'");
                        return v;
                    }
                    default -> { return new YamlString(text); }
                }
            }
            return new YamlString(text);   // "!" and application specific tags: keep the text
        }
        return plain ? resolvePlain(text) : new YamlString(text);
    }

    /** Implicit resolution of a plain scalar. */
    public static YamlScalar resolvePlain(String s) {
        switch (s) {
            case "", "~", "null", "Null", "NULL": return new YamlNull();
            case "true", "True", "TRUE": return new YamlBoolean(true);
            case "false", "False", "FALSE": return new YamlBoolean(false);
            default: break;
        }
        char c = s.charAt(0);
        if ((c >= '0' && c <= '9') || c == '-' || c == '+' || c == '.') {
            YamlNumber<?> n = parseNumber(s);
            if (n != null) return n;
        }
        return new YamlString(s);
    }

    private static boolean digit(char c) { return c >= '0' && c <= '9'; }

    /** @return the number for a core-schema numeric lexeme, or {@code null} if {@code s} is not one */
    static YamlNumber<? extends Number> parseNumber(String s) {
        int len = s.length();
        int i = 0;
        boolean negative = false;
        char first = s.charAt(0);
        if (first == '+' || first == '-') {
            negative = first == '-';
            i = 1;
            if (len == 1) return null;
        }
        String rest = s.substring(i);
        if (rest.equals(".inf") || rest.equals(".Inf") || rest.equals(".INF")) {
            return new YamlDoubleNumber(negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY); //, s);
        }
        if (i == 0 && (s.equals(".nan") || s.equals(".NaN") || s.equals(".NAN"))) {
            return new YamlDoubleNumber(Double.NaN); //, s);
        }
        if (i == 0 && len > 2 && first == '0') {
            char k = s.charAt(1);
            if (k == 'x' || k == 'o') {
                int radix = k == 'x' ? 16 : 8;
                for (int p = 2; p < len; p++) if (Character.digit(s.charAt(p), radix) < 0) return null;
                BigInteger b = new BigInteger(s.substring(2), radix);
//                return YamlNumberImpl.parsed(b.bitLength() < 63 ? (Number) b.longValue() : b, s);
                return b.bitLength() < 63 ? new YamlLongNumber(b.longValue()) : new YamlBigIntegerNumber(b);
            }
        }
        int j = i;
        while (j < len && digit(s.charAt(j))) j++;
        int before = j - i;
        if (j == len) {
            if (before == 0) return null;
//            Number n = before <= 18 ? (Number) Long.parseLong(s) : new BigInteger(s);
//            return YamlNumberImpl.parsed(n, s);
            return before <= 18 ? new YamlLongNumber(Long.parseLong(s)) : new YamlBigIntegerNumber(new BigInteger(s));
        }
        int after = 0;
        if (s.charAt(j) == '.') {
            j++;
            int k = j;
            while (j < len && digit(s.charAt(j))) j++;
            after = j - k;
        }
        if (before == 0 && after == 0) return null;
        if (j < len && (s.charAt(j) == 'e' || s.charAt(j) == 'E')) {
            j++;
            if (j < len && (s.charAt(j) == '+' || s.charAt(j) == '-')) j++;
            int k = j;
            while (j < len && digit(s.charAt(j))) j++;
            if (j == k) return null;
        }
        if (j != len) return null;
        return new YamlBigDecimalNumber(new BigDecimal(s));//, s);
    }
}
