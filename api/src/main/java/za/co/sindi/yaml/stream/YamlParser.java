/**
 * 
 */
package za.co.sindi.yaml.stream;

import java.io.Closeable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Stream;

import za.co.sindi.yaml.YamlException;
import za.co.sindi.yaml.YamlMapping;
import za.co.sindi.yaml.YamlNode;
import za.co.sindi.yaml.YamlReader;
import za.co.sindi.yaml.YamlSequence;

/**
 * Pull parser over a YAML stream, in the spirit of {@code YamlParser}. Anchors and aliases are
 * expanded transparently; merge keys ({@code <<}) are reported as ordinary keys (they are applied
 * by {@link YamlReader}, which builds the object model).
 *
 * <pre>
 * START_DOCUMENT, START_MAPPING, KEY_NAME, VALUE_NUMBER, KEY_NAME, START_SEQUENCE, VALUE_STRING,
 * END_SEQUENCE, END_MAPPING, END_DOCUMENT
 * </pre>
 * 
 * @author Buhake Sindi
 * @since 10 October 2026
 */
public interface YamlParser extends Closeable {
	
	enum Event {
        START_DOCUMENT, END_DOCUMENT,
        START_MAPPING, END_MAPPING,
        START_SEQUENCE, END_SEQUENCE,
        KEY_NAME,
        VALUE_STRING, VALUE_NUMBER, VALUE_TRUE, VALUE_FALSE, VALUE_NULL
    }
	
	/**
     * Returns {@code true} if there are more parsing states. This method returns
     * {@code false} if the parser reaches the end of the YAML text.
     *
     * @return {@code true} if there are more parsing states.
     * @throws YamlException if an i/o error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlParsingException if the parser encounters invalid YAML
     * when advancing to next state.
     */
    boolean hasNext();
    
    /**
     * Returns the event for the next parsing state.
     *
     * @throws YamlException if an i/o error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlParsingException if the parser encounters invalid YAML
     * when advancing to next state.
     * @throws java.util.NoSuchElementException if there are no more parsing
     * states.
     * @return the event for the next parsing state
     */
    Event next();
    
    /**
     * Returns a {@code String} for the name in a name/value pair,
     * for a string value or a number value. This method should only be called
     * when the parser state is {@link Event#KEY_NAME}, {@link Event#VALUE_STRING},
     * or {@link Event#VALUE_NUMBER}.
     *
     * @return a name when the parser state is {@link Event#KEY_NAME}
     *         a string value when the parser state is {@link Event#VALUE_STRING}
     *         a number value when the parser state is {@link Event#VALUE_NUMBER}
     * @throws IllegalStateException when the parser state is not
     *      {@code KEY_NAME}, {@code VALUE_STRING}, or {@code VALUE_NUMBER}
     */
    String getString();

    /**
     * Returns true if the YAML number at the current parser state is a
     * integral number. A {@link BigDecimal} may be used to store the value
     * internally and this method semantics are defined using its
     * {@code scale()}. If the scale is zero, then it is considered integral
     * type. This integral type information can be used to invoke an
     * appropriate accessor method to obtain a numeric value as in the
     * following example:
     *
     * <pre>
     * <code>
     * YamlParser parser = ...
     * if (parser.isIntegralNumber()) {
     *     parser.getInt();     // or other methods to get integral value
     * } else {
     *     parser.getBigDecimal();
     * }
     * </code>
     * </pre>
     *
     * @return true if this number is a integral number, otherwise false
     * @throws IllegalStateException when the parser state is not
     *      {@code VALUE_NUMBER}
     */
    boolean isIntegralNumber();

    /**
     * Returns a YAML number as an integer. The returned value is equal
     * to {@code new BigDecimal(getString()).intValue()}. Note that
     * this conversion can lose information about the overall magnitude
     * and precision of the number value as well as return a result with
     * the opposite sign. This method should only be called when the parser
     * state is {@link Event#VALUE_NUMBER}.
     *
     * @return an integer for a YAML number
     * @throws IllegalStateException when the parser state is not
     *      {@code VALUE_NUMBER}
     * @see java.math.BigDecimal#intValue()
     */
    int getInt();

    /**
     * Returns a YAML number as a long. The returned value is equal
     * to {@code new BigDecimal(getString()).longValue()}. Note that this
     * conversion can lose information about the overall magnitude and
     * precision of the number value as well as return a result with
     * the opposite sign. This method is only called when the parser state is
     * {@link Event#VALUE_NUMBER}.
     *
     * @return a long for a YAML number
     * @throws IllegalStateException when the parser state is not
     *      {@code VALUE_NUMBER}
     * @see java.math.BigDecimal#longValue()
     */
    long getLong();

    /**
     * Returns a YAML number as a {@code BigDecimal}. The {@code BigDecimal}
     * is created using {@code new BigDecimal(getString())}. This
     * method should only called when the parser state is
     * {@link Event#VALUE_NUMBER}.
     *
     * @return a {@code BigDecimal} for a YAML number
     * @throws IllegalStateException when the parser state is not
     *      {@code VALUE_NUMBER}
     */
    BigDecimal getBigDecimal();

    /**
     * Return the location that corresponds to the parser's current state in
     * the YAML input source. The location information is only valid in the
     * current parser state (or until the parser is advanced to a next state).
     *
     * @return a non-null location corresponding to the current parser state
     * in YAML input source
     */
    YamlLocation getLocation();

    /**
     * Returns a {@code YamlMapping} and advances the parser to the
     * corresponding {@code END_OBJECT}.
     *
     * @return the {@code YamlMapping} at the current parser position
     * @throws jakarta.json.YamlException if an i/o error occurs (IOException
     * would be cause of YamlException)
     * @throws IllegalStateException when the parser state is not
     *     {@code START_OBJECT}
     * @throws YamlParsingException if the parser encounters invalid YAML
     * when advancing to next state.
     * @throws java.util.NoSuchElementException if there are no more parsing
     * states.
     *
     * @since 1.1
     */
    default public YamlMapping getMapping() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a {@code YamlNode} at the current parser position.
     * If the parser state is {@code START_ARRAY}, the behavior is
     * the same as {@link #getSequence}. If the parser state is
     * {@code START_OBJECT}, the behavior is the same as
     * {@link #getMapping}. For all other cases, if applicable, the YAML value is
     * read and returned.
     *
     * @return the {@code YamlNode} at the current parser position.
     * @throws jakarta.json.YamlException if an i/o error occurs (IOException
     * would be cause of YamlException)
     * @throws IllegalStateException when the parser state is
     *     {@code END_OBJECT} or {@code END_ARRAY}
     * @throws YamlParsingException if the parser encounters invalid YAML
     * when advancing to next state.
     * @throws java.util.NoSuchElementException if there are no more parsing
     * states.
     *
     * @since 1.1
     */
    default public YamlNode getValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a {@code YamlSequence} and advance the parser to the
     * the corresponding {@code END_ARRAY}.
     *
     * @return the {@code YamlSequence} at the current parser position
     * @throws jakarta.json.YamlException if an i/o error occurs (IOException
     * would be cause of YamlException)
     * @throws IllegalStateException when the parser state is not
     *     {@code START_ARRAY}
     * @throws YamlParsingException if the parser encounters invalid YAML
     * when advancing to next state.
     * @throws java.util.NoSuchElementException if there are no more parsing
     * states.
     *
     * @since 1.1
     */
    default public YamlSequence getSequence() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a stream of the {@code YamlSequence} elements.
     * The parser state must be {@code START_ARRAY}.
     * The elements are read lazily, on an as-needed basis, as
     * required by the stream operations.
     * If the stream operations do not consume
     * all of the sequence elements, {@link skipSequence} can be used to
     * skip the unprocessed sequence elements.
     *
     * @return a stream of elements of the {@code YamlSequence}
     *
     * @throws IllegalStateException when the parser state is not
     *     {@code START_ARRAY}
     *
     * @since 1.1
     */
    default public Stream<YamlNode> getSequenceStream() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a stream of the {@code YamlMapping}'s
     * name/value pairs. The parser state must be {@code START_OBJECT}.
     * The name/value pairs are read lazily, on an as-needed basis, as
     * required by the stream operations.
     * If the stream operations do not consume
     * all of the object's name/value pairs, {@link skipMapping} can be
     * used to skip the unprocessed elements.
     *
     * @return a stream of name/value pairs of the {@code YamlMapping}
     *
     * @throws IllegalStateException when the parser state is not
     *     {@code START_OBJECT}
     *
     * @since 1.1
     */
    default public Stream<Map.Entry<String,YamlNode>> getMappingStream() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a stream of {@code YamlNode} from a sequence of
     * YAML nodes. The nodes are read lazily, on an as-needed basis,
     * as needed by the stream operations.
     *
     * @return a Stream of {@code YamlNode}
     *
     * @throws IllegalStateException if the parser is in an sequence or object.
     *
     * @since 1.1
     */
    default public Stream<YamlNode> getNodeStream() {
        throw new UnsupportedOperationException();
    }

    /**
     * Advance the parser to {@code END_ARRAY}.
     * If the parser is in sequence context, i.e. it has previously
     * encountered a {@code START_ARRAY} without encountering the
     * corresponding {@code END_ARRAY}, the parser is advanced to
     * the corresponding {@code END_ARRAY}.
     * If the parser is not in any sequence context, nothing happens.
     *
     * @since 1.1
     */
    default public void skipSequence() {
        throw new UnsupportedOperationException();
    }

    /**
     * Advance the parser to {@code END_OBJECT}.
     * If the parser is in object context, i.e. it has previously
     * encountered a {@code START_OBJECT} without encountering the
     * corresponding {@code END_OBJECT}, the parser is advanced to
     * the corresponding {@code END_OBJECT}.
     * If the parser is not in any object context, nothing happens.
     *
     * @since 1.1
     */
    default public void skipMapping() {
        throw new UnsupportedOperationException();
    }

	/**
     * Closes this parser and frees any resources associated with the
     * parser. This method closes the underlying input source.
     *
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     */
    @Override
    void close();
}
