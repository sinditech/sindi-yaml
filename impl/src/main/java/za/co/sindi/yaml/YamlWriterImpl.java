/**
 * 
 */
package za.co.sindi.yaml;

import java.io.Writer;
import java.util.Map;

import za.co.sindi.yaml.stream.YamlGeneratorImpl;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public class YamlWriterImpl implements YamlWriter {

	private final YamlGeneratorImpl generator;
	private boolean writeDone;
	
	/**
	 * @param out
	 * @param yamlContext
	 */
	public YamlWriterImpl(Writer out, YamlContext yamlContext) {
		generator = new YamlGeneratorImpl(out, yamlContext);
	}
	
	@Override
	public void writeSequence(YamlSequence sequence) {
		// TODO Auto-generated method stub
		ensureWrite();
		writeDone = true;
        generator.writeStartSequence();
        for(YamlNode node : sequence) {
            generator.write(node);
        }
        generator.writeEnd();
        generator.flush();
	}

	@Override
	public void writeMapping(YamlMapping mapping) {
		// TODO Auto-generated method stub
		ensureWrite();
        writeDone = true;
        generator.writeStartMapping();
        for(Map.Entry<String, YamlNode> e : mapping.entrySet()) {
            generator.write(e.getKey(), e.getValue());
        }
        generator.writeEnd();
        generator.flush();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		generator.close();
		writeDone = true;
	}
	
	private void ensureWrite() {
		if (writeDone) {
			throw new IllegalStateException("Writer write already called.");
		}
	}
}
