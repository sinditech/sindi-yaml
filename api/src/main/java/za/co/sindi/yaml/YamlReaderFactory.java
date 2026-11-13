/**
 * 
 */
package za.co.sindi.yaml;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * Factory to create {@link YamlReader} instances. If a factory
 * instance is configured with some configuration, that would be
 * used to configure the created reader instances.
 *
 * <p>
 * {@link YamlReader} can also be created using {@link Yaml}'s
 * {@code createReader} methods. If multiple reader instances are created,
 * then creating them using a reader factory is preferred.
 *
 * <p>
 * <b>For example:</b>
 * <pre>
 * <code>
 * YamlReaderFactory factory = Yaml.createReaderFactory(...);
 * YamlReader reader1 = factory.createReader(...);
 * YamlReader reader2 = factory.createReader(...);
 * </code>
 * </pre>
 *
 * <p> All the methods in this class are safe for use by multiple concurrent
 * threads.
 * 
 * @author Buhake Sindi
 * @since 10 October 2026
 */
public interface YamlReaderFactory {

    /**
     * Creates a YAML reader from a character stream. The reader is configured
     * with the factory configuration.
     *
     * @param reader a reader from which YAML is to be read
     * @return a YAML reader
     */
    YamlReader createReader(Reader reader);

    /**
     * Creates a YAML reader from a byte stream. The character encoding of
     * the stream is determined as described in
     * <a href="http://tools.ietf.org/rfc/rfc7159.txt">RFC 7159</a>.
     * The reader is configured with the factory configuration.
     *
     * @param in a byte stream from which YAML is to be read
     * @return a YAML reader
     */
    YamlReader createReader(InputStream in);

    /**
     * Creates a YAML reader from a byte stream. The bytes of the stream
     * are decoded to characters using the specified charset. The reader is
     * configured with the factory configuration.
     *
     * @param in a byte stream from which YAML is to be read
     * @param charset a charset
     * @return a YAML reader
     */
    YamlReader createReader(InputStream in, Charset charset);
}
