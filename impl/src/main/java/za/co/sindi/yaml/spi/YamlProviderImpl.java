/**
 * 
 */
package za.co.sindi.yaml.spi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

import za.co.sindi.commons.utils.Charsets;
import za.co.sindi.yaml.YamlBoolean;
import za.co.sindi.yaml.YamlBuilderFactory;
import za.co.sindi.yaml.YamlBuilderFactoryImpl;
import za.co.sindi.yaml.YamlContext;
import za.co.sindi.yaml.YamlException;
import za.co.sindi.yaml.YamlMapping;
import za.co.sindi.yaml.YamlMappingBuilder;
import za.co.sindi.yaml.YamlMappingBuilderImpl;
import za.co.sindi.yaml.YamlNumber;
import za.co.sindi.yaml.YamlReader;
import za.co.sindi.yaml.YamlReaderFactory;
import za.co.sindi.yaml.YamlReaderFactoryImpl;
import za.co.sindi.yaml.YamlReaderImpl;
import za.co.sindi.yaml.YamlSequence;
import za.co.sindi.yaml.YamlSequenceBuilder;
import za.co.sindi.yaml.YamlSequenceBuilderImpl;
import za.co.sindi.yaml.YamlString;
import za.co.sindi.yaml.YamlWriter;
import za.co.sindi.yaml.YamlWriterFactory;
import za.co.sindi.yaml.YamlWriterFactoryImpl;
import za.co.sindi.yaml.YamlWriterImpl;
import za.co.sindi.yaml.node.YamlBigDecimalNumber;
import za.co.sindi.yaml.node.YamlBigIntegerNumber;
import za.co.sindi.yaml.node.YamlDoubleNumber;
import za.co.sindi.yaml.node.YamlIntegerNumber;
import za.co.sindi.yaml.node.YamlLongNumber;
import za.co.sindi.yaml.stream.YamlGenerator;
import za.co.sindi.yaml.stream.YamlGeneratorFactory;
import za.co.sindi.yaml.stream.YamlGeneratorFactoryImpl;
import za.co.sindi.yaml.stream.YamlGeneratorImpl;
import za.co.sindi.yaml.stream.YamlParser;
import za.co.sindi.yaml.stream.YamlParserFactory;
import za.co.sindi.yaml.stream.YamlParserFactoryImpl;
import za.co.sindi.yaml.stream.YamlParserImpl;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public class YamlProviderImpl extends YamlProvider {
	
	private final YamlContext defaultContext = YamlContext.DEFAULT;

	@Override
	public YamlParser createParser(Reader reader) {
		// TODO Auto-generated method stub
		return new YamlParserImpl(reader, defaultContext);
	}

	@Override
	public YamlParser createParser(InputStream in) {
		// TODO Auto-generated method stub
		try {
			return createParser(new InputStreamReader(in, Charsets.detect(in)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new YamlException("I/O error while detecting the encoding", e);
		}
	}

	@Override
	public YamlParserFactory createParserFactory(Map<String, ?> config) {
		// TODO Auto-generated method stub
		return new YamlParserFactoryImpl(config == null ? defaultContext : YamlContext.from(config));
	}

	@Override
	public YamlGenerator createGenerator(Writer writer) {
		// TODO Auto-generated method stub
		return new YamlGeneratorImpl(writer, defaultContext);
	}

	@Override
	public YamlGenerator createGenerator(OutputStream out) {
		// TODO Auto-generated method stub
		return createGenerator(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)));
	}

	@Override
	public YamlGeneratorFactory createGeneratorFactory(Map<String, ?> config) {
		// TODO Auto-generated method stub
		return new YamlGeneratorFactoryImpl(config == null ? defaultContext : YamlContext.from(config));
	}

	@Override
	public YamlReader createReader(Reader reader) {
		// TODO Auto-generated method stub
		return new YamlReaderImpl(reader, defaultContext);
	}

	@Override
	public YamlReader createReader(InputStream in) {
		// TODO Auto-generated method stub
		try {
			return createReader(new InputStreamReader(in, Charsets.detect(in)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new YamlException("I/O error while detecting the encoding", e);
		}
	}

	@Override
	public YamlWriter createWriter(Writer writer) {
		// TODO Auto-generated method stub
		return new YamlWriterImpl(writer, defaultContext);
	}

	@Override
	public YamlWriter createWriter(OutputStream out) {
		// TODO Auto-generated method stub
		return createWriter(new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8)));
	}

	@Override
	public YamlWriterFactory createWriterFactory(Map<String, ?> config) {
		// TODO Auto-generated method stub
		return new YamlWriterFactoryImpl(config == null ? defaultContext : YamlContext.from(config));
	}

	@Override
	public YamlReaderFactory createReaderFactory(Map<String, ?> config) {
		// TODO Auto-generated method stub
		return new YamlReaderFactoryImpl(config == null ? defaultContext : YamlContext.from(config));
	}

	@Override
	public YamlMappingBuilder createMappingBuilder() {
		// TODO Auto-generated method stub
		return new YamlMappingBuilderImpl();
	}

	@Override
	public YamlMappingBuilder createMappingBuilder(YamlMapping mapping) {
		// TODO Auto-generated method stub
		return new YamlMappingBuilderImpl(mapping);
	}

	@Override
	public YamlMappingBuilder createMappingBuilder(Map<String, ?> map) {
		// TODO Auto-generated method stub
		return new YamlMappingBuilderImpl(map);
	}

	@Override
	public YamlSequenceBuilder createSequenceBuilder() {
		// TODO Auto-generated method stub
		return new YamlSequenceBuilderImpl();
	}

	@Override
	public YamlSequenceBuilder createSequenceBuilder(YamlSequence sequence) {
		// TODO Auto-generated method stub
		return new YamlSequenceBuilderImpl(sequence);
	}

	@Override
	public YamlSequenceBuilder createSequenceBuilder(Collection<?> collection) {
		// TODO Auto-generated method stub
		return new YamlSequenceBuilderImpl(collection);
	}

	@Override
	public YamlBuilderFactory createBuilderFactory(Map<String, ?> config) {
		// TODO Auto-generated method stub
		return new YamlBuilderFactoryImpl(config == null ? defaultContext : YamlContext.from(config));
	}

	@Override
	public YamlString createValue(String value) {
		// TODO Auto-generated method stub
		return new za.co.sindi.yaml.node.YamlString(value);
	}

	@Override
	public YamlNumber<Integer> createValue(int value) {
		// TODO Auto-generated method stub
		return new YamlIntegerNumber(value);
	}

	@Override
	public YamlNumber<Long> createValue(long value) {
		// TODO Auto-generated method stub
		return new YamlLongNumber(value);
	}

	@Override
	public YamlNumber<Double> createValue(double value) {
		// TODO Auto-generated method stub
		return new YamlDoubleNumber(value);
	}

	@Override
	public YamlNumber<BigDecimal> createValue(BigDecimal value) {
		// TODO Auto-generated method stub
		return new YamlBigDecimalNumber(value);
	}

	@Override
	public YamlNumber<BigInteger> createValue(BigInteger value) {
		// TODO Auto-generated method stub
		return new YamlBigIntegerNumber(value);
	}

	@Override
	public YamlBoolean createValue(boolean value) {
		// TODO Auto-generated method stub
		return new za.co.sindi.yaml.node.YamlBoolean(value);
	}
}
