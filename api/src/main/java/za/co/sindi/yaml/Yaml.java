/**
 * 
 */
package za.co.sindi.yaml;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import za.co.sindi.yaml.spi.YamlProvider;
import za.co.sindi.yaml.stream.YamlGenerator;
import za.co.sindi.yaml.stream.YamlGeneratorFactory;
import za.co.sindi.yaml.stream.YamlParser;
import za.co.sindi.yaml.stream.YamlParserFactory;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public final class Yaml {

	private Yaml() {
	}

	/**
     * Creates a YAML parser from a character stream.
     *
     * @param reader i/o reader from which YAML is to be read
     * @return a YAML parser
     */
    public static YamlParser createParser(Reader reader) {
        return YamlProvider.provider().createParser(reader);
    }

    /**
     * Creates a YAML parser from a byte stream.
     * The character encoding of the stream is determined as specified in
     * <a href="http://tools.ietf.org/rfc/rfc7159.txt">RFC 7159</a>.
     *
     * @param in i/o stream from which YAML is to be read
     * @throws YamlException if encoding cannot be determined
     *         or i/o error (IOException would be cause of YamlException)
     * @return a YAML parser
     */
    public static YamlParser createParser(InputStream in) {
        return YamlProvider.provider().createParser(in);
    }

    /**
     * Creates a YAML generator for writing YAML to a character stream.
     *
     * @param writer a i/o writer to which YAML is written
     * @return a YAML generator
     */
    public static YamlGenerator createGenerator(Writer writer) {
        return YamlProvider.provider().createGenerator(writer);
    }

    /**
     * Creates a YAML generator for writing YAML to a byte stream.
     *
     * @param out i/o stream to which YAML is written
     * @return a YAML generator
     */
    public static YamlGenerator createGenerator(OutputStream out) {
        return YamlProvider.provider().createGenerator(out);
    }

    /**
     * Creates a parser factory for creating {@link YamlParser} mappings.
     *
     * @return YAML parser factory.
     *
    public static YamlParserFactory createParserFactory() {
        return YamlProvider.provider().createParserFactory();
    }
     */

    /**
     * Creates a parser factory for creating {@link YamlParser} mappings.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               YAML parsers. The map may be empty or null
     * @return YAML parser factory
     */
    public static YamlParserFactory createParserFactory(Map<String, ?> config) {
        return YamlProvider.provider().createParserFactory(config);
    }

    /**
     * Creates a generator factory for creating {@link YamlGenerator} mappings.
     *
     * @return YAML generator factory
     *
    public static YamlGeneratorFactory createGeneratorFactory() {
        return YamlProvider.provider().createGeneratorFactory();
    }
    */

    /**
     * Creates a generator factory for creating {@link YamlGenerator} mappings.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               YAML generators. The map may be empty or null
     * @return YAML generator factory
     */
    public static YamlGeneratorFactory createGeneratorFactory(
            Map<String, ?> config) {
        return YamlProvider.provider().createGeneratorFactory(config);
    }

    /**
     * Creates a YAML writer to write a
     * YAML {@link YamlMapping mapping} or {@link YamlSequence sequence}
     * structure to the specified character stream.
     *
     * @param writer to which YAML mapping or sequence is written
     * @return a YAML writer
     */
    public static YamlWriter createWriter(Writer writer) {
        return YamlProvider.provider().createWriter(writer);
    }

    /**
     * Creates a YAML writer to write a
     * YAML {@link YamlMapping mapping} or {@link YamlSequence sequence}
     * structure to the specified byte stream. Characters written to
     * the stream are encoded into bytes using UTF-8 encoding.
     *
     * @param out to which YAML mapping or sequence is written
     * @return a YAML writer
     */
    public static YamlWriter createWriter(OutputStream out) {
        return YamlProvider.provider().createWriter(out);
    }

    /**
     * Creates a YAML reader from a character stream.
     *
     * @param reader a reader from which YAML is to be read
     * @return a YAML reader
     */
    public static YamlReader createReader(Reader reader) {
        return YamlProvider.provider().createReader(reader);
    }

    /**
     * Creates a YAML reader from a byte stream. The character encoding of
     * the stream is determined as described in
     * <a href="http://tools.ietf.org/rfc/rfc7159.txt">RFC 7159</a>.
     *
     * @param in a byte stream from which YAML is to be read
     * @return a YAML reader
     */
    public static YamlReader createReader(InputStream in) {
        return YamlProvider.provider().createReader(in);
    }

    /**
     * Creates a reader factory for creating {@link YamlReader} mappings.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               YAML readers. The map may be empty or null
     * @return a YAML reader factory
     */
    public static YamlReaderFactory createReaderFactory(Map<String, ?> config) {
        return YamlProvider.provider().createReaderFactory(config);
    }

    /**
     * Creates a writer factory for creating {@link YamlWriter} mappings.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               YAML writers. The map may be empty or null
     * @return a YAML writer factory
     */
    public static YamlWriterFactory createWriterFactory(Map<String, ?> config) {
        return YamlProvider.provider().createWriterFactory(config);
    }

    /**
     * Creates a YAML sequence builder
     *
     * @return a YAML sequence builder
     */
    public static YamlSequenceBuilder createSequenceBuilder() {
        return YamlProvider.provider().createSequenceBuilder();
    }

    /**
     * Creates a YAML sequence builder, initialized with the specified sequence
     *
     * @param sequence the initial sequence in the builder
     * @return a YAML sequence builder
     *
     * @since 1.1
     */
    public static YamlSequenceBuilder createSequenceBuilder(YamlSequence sequence) {
        return YamlProvider.provider().createSequenceBuilder(sequence);
    }

    /**
     * Creates a YAML sequence builder, initialized with the content of specified {@code collection}.
     * If the @{code collection} contains {@link Optional}s then resulting YAML sequence builder
     * contains the value from the {@code collection} only if the {@link Optional} is not empty.
     *
     * @param collection the initial data for the builder
     * @return a YAML sequence builder
     * @exception IllegalArgumentException if the value from the {@code collection} cannot be converted
     *            to the corresponding {@link YamlNode}
     *
     * @since 1.1
     */
    public static YamlSequenceBuilder createSequenceBuilder(Collection<?> collection) {
        return YamlProvider.provider().createSequenceBuilder(collection);
    }

    /**
     * Creates a YAML mapping builder
     *
     * @return a YAML mapping builder
     */
    public static YamlMappingBuilder createMappingBuilder() {
        return YamlProvider.provider().createMappingBuilder();
    }

    /**
     * Creates a YAML mapping builder, initialized with the specified mapping.
     *
     * @param mapping the initial mapping in the builder
     * @return a YAML mapping builder
     *
     * @since 1.1
     */
    public static YamlMappingBuilder createMappingBuilder(YamlMapping mapping) {
        return YamlProvider.provider().createMappingBuilder(mapping);
    }

    /**
     * Creates a YAML mapping builder, initialized with the data from specified {@code map}.
     * If the @{code map} contains {@link Optional}s then resulting YAML mapping builder
     * contains the key from the {@code map} only if the {@link Optional} is not empty.
     *
     * @param map the initial mapping in the builder
     * @return a YAML mapping builder
     * @exception IllegalArgumentException if the value from the {@code map} cannot be converted
     *            to the corresponding {@link YamlNode}
     *
     * @since 1.1
     */
    public static YamlMappingBuilder createMappingBuilder(Map<String, ?> map) {
        return YamlProvider.provider().createMappingBuilder(map);
    }
    
    /**
     * Creates a builder factory for creating {@link YamlSequenceBuilder}
     * and {@link YamlMappingBuilder} mappings.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               YAML builders. The map may be empty or null
     * @return a YAML builder factory
     */
    public static YamlBuilderFactory createBuilderFactory(
            Map<String, ?> config) {
        return YamlProvider.provider().createBuilderFactory(config);
    }

    /**
     * Creates a YamlString.
     *
     * @param value a YAML string
     * @return the YamlString for the string
     *
     * @since 1.1
     */
    public static YamlString createValue(String value) {
        return YamlProvider.provider().createValue(value);
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public static YamlNumber<Integer> createValue(int value) {
        return YamlProvider.provider().createValue(value);
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public static YamlNumber<Long> createValue(long value) {
        return YamlProvider.provider().createValue(value);
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public static YamlNumber<Double> createValue(double value) {
        return YamlProvider.provider().createValue(value);
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public static YamlNumber<BigDecimal> createValue(BigDecimal value) {
        return YamlProvider.provider().createValue(value);
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public static YamlNumber<BigInteger> createValue(BigInteger value) {
        return YamlProvider.provider().createValue(value);
    }

    /**
     * Creates a YamlBoolean.
     *
     * @param value a YAML boolean
     * @return the YamlBoolean for the value
     *
     * @since 1.1
     */
    public static YamlBoolean createValue(boolean value) {
        return YamlProvider.provider().createValue(value);
    }
    
    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 2.1
     */
    public static <N extends Number> YamlNumber<N> createValue(N value) {
        return YamlProvider.provider().createValue(value);
    }
}
