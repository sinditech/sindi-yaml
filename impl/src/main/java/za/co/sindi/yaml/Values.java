/**
 * 
 */
package za.co.sindi.yaml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import za.co.sindi.yaml.node.YamlBigDecimalNumber;
import za.co.sindi.yaml.node.YamlBigIntegerNumber;
import za.co.sindi.yaml.node.YamlBoolean;
import za.co.sindi.yaml.node.YamlDoubleNumber;
import za.co.sindi.yaml.node.YamlFloatNumber;
import za.co.sindi.yaml.node.YamlIntegerNumber;
import za.co.sindi.yaml.node.YamlLongNumber;
import za.co.sindi.yaml.node.YamlMapping;
import za.co.sindi.yaml.node.YamlNull;
import za.co.sindi.yaml.node.YamlSequence;
import za.co.sindi.yaml.node.YamlString;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public final class Values {

	private Values() {
		throw new AssertionError("Private constructor.");
	}
	
	public static YamlScalar string(String s) { return s == null ? new YamlNull() : new YamlString(s); }

    public static YamlBoolean bool(boolean b) { return new YamlBoolean(b); }

    public static YamlNode of(Object o) {
        return switch (o) {
            case null -> new YamlNull();
            case YamlNode v -> v;
            case CharSequence s -> new YamlString(s.toString());
            case Character c -> new YamlString(c.toString());
            case Boolean b -> bool(b);
            case Integer n -> new YamlIntegerNumber(n);
            case Long n -> new YamlLongNumber(n);
            case Short n -> new YamlIntegerNumber(n.intValue());
            case Byte n -> new YamlIntegerNumber(n.intValue());
            case BigInteger n -> new YamlBigIntegerNumber(n);
            case BigDecimal n -> new YamlBigDecimalNumber(n);
            case Double n -> new YamlDoubleNumber(n);
            case Float n -> new YamlFloatNumber(n);
            case Enum<?> e -> new YamlString(e.name());
            case Map<?, ?> m -> {
                Map<String, YamlNode> copy = new LinkedHashMap<>();
                for (Map.Entry<?, ?> e : m.entrySet()) copy.put(String.valueOf(e.getKey()), of(e.getValue()));
                yield new YamlMapping(copy);
            }
            case Collection<?> c -> {
                List<YamlNode> copy = new ArrayList<>(c.size());
                for (Object e : c) copy.add(of(e));
                yield new YamlSequence(copy);
            }
            case Object[] a -> {
                List<YamlNode> copy = new ArrayList<>(a.length);
                for (Object e : a) copy.add(of(e));
                yield new YamlSequence(copy);
            }
            default -> throw new IllegalArgumentException("Unsupported value type: " + o.getClass().getName());
        };
    }
}
