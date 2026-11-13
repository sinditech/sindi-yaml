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
public interface YamlSequenceBuilder {

	/**
     * Adds a value to the sequence.
     *
     * @param value the YAML value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     */
    YamlSequenceBuilder add(YamlNode value);

    /**
     * Adds a value to the sequence as a {@link YamlString}.
     *
     * @param value the string value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     */
    YamlSequenceBuilder add(String value);

    /**
     * Adds a value to the sequence as a {@link YamlNumber}.
     *
     * @param value the number value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     *
     * @see YamlNumber
     */
    YamlSequenceBuilder add(BigDecimal value);

    /**
     * Adds a value to the sequence as a {@link YamlNumber}.
     *
     * @param value the number value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     *
     * @see YamlNumber
     */
    YamlSequenceBuilder add(BigInteger value);

    /**
     * Adds a value to the sequence as a {@link YamlNumber}.
     *
     * @param value the number value
     * @return this sequence builder
     *
     * @see YamlNumber
     */
    YamlSequenceBuilder add(int value);

    /**
     * Adds a value to the sequence as a {@link YamlNumber}.
     *
     * @param value the number value
     * @return this sequence builder
     *
     * @see YamlNumber
     */
    YamlSequenceBuilder add(long value);

    /**
     * Adds a value to the sequence as a {@link YamlNumber}.
     *
     * @param value the number value
     * @return this sequence builder
     * @throws NumberFormatException if the value is Not-a-Number (NaN) or
     *      infinity
     *
     * @see YamlNumber
     */
    YamlSequenceBuilder add(double value);

    /**
     * Adds a {@link YamlNode#TRUE}  or {@link YamlNode#FALSE} value to the
     * sequence.
     *
     * @param value the boolean value
     * @return this sequence builder
     */
    YamlSequenceBuilder add(boolean value);

    /**
     * Adds a {@link YamlNode#NULL} value to the sequence.
     *
     * @return this sequence builder
     */
    YamlSequenceBuilder addNull();

    /**
     * Adds a {@link YamlMapping} from an object builder to the sequence.
     *
     * @param builder the object builder
     * @return this sequence builder
     * @throws NullPointerException if the specified builder is null
     */
    YamlSequenceBuilder add(YamlMappingBuilder builder);

    /**
     * Adds a {@link YamlSequence} from an sequence builder to the sequence.
     *
     * @param builder the sequence builder
     * @return this sequence builder
     * @throws NullPointerException if the specified builder is null
     */
    YamlSequenceBuilder add(YamlSequenceBuilder builder);

    /**
     * Adds all elements of the sequence in the specified sequence builder to the sequence.
     *
     * @param builder the sequence builder
     * @return this sequence builder
     * @throws NullPointerException if the specified builder is null
     *
     @since 1.1
     */
    default YamlSequenceBuilder addAll(YamlSequenceBuilder builder) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts a value to the sequence at the specified position. Shifts the value
     * currently at that position (if any) and any subsequent values to the right
     * (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param value the YAML value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, YamlNode value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a value to the sequence as a {@link YamlString} at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param value the string value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, String value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a value to the sequence as a {@link YamlNumber} at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, BigDecimal value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a value to the sequence as a {@link YamlNumber} at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, BigInteger value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a value to the sequence as a {@link YamlNumber} at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a value to the sequence as a {@link YamlNumber} at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a value to the sequence as a {@link YamlNumber} at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws NumberFormatException if the value is Not-a-Number (NaN) or
     *      infinity
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a {@link YamlNode#TRUE}  or {@link YamlNode#FALSE} value to the
     * sequence at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param value the boolean value
     * @return this sequence builder
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, boolean value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a {@link YamlNode#NULL} value to the sequence at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @return this sequence builder
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @since 1.1
     */
    YamlSequenceBuilder addNull(int index);

    /**
     * Adds a {@link YamlMapping} from an object builder to the sequence at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param builder the object builder
     * @return this sequence builder
     * @throws NullPointerException if the specified builder is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, YamlMappingBuilder builder) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a {@link YamlSequence} from an sequence builder to the sequence at the specified position.
     * Shifts the value currently at that position (if any) and any subsequent values
     * to the right (adds one to their indices).  Index starts with 0.
     *
     * @param index the position in the sequence
     * @param builder the sequence builder
     * @return this sequence builder
     * @throws NullPointerException if the specified builder is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index > sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder add(int index, YamlSequenceBuilder builder) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with the specified value at the
     * specified position.
     *
     * @param index the position in the sequence
     * @param value the YAML value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, YamlNode value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with the specified value as a
     * {@link YamlString} at the specified position.
     *
     * @param index the position in the sequence
     * @param value the string value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, String value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with the specified value as a
     * {@link YamlNumber} at the specified position.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, BigDecimal value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with the specified value as a
     * {@link YamlNumber} at the specified position.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws NullPointerException if the specified value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, BigInteger value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with the specified value as a
     * {@link YamlNumber} at the specified position.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with the specified value as a
     * {@link YamlNumber} at the specified position.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with the specified value as a
     * {@link YamlNumber} at the specified position.
     *
     * @param index the position in the sequence
     * @param value the number value
     * @return this sequence builder
     * @throws NumberFormatException if the value is Not-a-Number (NaN) or
     *      infinity
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @see YamlNumber
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with
     * a {@link YamlNode#TRUE}  or {@link YamlNode#FALSE} value
     * at the specified position.
     *
     * @param index the position in the sequence
     * @param value the boolean value
     * @return this sequence builder
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, boolean value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with
     * a {@link YamlNode#NULL} value at the specified position.
     *
     * @param index the position in the sequence
     * @return this sequence builder
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @since 1.1
     */
    YamlSequenceBuilder setNull(int index);

    /**
     * Replaces a value in the sequence with the specified value as a
     * {@link YamlMapping} from an object builder at the specified position.
     *
     * @param index the position in the sequence
     * @param builder the object builder
     * @return this sequence builder
     * @throws NullPointerException if the specified builder is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, YamlMappingBuilder builder) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces a value in the sequence with the specified value as a
     * {@link YamlSequence} from an sequence builder at the specified position.
     *
     * @param index the position in the sequence
     * @param builder the sequence builder
     * @return this sequence builder
     * @throws NullPointerException if the specified builder is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder set(int index, YamlSequenceBuilder builder) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove the value in the sequence at the specified position.
     * Shift any subsequent values to the left (subtracts one from their
     * indices.
     *
     * @param index the position in the sequence
     * @return this sequence builder
     * @throws IndexOutOfBoundsException if the index is out of range
     *   {@code (index < 0 || index >= sequence size)}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder remove(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the current sequence.
     *
     * @return the current YAML sequence
     */
    YamlSequence build();
}
