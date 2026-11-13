/**
 * 
 */
package za.co.sindi.yaml.node;

import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public record YamlLongNumber(Long value) implements za.co.sindi.yaml.YamlNumber<Long> {

	public YamlLongNumber {
		value = Objects.requireNonNull(value, "A long value is required.");
	}

	@Override
	public boolean isIntegral() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Long.toString(value);
	}
}
