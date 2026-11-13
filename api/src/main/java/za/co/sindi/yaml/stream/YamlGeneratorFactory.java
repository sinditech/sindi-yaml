/**
 * 
 */
package za.co.sindi.yaml.stream;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Buhake Sindi
 * @since 08 November 2026
 */
public interface YamlGeneratorFactory {

	/**
     * Creates a YAML generator to write YAML text to a character stream.
     * The generator is configured with the factory configuration.
     *
     * @param writer i/o writer to which YAML is written
     * @return the created YAML generator
     */
    YamlGenerator createGenerator(Writer writer);

    /**
     * Creates a YAML generator to write YAML text to a byte stream. Characters 
     * written to the stream are encoded into bytes using UTF-8 encoding. 
     * The generator is configured with the factory's configuration.
     *
     * @param out i/o stream to which YAML is written
     * @return the created YAML generator
     */
    YamlGenerator createGenerator(OutputStream out);

    /**
     * Creates a YAML generator to write YAML text to a byte stream. Characters 
     * written to the stream are encoded into bytes using the specified charset. 
     * The generator is configured with the factory's configuration.
     *
     * @param out i/o stream to which YAML is written
     * @param charset a charset
     * @return the created YAML generator
     */
    YamlGenerator createGenerator(OutputStream out, Charset charset);

    /**
     * Returns a read-only map of supported provider specific configuration
     * properties that are used to configure the YAML generators.
     * If there are any specified configuration properties that are not
     * supported by the provider, they won't be part of the returned map.
     *
     * @return a map of supported provider specific properties that are used
     * to configure the created generators. The map may be empty but not null
     */
    Map<String, ?> getConfigInUse();
}
