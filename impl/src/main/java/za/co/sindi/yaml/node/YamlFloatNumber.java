/**
 * 
 */
package za.co.sindi.yaml.node;

import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public record YamlFloatNumber(Float value) implements za.co.sindi.yaml.YamlNumber<Float> {

	public YamlFloatNumber {
		value = Objects.requireNonNull(value, "A float value is required.");
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (value.isNaN()) return ".nan";
		if (value.isInfinite()) return value > 0 ? ".inf" : "-.inf";
		return Float.toString(value);
	}
}
