/**
 * 
 */
package za.co.sindi.yaml;

import java.util.Map;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public non-sealed interface YamlMapping extends YamlStructure , Map<String, YamlNode> {

	@Override
	default Kind getKind() {
		// TODO Auto-generated method stub
		return Kind.MAPPING;
	}
	
	/**
     * Returns the array value to which the specified name is mapped.
     * This is a convenience method for {@code (YamlSequence)get(name)} to
     * get the value.
     *
     * @param name the name whose associated value is to be returned
     * @return the array value to which the specified name is mapped, or
     *         {@code null} if this object contains no mapping for the name
     * @throws ClassCastException if the value to which the specified name
     * is mapped is not assignable to YamlSequence type
     */
    YamlSequence getYamlSequence(String name);

    /**
     * Returns the object value to which the specified name is mapped.
     * This is a convenience method for {@code (YamlMapping)get(name)} to
     * get the value.
     *
     * @param name the name whose associated value is to be returned
     * @return the object value to which the specified name is mapped, or
     *         {@code null} if this object contains no mapping for the name
     * @throws ClassCastException if the value to which the specified name
     * is mapped is not assignable to YamlMapping type
     */
    YamlMapping getYamlMapping(String name);

    /**
     * Returns the number value to which the specified name is mapped.
     * This is a convenience method for {@code (YamlNumber)get(name)} to
     * get the value.
     *
     * @param name the name whose associated value is to be returned
     * @return the number value to which the specified name is mapped, or
     *         {@code null} if this object contains no mapping for the name
     * @throws ClassCastException if the value to which the specified name
     * is mapped is not assignable to YamlNumber type
     */
    <X extends Number> YamlNumber<X> getYamlNumber(String name);

    /**
     * Returns the string value to which the specified name is mapped.
     * This is a convenience method for {@code (YamlString)get(name)} to
     * get the value.
     *
     * @param name the name whose associated value is to be returned
     * @return the string value to which the specified name is mapped, or
     *         {@code null} if this object contains no mapping for the name
     * @throws ClassCastException if the value to which the specified name
     * is mapped is not assignable to YamlString type
     */
    YamlString getYamlString(String name);
    
    /**
     * Returns the boolean value to which the specified name is mapped.
     * This is a convenience method for {@code (YamlBoolean)get(name)} to
     * get the value.
     *
     * @param name the name whose associated value is to be returned
     * @return the boolean value to which the specified name is mapped, or
     *         {@code null} if this object contains no mapping for the name
     * @throws ClassCastException if the value to which the specified name
     * is mapped is not assignable to YamlBoolean type
     */
    YamlBoolean getYamlBoolean(String name);

    /**
     * A convenience method for
     * {@code getYamlString(name).value()}
     *
     * @param name whose associated value is to be returned as String
     * @return the String value to which the specified name is mapped
     * @throws NullPointerException if the specified name doesn't have any
     * mapping
     * @throws ClassCastException if the value for specified name mapping
     * is not assignable to YamlString
     */
    String getString(String name);

    /**
     * Returns the string value of the associated {@code YamlString} mapping
     * for the specified name. If {@code YamlString} is found, then its
     * {@link jakarta.json.YamlString#value()} is returned. Otherwise,
     * the specified default value is returned.
     *
     * @param name whose associated value is to be returned as String
     * @param defaultValue a default value to be returned
     * @return the string value of the associated mapping for the name,
     * or the default value
     */
    String getString(String name, String defaultValue);

    /**
     * A convenience method for
     * {@code getYamlNumber(name).intValue()}
     *
     * @param name whose associated value is to be returned as int
     * @return the int value to which the specified name is mapped
     * @throws NullPointerException if the specified name doesn't have any
     * mapping
     * @throws ClassCastException if the value for specified name mapping
     * is not assignable to YamlNumber
     */
    int getInt(String name);

    /**
     * Returns the int value of the associated {@code YamlNumber} mapping
     * for the specified name. If {@code YamlNumber} is found, then its
     * {@link YamlNumber#value()} is returned. Otherwise,
     * the specified default value is returned.
     *
     * @param name whose associated value is to be returned as int
     * @param defaultValue a default value to be returned
     * @return the int value of the associated mapping for the name,
     * or the default value
     */
    int getInt(String name, int defaultValue);

    /**
     * Returns the boolean value of the associated mapping for the specified
     * name. If the associated mapping is YamlValue.TRUE, then returns true.
     * If the associated mapping is YamlValue.FALSE, then returns false.
     *
     * @param name whose associated value is to be returned as boolean
     * @return the boolean value to which the specified name is mapped
     * @throws NullPointerException if the specified name doesn't have any
     * mapping
     * @throws ClassCastException if the value for specified name mapping
     * is not assignable to YamlValue.TRUE or YamlValue.FALSE
     */
    boolean getBoolean(String name);

    /**
     * Returns the boolean value of the associated mapping for the specified
     * name. If the associated mapping is YamlValue.TRUE, then returns true.
     * If the associated mapping is YamlValue.FALSE, then returns false.
     * Otherwise, the specified default value is returned.
     *
     * @param name whose associated value is to be returned as int
     * @param defaultValue a default value to be returned
     * @return the boolean value of the associated mapping for the name,
     * or the default value
     */
    boolean getBoolean(String name, boolean defaultValue);

    /**
     * Returns {@code true} if the associated value for the specified name is
     * {@code YamlValue.NULL}.
     *
     * @param name name whose associated value is checked
     * @return return true if the associated value is {@code YamlValue.NULL},
     * otherwise false
     * @throws NullPointerException if the specified name doesn't have any
     * mapping
     */
    boolean isNull(String name);

}
