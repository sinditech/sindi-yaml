/**
 * 
 */
package za.co.sindi.yaml.stream;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import za.co.sindi.yaml.YamlContext;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public record YamlGeneratorFactoryImpl(YamlContext yamlContext) implements YamlGeneratorFactory {

	@Override
	public YamlGenerator createGenerator(Writer writer) {
		// TODO Auto-generated method stub
		return new YamlGeneratorImpl(writer, yamlContext);
	}

	@Override
	public YamlGenerator createGenerator(OutputStream out) {
		// TODO Auto-generated method stub
		return createGenerator(out, StandardCharsets.UTF_8);
	}

	@Override
	public YamlGenerator createGenerator(OutputStream out, Charset charset) {
		// TODO Auto-generated method stub
		return createGenerator(new BufferedWriter(new OutputStreamWriter(out, charset)));
	}

	@Override
	public Map<String, ?> getConfigInUse() {
		// TODO Auto-generated method stub
		return yamlContext.raw();
	}
}
