/**
 * 
 */
package za.co.sindi.yaml;

import java.io.Closeable;

import za.co.sindi.yaml.stream.YamlParsingException;

/**
 * @author Buhake Sindi
 * @since 10 October 2026
 */
public interface YamlReader extends Closeable {

	/**
     * Returns a YAML sequence or mapping that is represented in
     * the input source. This method needs to be called
     * only once for a reader instance.
     *
     * @return a YAML mapping or sequence
     * @throws YamlException if a YAML mapping or sequence cannot
     *     be created due to I/O error (IOException would be
     * cause of YamlException)
     * @throws jakarta.yaml.stream.YamlParsingException if a YAML mapping or sequence
     *     cannot be created due to incorrect representation
     * @throws IllegalStateException if read, readMapping, readSequence,
     *     readNode or close method is already called
     */
    YamlNode read();

    /**
     * Returns a YAML mapping that is represented in
     * the input source. This method needs to be called
     * only once for a reader instance.
     *
     * @return a YAML mapping
     * @throws YamlException if a YAML mapping cannot
     *     be created due to I/O error (IOException would be
     *     cause of YamlException)
     * @throws jakarta.yaml.stream.YamlParsingException if a YAML mapping cannot
     *     be created due to incorrect representation
     * @throws IllegalStateException if read, readMapping, readSequence,
     *     readNode or close method is already called
     */
    YamlMapping readMapping();

    /**
     * Returns a YAML sequence that is represented in
     * the input source. This method needs to be called
     * only once for a reader instance.
     *
     * @return a YAML sequence
     * @throws YamlException if a YAML sequence cannot
     *     be created due to I/O error (IOException would be
     *     cause of YamlException)
     * @throws jakarta.yaml.stream.YamlParsingException if a YAML sequence cannot
     *     be created due to incorrect representation
     * @throws IllegalStateException if read, readMapping, readSequence,
     *     readNode or close method is already called
     */
    YamlSequence readSequence();

//    /**
//     * Returns a YAML value that is represented in
//     * the input source. This method needs to be called
//     * only once for a reader instance.
//     *
//     * @return a YAML node
//     * @throws YamlException if a YAML value
//     *     be created due to I/O error (IOException would be
//     *     cause of YamlException)
//     * @throws YamlParsingException if a YAML value
//     *     cannot be created due to incorrect representation
//     * @throws IllegalStateException if read, readMapping, readSequence,
//     *     readNode or close method is already called
//     *
//     * @since 1.1
//     */
//    default YamlNode readNode() {
//        throw new UnsupportedOperationException();
//    }

    /**
     * Closes this reader and frees any resources associated with the
     * reader. This method closes the underlying input source.
     *
     * @throws YamlException if an I/O error occurs (IOException would be
     * cause of YamlException)
     */
    @Override
    void close();
}
