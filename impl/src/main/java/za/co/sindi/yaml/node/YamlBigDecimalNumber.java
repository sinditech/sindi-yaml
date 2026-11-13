/**
 * 
 */
package za.co.sindi.yaml.node;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public record YamlBigDecimalNumber(BigDecimal value) implements za.co.sindi.yaml.YamlNumber<BigDecimal> {

	public YamlBigDecimalNumber {
		value = Objects.requireNonNull(value, "A BigDecimal value is required.");
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value.toString();
	}
}
