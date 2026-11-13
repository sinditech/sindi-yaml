/**
 * 
 */
package za.co.sindi.yaml.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;

import za.co.sindi.yaml.YamlNode;

/**
 * @author Buhake Sindi
 * @since 08 November 2026
 */
public interface YamlGenerator extends Flushable, Closeable {

	/**
     * Writes the YAML start mapping character. It starts a new child mapping
     * context within which YAML name/value pairs can be written to the mapping.
     * This method is valid only in an sequence context, field context or in no context (when a
     * context is not yet started). This method can only be called once in
     * no context.
     *
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is called within an
     *      mapping context or if it is called more than once in no context.
     */
    YamlGenerator writeStartMapping();

    /**
     * Writes the YAML name/start mapping character pair in the current
     * mapping context. It starts a new child mapping context within which YAML
     * name/value pairs can be written to the mapping.
     *
     * @param name a name within the YAML name/mapping pair to be written
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *     mapping context
     */
    YamlGenerator writeStartMapping(String name);

    /**
     * Writes the YAML name with a colon. It starts a field context, in which valid
     * options are writing a value, starting an mapping or an sequence.
     *
     * Writing value closes field context, if mapping or sequence is started after field name,
     * field context will be closed after mapping/sequence close.
     *
     * @param name name of json field
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *     mapping context
     *
     * @since 1.1
     */
    YamlGenerator writeKey(String name);

    /**
     * Writes the YAML start sequence character. It starts a new child sequence
     * context within which YAML values can be written to the sequence. This
     * method is valid only in an sequence context, field context or in no context (when a
     * context is not yet started). This method can only be called once in
     * no context.
     *
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is called within an
     *      mapping context or if called more than once in no context
     */
    YamlGenerator writeStartSequence();

    /**
     * Writes the YAML name/start sequence character pair with in the current
     * mapping context. It starts a new child sequence context within which YAML
     * values can be written to the sequence.
     *
     * @param name a name within the YAML name/sequence pair to be written
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within
     *      an mapping context
     */
    YamlGenerator writeStartSequence(String name);

    /**
     * Writes a YAML name/value pair in the current mapping context.
     *
     * @param name a name in the YAML name/value pair to be written in
     *             current YAML mapping
     * @param value a value in the YAML name/value pair to be written in
     *             current YAML mapping
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      mapping context
     */
    YamlGenerator write(String name, YamlNode value);

    /**
     * Writes a YAML name/string value pair in the current mapping context.
     * The specified value is written as YAML string value.
     *
     * @param name a name in the YAML name/string pair to be written in
     *             current YAML mapping
     * @param value a value in the YAML name/string pair to be written in
     *             current YAML mapping
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      mapping context
     */
    YamlGenerator write(String name, String value);

    /**
     * Writes a YAML name/number value pair in the current mapping context.
     * The specified value is written as a YAML number value. The string
     * {@code new BigDecimal(value).toString()}
     * is used as the text value for writing.
     *
     * @param name a name in the YAML name/number pair to be written in
     *             current YAML mapping
     * @param value a value in the YAML name/number pair to be written in
     *             current YAML mapping
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      mapping context.
     */
    YamlGenerator write(String name, BigInteger value);

    /**
     * Writes a YAML name/number value pair in the current mapping context.
     * The specified value is written as a YAML number value. The specified
     * value's {@code toString()} is used as the text value for writing.
     *
     * @param name a name in the YAML name/number pair to be written in
     *             current YAML mapping
     * @param value a value in the YAML name/number pair to be written in
     *             current YAML mapping
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      mapping context.
     */
    YamlGenerator write(String name, BigDecimal value);

    /**
     * Writes a YAML name/number value pair in the current mapping context.
     * The specified value is written as a YAML number value. The string
     * {@code new BigDecimal(value).toString()} is used as the text value
     * for writing.
     *
     * @param name a name in the YAML name/number pair to be written in
     *             current YAML mapping
     * @param value a value in the YAML name/number pair to be written in
     *             current YAML mapping
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      mapping context.
     */
    YamlGenerator write(String name, int value);

    /**
     * Writes a YAML name/number value pair in the current mapping context.
     * The specified value is written as a YAML number value. The string
     * {@code new BigDecimal(value).toString()} is used as the text
     * value for writing.
     *
     * @param name a name in the YAML name/number pair to be written in
     *             current YAML mapping
     * @param value a value in the YAML name/number pair to be written in
     *             current YAML mapping
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      mapping context.
     */
    YamlGenerator write(String name, long value);

    /**
     * Writes a YAML name/number value pair in the current mapping context.
     * The specified value is written as a YAML number value. The string
     * {@code BigDecimal.valueOf(double).toString()}
     * is used as the text value for writing.
     *
     * @param name a name in the YAML name/number pair to be written in
     *             current YAML mapping
     * @param value a value in the YAML name/number pair to be written in
     *             current YAML mapping
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws NumberFormatException if the value is Not-a-Number (NaN) or infinity.
     * @throws YamlGenerationException if this method is not called within an
     *      mapping context
     */
    YamlGenerator write(String name, double value);

    /**
     * Writes a YAML name/boolean value pair in the current mapping context.
     * If value is true, it writes the YAML {@code true} value, otherwise
     * it writes the YAML {@code false} value.
     *
     * @param name a name in the YAML name/boolean pair to be written in
     *             current YAML mapping
     * @param value a value in the YAML name/boolean pair to be written in
     *             current YAML mapping
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      mapping context.
     */
    YamlGenerator write(String name, boolean value);


    /**
     * Writes a YAML name/null value pair in an current mapping context.
     *
     * @param name a name in the YAML name/null pair to be written in
     *             current YAML mapping
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      mapping context
     */
    YamlGenerator writeNull(String name);

    /**
     * Writes the end of the current context. If the current context is
     * an sequence context, this method writes the end-of-sequence character (']').
     * If the current context is an mapping context, this method writes the
     * end-of-mapping character ('}'). After writing the end of the current
     * context, the parent context becomes the new current context.
     * If parent context is field context, it is closed.
     *
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is called in no context.
     */
    YamlGenerator writeEnd();

    /**
     * Writes the specified value as a YAML value within
     * the current sequence, field or root context.
     *
     * @param value a value to be written in current YAML sequence
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      sequence, root or field context.
     */
    YamlGenerator write(YamlNode value);

    /**
     * Writes the specified value as a YAML string value within
     * the current sequence, field or root context.
     *
     * @param value a value to be written in current YAML sequence
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      sequence, root or field context.
     */
    YamlGenerator write(String value);

    /**
     * Writes the specified value as a YAML number value within
     * the current sequence, field or root context. The specified value's {@code toString()}
     * is used as the the text value for writing.
     *
     * @param value a value to be written in current YAML sequence
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      sequence, root or field context.
     *
     * @see YamlNumber
     */
    YamlGenerator write(BigDecimal value);

    /**
     * Writes the specified value as a YAML number value within
     * the current sequence, field or root context. The string {@code new BigDecimal(value).toString()}
     * is used as the text value for writing.
     *
     * @param value a value to be written in current YAML sequence
     * @return this generator.
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      sequence, root or field context.
     *
     * @see YamlNumber
     */
    YamlGenerator write(BigInteger value);

    /**
     * Writes the specified value as a YAML number value within
     * the current sequence, field or root context. The string {@code new BigDecimal(value).toString()}
     * is used as the text value for writing.
     *
     * @param value a value to be written in current YAML sequence
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      sequence, root or field context.
     */
    YamlGenerator write(int value);

    /**
     * Writes the specified value as a YAML number value within
     * the current sequence, field or root context. The string {@code new BigDecimal(value).toString()}
     * is used as the text value for writing.
     *
     * @param value a value to be written in current YAML sequence
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      sequence, root or field context.
     */
    YamlGenerator write(long value);

    /**
     * Writes the specified value as a YAML number value within the current
     * sequence, field or root context. The string {@code BigDecimal.valueOf(value).toString()}
     * is used as the text value for writing.
     *
     * @param value a value to be written in current YAML sequence
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      sequence, root or field context.
     * @throws NumberFormatException if the value is Not-a-Number (NaN) or infinity.
     */
    YamlGenerator write(double value);

    /**
     * Writes a YAML true or false value within the current sequence, field or root context.
     * If value is true, this method writes the YAML {@code true} value,
     * otherwise it writes the YAML {@code false} value.
     *
     * @param value a {@code boolean} value
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      sequence, root or field context.
     */
    YamlGenerator write(boolean value);

    /**
     * Writes a YAML null value within the current sequence, field or root context.
     *
     * @return this generator
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if this method is not called within an
     *      sequence, root or field context.
     */
    YamlGenerator writeNull();

    /**
     * Closes this generator and frees any resources associated with it.
     * This method closes the underlying output source.
     *
     * The underlying stream is closed only if complete YAML mapping is written. In case
     * of incomplete mapping YamlGenerationException is thrown and underlying stream is
     * not closed.
     *
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     * @throws YamlGenerationException if an incomplete YAML is generated
     */
    @Override
    void close();

    /**
     * Flushes the underlying output source. If the generator has saved
     * any characters in a buffer, writes them immediately to the underlying
     * output source before flushing it.
     *
     * @throws YamlException if an I/O error occurs (IOException
     * would be cause of YamlException)
     */
    @Override
    void flush();
}
