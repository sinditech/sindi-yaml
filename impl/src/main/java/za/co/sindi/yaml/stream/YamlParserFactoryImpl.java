/**
 * 
 */
package za.co.sindi.yaml.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import za.co.sindi.commons.utils.Charsets;
import za.co.sindi.yaml.YamlContext;
import za.co.sindi.yaml.YamlException;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public record YamlParserFactoryImpl(YamlContext yamlContext) implements YamlParserFactory {

	@Override
	public YamlParser createParser(Reader reader) {
		// TODO Auto-generated method stub
		return new YamlParserImpl(reader, yamlContext);
	}

	@Override
	public YamlParser createParser(InputStream in) {
		// TODO Auto-generated method stub
		try {
			return createParser(in, Charsets.detect(in));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new YamlException("I/O error while detecting the encoding", e);
		}
	}

	@Override
	public YamlParser createParser(InputStream in, Charset charset) {
		// TODO Auto-generated method stub
		return createParser(new InputStreamReader(in, charset));
	}
}
