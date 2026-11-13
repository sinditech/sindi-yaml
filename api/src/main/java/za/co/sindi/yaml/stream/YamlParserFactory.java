/**
 * 
 */
package za.co.sindi.yaml.stream;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * 
 * @author Buhake Sindi
 * @since 10 October 2026
 */
public interface YamlParserFactory {

	/**
     * Creates a YAML parser from a character stream.
     *
     * @param reader a I/O reader from which YAML is to be read
     * @return the created YAML parser
     */
    YamlParser createParser(Reader reader);

    /**
     * Creates a YAML parser from the specified byte stream.
     *
     * @param in I/O stream from which YAML is to be read
     * @return the created YAML parser
     * @throws jakarta.json.JsonException if encoding cannot be determined
     *         or I/O error (IOException would be cause of YamlException)
     */
    YamlParser createParser(InputStream in);

    /**
     * Creates a YAML parser from the specified byte stream.
     * The bytes of the stream are decoded to characters using the
     * specified charset.
     *
     * @param in I/O stream from which YAML is to be read
     * @param charset a charset
     * @return the created YAML parser
     */
    YamlParser createParser(InputStream in, Charset charset);
}
