/**
 * 
 */
package za.co.sindi.yaml;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public non-sealed interface YamlSequence extends YamlStructure, List<YamlNode> {

	@Override
	default Kind getKind() {
		// TODO Auto-generated method stub
		return Kind.SEQUENCE;
	}

	/**
     * Returns the object value at the specified position in this array.
     * This is a convenience method for {@code (YamlMapping)get(index)}.
     *
     * @param index index of the value to be returned
     * @return the value at the specified position in this array
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to the YamlMapping type
     */
    YamlMapping getYamlMapping(int index);

    /**
     * Returns the array value at the specified position in this array.
     * This is a convenience method for {@code (YamlSequence)get(index)}.
     *
     * @param index index of the value to be returned
     * @return the value at the specified position in this array
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to the YamlSequence type
     */
    YamlSequence getYamlSequence(int index);

    /**
     * Returns the number value at the specified position in this array.
     * This is a convenience method for {@code (YamlNumber)get(index)}.
     *
     * @param index index of the value to be returned
     * @return the value at the specified position in this array
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to the YamlNumber type
     */
    <X extends Number> YamlNumber<X> getYamlNumber(int index);

    /**
     * Returns the string value at the specified position in this array.
     * This is a convenience method for {@code (YamlString)get(index)}.
     *
     * @param index index of the value to be returned
     * @return the value at the specified position in this array
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to the YamlString type
     */
    YamlString getYamlString(int index);
    
    /**
     * Returns the boolean value at the specified position in this array.
     * This is a convenience method for {@code (YamlBoolean)get(index)}.
     *
     * @param index index of the value to be returned
     * @return the value at the specified position in this array
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to the YamlBoolean type
     */
    YamlBoolean getYamlBoolean(int index);

    /**
     * Returns a list view of the specified type for the array. This method
     * does not verify if there is a value of wrong type in the array. Providing
     * this typesafe view dynamically may cause a program fail with a
     * {@code ClassCastException}, if there is a value of wrong type in this
     * array. Unfortunately, the exception can occur at any time after this
     * method returns.
     *
     * @param <T> The type of the List for the array
     * @param clazz a YamlNode type
     * @return a list view of the specified type
     */
    <T extends YamlNode> List<T> getValuesAs(Class<T> clazz);

    /**
     * Returns a list view for the array. The value and the type of the elements
     * in the list is specified by the {@code func} argument.
     * <p>This method can be used to obtain a list of the unwrapped types, such as
     * <pre>{@code
     *     List<String> strings = ary1.getValuesAs(YamlString::getString);
     *     List<Integer> ints = ary2.getValuesAs(YamlNumber::intValue);
     * } </pre>
     * or a list of simple projections, such as
     * <pre> {@code
     *     List<Integer> stringsizes = ary1.getValueAs((YamlString v)->v.getString().length();
     * } </pre>
     * @param <K> The element type (must be a subtype of YamlNode) of this YamlSequence.
     * @param <T> The element type of the returned List
     * @param func The function that maps the elements of this YamlSequence to the target elements.
     * @return A List of the specified values and type.
     * @throws ClassCastException if the {@code YamlSequence} contains a value of wrong type
     *
     * @since 1.1
     */
    default <T, K extends YamlNode> List<T> getValuesAs(Function<K, T> func) {
        @SuppressWarnings("unchecked")
        Stream<K> stream = (Stream<K>) stream();
        return stream.map(func).collect(Collectors.toList());
    }

    /**
     * A convenience method for
     * {@code getYamlString(index).getString()}.
     *
     * @param index index of the {@code YamlString} value
     * @return the String value at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to {@code YamlString}
     */
    String getString(int index);

    /**
     * Returns the {@code String} value of {@code YamlString} at the specified
     * position in this YAML array values. If {@code YamlString} is found,
     * its {@link jakarta.json.YamlString#getString()} is returned. Otherwise,
     * the specified default value is returned.
     *
     * @param index index of the {@code YamlString} value
     * @param defaultValue the String to return if the {@code YamlNode} at the
     *    specified position is not a {@code YamlString}
     * @return the String value at the specified position in this array,
     * or the specified default value
     */
    String getString(int index, String defaultValue);

    /**
     * A convenience method for
     * {@code getYamlNumber(index).intValue()}.
     *
     * @param index index of the {@code YamlNumber} value
     * @return the int value at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to {@code YamlNumber}
     */
    int getInt(int index);

    /**
     * Returns the int value of the {@code YamlNumber} at the specified position. 
     * If the value at that position is a {@code YamlNumber},
     * this method returns {@link jakarta.json.YamlNumber#intValue()}. Otherwise
     * this method returns the specified default value.
     *
     * @param index index of the {@code YamlNumber} value
     * @param defaultValue the int value to return if the {@code YamlNode} at
     *     the specified position is not a {@code YamlNumber}
     * @return the int value at the specified position in this array,
     * or the specified default value
     */
    int getInt(int index, int defaultValue);

    /**
     * Returns the boolean value at the specified position.
     * If the value at the specified position is {@code YamlNode.TRUE} 
     * this method returns {@code true}. If the value at the specified position 
     * is {@code YamlNode.FALSE} this method returns {@code false}.
     *
     * @param index index of the YAML boolean value
     * @return the boolean value at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * {@code YamlNode.TRUE} or {@code YamlNode.FALSE}
     */
    boolean getBoolean(int index);

    /**
     * Returns the boolean value at the specified position.
     * If the value at the specified position is {@code YamlNode.TRUE}
     * this method returns {@code true}. If the value at the specified position 
     * is {@code YamlNode.FALSE} this method returns {@code false}. 
     * Otherwise this method returns the specified default value.
     *
     * @param index index of the YAML boolean value
     * @param defaultValue the boolean value to return if the {@code YamlNode}
     *    at the specified position is neither TRUE nor FALSE
     * @return the boolean value at the specified position,
     * or the specified default value
     */
    boolean getBoolean(int index, boolean defaultValue);

    /**
     * Returns {@code true} if the value at the specified location in this
     * array is {@code YamlNode.NULL}.
     *
     * @param index index of the YAML null value
     * @return return true if the value at the specified location is
     * {@code YamlNode.NULL}, otherwise false
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    boolean isNull(int index);
}
