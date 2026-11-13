/**
 * 
 */
package za.co.sindi.yaml.node;

import java.math.BigInteger;
import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public record YamlBigIntegerNumber(BigInteger value) implements za.co.sindi.yaml.YamlNumber<BigInteger> {

	public YamlBigIntegerNumber {
		value = Objects.requireNonNull(value, "A BigInteger value is required.");
	}

	@Override
	public boolean isIntegral() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value.toString();
	}
}
