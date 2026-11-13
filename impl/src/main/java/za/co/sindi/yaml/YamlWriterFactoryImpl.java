/**
 * 
 */
package za.co.sindi.yaml;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public record YamlWriterFactoryImpl(YamlContext yamlContext) implements YamlWriterFactory {

	@Override
	public YamlWriter createWriter(Writer writer) {
		// TODO Auto-generated method stub
		return new YamlWriterImpl(writer, yamlContext);
	}

	@Override
	public YamlWriter createWriter(OutputStream out) {
		// TODO Auto-generated method stub
		return createWriter(out, StandardCharsets.UTF_8);
	}

	@Override
	public YamlWriter createWriter(OutputStream out, Charset charset) {
		// TODO Auto-generated method stub
		return createWriter(new BufferedWriter(new OutputStreamWriter(out, charset)));
	}

	@Override
	public Map<String, ?> getConfigInUse() {
		// TODO Auto-generated method stub
		return yamlContext.raw();
	}
}
