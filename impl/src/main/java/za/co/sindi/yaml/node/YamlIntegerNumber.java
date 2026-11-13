/**
 * 
 */
package za.co.sindi.yaml.node;

import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public record YamlIntegerNumber(Integer value) implements za.co.sindi.yaml.YamlNumber<Integer> {

	public YamlIntegerNumber {
		value = Objects.requireNonNull(value, "An integer value is required.");
	}

	@Override
	public boolean isIntegral() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Integer.toString(value);
	}
}
