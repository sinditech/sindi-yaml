/**
 * 
 */
package za.co.sindi.yaml;

import java.io.Closeable;

/**
 * @author Buhake Sindi
 * @since 10 October 2026
 */
public interface YamlWriter extends Closeable {

	/**
     * Writes the specified YAML {@link YamlSequence sequence} to the output
     * source. This method needs to be called only once for a writer instance.
     *
     * @param sequence YAML sequence that is to be written to the output source
     * @throws YamlException if the specified YAML mapping cannot be
     *     written due to i/o error (IOException would be cause of
     *     YamlException)
     * @throws IllegalStateException if writeSequence, writeMapping, write or close
     *     method is already called
     */
    void writeSequence(YamlSequence sequence);

    /**
     * Writes the specified YAML {@link YamlMapping mapping} to the output
     * source. This method needs to be called only once for a writer instance.
     *
     * @param mapping YAML mapping that is to be written to the output source
     * @throws YamlException if the specified YAML mapping cannot be
     *     written due to i/o error (IOException would be cause of YamlException)
     * @throws IllegalStateException if writeSequence, writeMapping, write or close
     *     method is already called
     */
    void writeMapping(YamlMapping mapping);

    /**
     * Closes this YAML writer and frees any resources associated with the
     * writer. This method closes the underlying output source.
     *
     * @throws YamlException if an i/o error occurs (IOException would be
     * cause of YamlException)
     */
    @Override
    void close();
}
