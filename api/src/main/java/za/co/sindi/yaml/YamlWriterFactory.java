/**
 * 
 */
package za.co.sindi.yaml;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Buhake Sindi
 * @since 10 October 2026
 */
public interface YamlWriterFactory {

	/**
     * Creates a YAML writer to write a YAML {@link YamlMapping mapping} or
     * {@link YamlSequence sequence} structure to the specified character stream.
     * The writer is configured with the factory configuration.
     *
     * @param writer to which YAML mapping or sequence is written
     * @return a YAML writer
     */
    YamlWriter createWriter(Writer writer);

    /**
     * Creates a YAML writer to write a YAML {@link YamlMapping mapping} or
     * {@link YamlSequence sequence} structure to the specified byte stream.
     * Characters written to the stream are encoded into bytes using UTF-8
     * encoding. The writer is configured with the factory configuration.
     *
     * @param out to which YAML mapping or sequence is written
     * @return a YAML writer
     */
    YamlWriter createWriter(OutputStream out);

    /**
     * Creates a YAML writer to write a YAML {@link YamlMapping mapping} or
     * {@link YamlSequence sequence} structure to the specified byte stream.
     * Characters written to the stream are encoded into bytes using the
     * specified charset. The writer is configured with the factory
     * configuration.
     *
     * @param out to which YAML mapping or sequence is written
     * @param charset a charset
     * @return a YAML writer
     */
    YamlWriter createWriter(OutputStream out, Charset charset);

    /**
     * Returns read-only map of supported provider specific configuration
     * properties that are used to configure the created YAML writer objects.
     * If there are any specified configuration properties that are not
     * supported by the provider, they won't be part of the returned map.
     *
     * @return a map of supported provider specific properties that are used
     * to configure the created writers. The map may be empty but not null.
     */
    Map<String, ?> getConfigInUse();
}
