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
        if (rest.equalsIgnoreCase(".inf")) {
            return new YamlDoubleNumber(negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
        }
        if (i == 0 && (s.equals(".nan") || s.equals(".NaN") || s.equals(".NAN"))) {
            return new YamlDoubleNumber(Double.NaN);
        }

        // hex / octal (optional leading sign)
        if (rest.length() > 2 && rest.charAt(0) == '0') {
            char k = rest.charAt(1);
            if (k == 'x' || k == 'o') {
                int radix = k == 'x' ? 16 : 8;
                for (int p = 2; p < rest.length(); p++) {
                    if (Character.digit(rest.charAt(p), radix) < 0) return null;
                }
                BigInteger b = new BigInteger(rest.substring(2), radix);
                if (negative) b = b.negate();
                return integralNumber(b);
            }
        }

        int j = i;
        while (j < len && digit(s.charAt(j))) j++;
        int before = j - i;

        // pure integer
        if (j == len) {
            if (before == 0) return null;
            return integralNumber(new BigInteger(s));
        }

        // fractional / exponent → float hierarchy
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

        return decimalNumber(new BigDecimal(s));
    }

	/** int → long → BigInteger by actual range */
	private static YamlNumber<? extends Number> integralNumber(BigInteger b) {
	    try {
	        return new YamlIntegerNumber(b.intValueExact());
	    } catch (ArithmeticException ignored) { }
	    try {
	        return new YamlLongNumber(b.longValueExact());
	    } catch (ArithmeticException ignored) { }
	    return new YamlBigIntegerNumber(b);
	}

	/**
	 * Prefer float, then double, when the binary value round-trips to the same BigDecimal;
	 * otherwise keep exact BigDecimal.
	 */
	private static YamlNumber<? extends Number> decimalNumber(BigDecimal bd) {
	    float f = bd.floatValue();
	    if (Float.isFinite(f) && new BigDecimal(Float.toString(f)).compareTo(bd) == 0) {
	        return new YamlFloatNumber(f);
	    }
	    double d = bd.doubleValue();
	    if (Double.isFinite(d) && new BigDecimal(Double.toString(d)).compareTo(bd) == 0) {
	        return new YamlDoubleNumber(d);
	    }
	    return new YamlBigDecimalNumber(bd);
	}
}
