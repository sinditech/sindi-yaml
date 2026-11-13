/**
 * 
 */
package za.co.sindi.yaml;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Buhake Sindi
 * @since 08 November 2026
 */
public interface YamlMappingBuilder {

	/**
     * Adds a name/{@code YamlNode} pair to the YAML mapping associated with
     * this mapping builder. If the mapping contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this mapping builder
     * @throws NullPointerException if the specified name or value is null
     */
    YamlMappingBuilder add(String name, YamlNode value);

    /**
     * Adds a name/{@code YamlString} pair to the YAML mapping associated with
     * this mapping builder. If the mapping contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this mapping builder
     * @throws NullPointerException if the specified name or value is null
     */
    YamlMappingBuilder add(String name, String value);

    /**
     * Adds a name/{@code YamlNumber} pair to the YAML mapping associated with
     * this mapping builder. If the mapping contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this mapping builder
     * @throws NullPointerException if the specified name or value is null
     *
     * @see YamlNumber
     */
    YamlMappingBuilder add(String name, BigInteger value);

    /**
     * Adds a name/{@code YamlNumber} pair to the YAML mapping associated with
     * this mapping builder. If the mapping contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this mapping builder
     * @throws NullPointerException if the specified name or value is null
     *
     * @see YamlNumber
     */
    YamlMappingBuilder add(String name, BigDecimal value);

    /**
     * Adds a name/{@code YamlNumber} pair to the YAML mapping associated with
     * this mapping builder. If the mapping contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this mapping builder
     * @throws NullPointerException if the specified name is null
     *
     * @see YamlNumber
     */
    YamlMappingBuilder add(String name, int value);

    /**
     * Adds a name/{@code YamlNumber} pair to the YAML mapping associated with
     * this mapping builder. If the mapping contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this mapping builder
     * @throws NullPointerException if the specified name is null
     *
     * @see YamlNumber
     */
    YamlMappingBuilder add(String name, long value);

    /**
     * Adds a name/{@code YamlNumber} pair to the YAML mapping associated with
     * this mapping builder. If the mapping contains a mapping for the specified
     * name, this method replaces the old value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this mapping builder
     * @throws NumberFormatException if the value is Not-a-Number (NaN) or 
     * infinity
     * @throws NullPointerException if the specified name is null
     *
     * @see YamlNumber
     */
    YamlMappingBuilder add(String name, double value);

    /**
     * Adds a name/{@code YamlNode#TRUE} or name/{@code YamlNode#FALSE} pair
     * to the YAML mapping associated with this mapping builder. If the mapping
     * contains a mapping for the specified name, this method replaces the old
     * value with the specified value.
     *
     * @param name name in the name/value pair
     * @param value value in the name/value pair
     * @return this mapping builder
     * @throws NullPointerException if the specified name is null
     */
    YamlMappingBuilder add(String name, boolean value);

    /**
     * Adds a name/{@code YamlNode#NULL} pair to the YAML mapping associated
     * with this mapping builder where the value is {@code null}.
     * If the mapping contains a mapping for the specified name, this method
     * replaces the old value with {@code null}.
     *
     * @param name name in the name/value pair
     * @return this mapping builder
     * @throws NullPointerException if the specified name is null
     */
    YamlMappingBuilder addNull(String name);

    /**
     * Adds a name/{@code YamlMapping} pair to the YAML mapping associated
     * with this mapping builder. The value {@code YamlMapping} is built from the
     * specified mapping builder. If the mapping contains a mapping for the
     * specified name, this method replaces the old value with the
     * {@code YamlMapping} from the specified mapping builder.
     *
     * @param name name in the name/value pair
     * @param builder the value is the mapping associated with this builder
     * @return this mapping builder
     * @throws NullPointerException if the specified name or builder is null
     */
    YamlMappingBuilder add(String name, YamlMappingBuilder builder);

    /**
     * Adds a name/{@code YamlSequence} pair to the YAML mapping associated with
     * this mapping builder. The value {@code YamlSequence} is built from the
     * specified array builder. If the mapping contains a mapping for the
     * specified name, this method replaces the old value with the
     * {@code YamlSequence} from the specified array builder.
     *
     * @param name the name in the name/value pair
     * @param builder the value is the mapping array with this builder
     * @return this mapping builder
     * @throws NullPointerException if the specified name or builder is null
     */
    YamlMappingBuilder add(String name, YamlSequenceBuilder builder);

    /**
     * Adds all name/value pairs in the YAML mapping associated with the specified
     * mapping builder to the YAML mapping associated with this mapping builder.
     * The newly added name/value pair will replace any existing name/value pair with
     * the same name.
     *
     * @param builder the specified mapping builder
     * @return this mapping builder
     * @throws NullPointerException if the specified builder is null
     * @since 1.1
     */
    default YamlMappingBuilder addAll(YamlMappingBuilder builder) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove the name/value pair from the YAML mapping associated with this
     * mapping builder if it is present.
     *
     * @param name the name in the name/value pair to be removed
     * @return this mapping builder
     * @throws NullPointerException if the specified name is null
     * @since 1.1
     */
    default YamlMappingBuilder remove(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the YAML mapping associated with this mapping builder. 
     * The iteration order for the {@code YamlMapping} is based
     * on the order in which name/value pairs are added to the mapping using
     * this builder.
     *
     * This method clears the builder.
     *
     * @return YAML mapping that is being built
     */
    YamlMapping build();
}
