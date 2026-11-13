/**
 * 
 */
package za.co.sindi.yaml;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public non-sealed interface YamlNumber<N extends Number> extends YamlScalar {
	
	/**
     * Returns true if this YAML number is a integral number. This method
     * semantics are defined using {@code bigDecimalValue().scale()}. If the
     * scale is zero, then it is considered integral type. This integral type
     * information can be used to invoke an appropriate accessor method to
     * obtain a numeric value as in the following example:
     *
     * <pre>
     * <code>
     * YamlNumber num = ...
     * if (num.isIntegral()) {
     *     num.value();     // or other methods to get integral value
     * } else {
     *     num.value();   // or other methods to get decimal number value
     * }
     * </code>
     * </pre>
     *
     * @return true if this number is a integral number, otherwise false
     */
    default boolean isIntegral() { return false; }

	N value();
}
