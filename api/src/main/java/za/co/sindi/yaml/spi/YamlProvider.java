/**
 * 
 */
package za.co.sindi.yaml.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import za.co.sindi.yaml.YamlBoolean;
import za.co.sindi.yaml.YamlBuilderFactory;
import za.co.sindi.yaml.YamlException;
import za.co.sindi.yaml.YamlMapping;
import za.co.sindi.yaml.YamlMappingBuilder;
import za.co.sindi.yaml.YamlNode;
import za.co.sindi.yaml.YamlNumber;
import za.co.sindi.yaml.YamlReader;
import za.co.sindi.yaml.YamlReaderFactory;
import za.co.sindi.yaml.YamlSequence;
import za.co.sindi.yaml.YamlSequenceBuilder;
import za.co.sindi.yaml.YamlString;
import za.co.sindi.yaml.YamlWriter;
import za.co.sindi.yaml.YamlWriterFactory;
import za.co.sindi.yaml.stream.YamlGenerator;
import za.co.sindi.yaml.stream.YamlGeneratorFactory;
import za.co.sindi.yaml.stream.YamlParser;
import za.co.sindi.yaml.stream.YamlParserFactory;

/**
 * @author Buhake Sindi
 * @since 10 October 2026
 */
public abstract class YamlProvider {
	
	/**
     * A constant representing the name of the default
     * {@code YamlProvider} implementation class.
     */
//    private static final String DEFAULT_PROVIDER
//            = "za.co.sindi.yaml.spi.YamlProviderImpl";

	/**
     * Default constructor.
     */
    protected YamlProvider() {
    }
	
    public static YamlProvider provider() {
        ServiceLoader<YamlProvider> loader = ServiceLoader.load(YamlProvider.class);
        Iterator<YamlProvider> it = loader.iterator();
        if (it.hasNext()) {
            return it.next();
        }

//        try {
//            Class<?> clazz = Class.forName(DEFAULT_PROVIDER);
//            return (YamlProvider) clazz.getConstructor().newInstance();
//        } catch (ClassNotFoundException x) {
//            throw new YamlException(
//                    "Provider " + DEFAULT_PROVIDER + " not found", x);
//        } catch (Exception x) {
//            throw new YamlException(
//                    "Provider " + DEFAULT_PROVIDER + " could not be instantiated: " + x,
//                    x);
//        }
        
        throw new YamlException("No YamlProvider could be found.");
    }
    


    /**
     * Creates a YAML parser from a character stream.
     *
     * @param reader i/o reader from which YAML is to be read
     * @return a YAML parser
     */
    public abstract YamlParser createParser(Reader reader);

    /**
     * Creates a YAML parser from the specified byte stream.
     * The character encoding of the stream is determined
     * as defined in <a href="http://tools.ietf.org/rfc/rfc7159.txt">RFC 7159
     * </a>.
     *
     * @param in i/o stream from which YAML is to be read
     * @throws YamlException if encoding cannot be determined
     *         or i/o error (IOException would be cause of YamlException)
     * @return a YAML parser
     */
    public abstract YamlParser createParser(InputStream in);

    /**
     * Creates a parser factory for creating {@link YamlParser} instances.
     *
     * @return a YAML parser factory
     *
    public abstract YamlParserFactory createParserFactory();
     */

    /**
     * Creates a parser factory for creating {@link YamlParser} instances.
     * The factory is configured with the specified map of
     * provider specific configuration properties. Provider implementations
     * should ignore any unsupported configuration properties specified in
     * the map.
     *
     * @param config a map of provider specific properties to configure the
     *               YAML parsers. The map may be empty or null
     * @return a YAML parser factory
     */
    public abstract YamlParserFactory createParserFactory(Map<String, ?> config);

    /**
     * Creates a YAML generator for writing YAML text to a character stream.
     *
     * @param writer a i/o writer to which YAML is written
     * @return a YAML generator
     */
    public abstract YamlGenerator createGenerator(Writer writer);

    /**
     * Creates a YAML generator for writing YAML text to a byte stream.
     *
     * @param out i/o stream to which YAML is written
     * @return a YAML generator
     */
    public abstract YamlGenerator createGenerator(OutputStream out);

    /**
     * Creates a generator factory for creating {@link YamlGenerator} instances.
     *
     * @return a YAML generator factory
     *
    public abstract YamlGeneratorFactory createGeneratorFactory();
     */

    /**
     * Creates a generator factory for creating {@link YamlGenerator} instances.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should
     * ignore any unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               YAML generators. The map may be empty or null
     * @return a YAML generator factory
     */
    public abstract YamlGeneratorFactory createGeneratorFactory(Map<String, ?> config);

    /**
     * Creates a YAML reader from a character stream.
     *
     * @param reader a reader from which YAML is to be read
     * @return a YAML reader
     */
    public abstract YamlReader createReader(Reader reader);

    /**
     * Creates a YAML reader from a byte stream. The character encoding of
     * the stream is determined as described in
     * <a href="http://tools.ietf.org/rfc/rfc7159.txt">RFC 7159</a>.
     *
     * @param in a byte stream from which YAML is to be read
     * @return a YAML reader
     */
    public abstract YamlReader createReader(InputStream in);

    /**
     * Creates a YAML writer to write a
     * YAML {@link YamlMapping mapping} or {@link YamlSequence sequence}
     * structure to the specified character stream.
     *
     * @param writer to which YAML mapping or sequence is written
     * @return a YAML writer
     */
    public abstract YamlWriter createWriter(Writer writer);

    /**
     * Creates a YAML writer to write a
     * YAML {@link YamlMapping mapping} or {@link YamlSequence sequence}
     * structure to the specified byte stream. Characters written to
     * the stream are encoded into bytes using UTF-8 encoding.
     *
     * @param out to which YAML mapping or sequence is written
     * @return a YAML writer
     */
    public abstract YamlWriter createWriter(OutputStream out);

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
    public abstract YamlWriterFactory createWriterFactory(Map<String,?> config);

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
    public abstract YamlReaderFactory createReaderFactory(Map<String,?> config);

    /**
     * Creates a YAML mapping builder.
     *
     * @return a YAML mapping builder
     */
    public abstract YamlMappingBuilder createMappingBuilder();

    /**
     * Creates a YAML mapping builder, initialized with the specified mapping.
     *
     * @param mapping the initial YAML mapping in the builder
     * @return a YAML mapping builder
     *
     * @since 1.1
     */
    public YamlMappingBuilder createMappingBuilder(YamlMapping mapping) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a YAML mapping builder, initialized with the data from specified {@code map}.
     * If the @{code map} contains {@link Optional}s then resulting YAML mapping builder
     * contains the key from the {@code map} only if the {@link Optional} is not empty.
     *
     * @param map the initial object in the builder
     * @return a YAML mapping builder
     * @exception IllegalArgumentException if the value from the {@code map} cannot be converted
     *            to the corresponding {@link YamlNode}
     *
     * @since 1.1
     */
    public YamlMappingBuilder createMappingBuilder(Map<String, ?> map) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a YAML sequence builder.
     *
     * @return a YAML sequence builder
     */
    public abstract YamlSequenceBuilder createSequenceBuilder();

    /**
     * Creates a YAML sequence builder, initialized with the specified sequence.
     *
     * @param sequence the initial YAML sequence in the builder
     * @return a YAML sequence builder
     *
     * @since 1.1
     */
    public YamlSequenceBuilder createSequenceBuilder(YamlSequence sequence) {
        throw new UnsupportedOperationException();
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
    public YamlSequenceBuilder createSequenceBuilder(Collection<?> collection) {
        throw new UnsupportedOperationException();
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
    public abstract YamlBuilderFactory createBuilderFactory(Map<String,?> config);
    
    /**
     * Creates a YamlString.
     *
     * @param value a YAML string
     * @return the YamlString for the string
     *
     * @since 1.1
     */
    public YamlString createValue(String value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public YamlNumber<Integer> createValue(int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public YamlNumber<Long> createValue(long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public YamlNumber<Double> createValue(double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public YamlNumber<BigDecimal> createValue(BigDecimal value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a YamlNumber.
     *
     * @param value a YAML number
     * @return the YamlNumber for the number
     *
     * @since 1.1
     */
    public YamlNumber<BigInteger> createValue(BigInteger value) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Creates a YamlBoolean.
     *
     * @param value a YAML boolean
     * @return the YamlBoolean for the value
     *
     * @since 1.1
     */
    public YamlBoolean createValue(boolean value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a YamlNumber.
     *
     * When it is not implemented it checks the type and delegates
     * to an existing method that already handles that type. It throws
     * UnsupportedOperationException in case the type is not known.
     *
     * @param number a YAML number
     * @return the YamlNumber for the number
     *
     * @since 2.1
     */
    @SuppressWarnings("unchecked")
	public <N extends Number> YamlNumber<N> createValue(N number) {
        if (number instanceof Integer) {
            return (YamlNumber<N>) createValue(number.intValue());
        } else if (number instanceof Long) {
            return (YamlNumber<N>) createValue(number.longValue());
        } else if (number instanceof Double) {
            return (YamlNumber<N>) createValue(number.doubleValue());
        } else if (number instanceof BigInteger) {
            return (YamlNumber<N>) createValue((BigInteger) number);
        } else if (number instanceof BigDecimal) {
            return (YamlNumber<N>) createValue((BigDecimal) number);
        } else {
            throw new UnsupportedOperationException(number + " type is not known");
        }
    }
}
