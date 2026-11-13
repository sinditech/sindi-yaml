/**
 * 
 */
package za.co.sindi.yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import za.co.sindi.commons.utils.Charsets;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public record YamlReaderFactoryImpl(YamlContext yamlContext) implements YamlReaderFactory {

	@Override
	public YamlReader createReader(Reader reader) {
		// TODO Auto-generated method stub
		return new YamlReaderImpl(reader, yamlContext);
	}

	@Override
	public YamlReader createReader(InputStream in) {
		// TODO Auto-generated method stub
		try {
			return createReader(in, Charsets.detect(in));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new YamlException("I/O error while detecting the encoding", e);
		}
	}

	@Override
	public YamlReader createReader(InputStream in, Charset charset) {
		// TODO Auto-generated method stub
		return createReader(new InputStreamReader(in, charset));
	}
}
