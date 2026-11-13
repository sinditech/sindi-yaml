/**
 * 
 */
package za.co.sindi.yaml.node;

import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public record YamlDoubleNumber(Double value) implements za.co.sindi.yaml.YamlNumber<Double> {

	public YamlDoubleNumber {
		value = Objects.requireNonNull(value, "A double value is required.");
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (value.isNaN()) return ".nan";
		if (value.isInfinite()) return value > 0 ? ".inf" : "-.inf";
		return Double.toString(value);
	}
}
