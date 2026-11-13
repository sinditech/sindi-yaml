/**
 * 
 */
package za.co.sindi.yaml.stream;

/**
 * A position in a YAML stream. Line and column numbers are 1-based; unknown values are -1. 
 * 
 * @author Buhake Sindi
 * @since 09 November 2026
 */
public interface YamlLocation {

	/**
     * Return the line number (starts with 1 for the first line) for the current YAML event in the input source.
     *
     * @return the line number (starts with 1 for the first line) or -1 if none is available
     */
    long getLineNumber();

    /**
     * Return the column number (starts with 1 for the first column) for the current YAML event in the input source.
     *
     * @return the column number (starts with 1 for the first column) or -1 if none is available
     */
    long getColumnNumber();

    /**
     * Return the stream offset into the input source this location
     * is pointing to. If the input source is a file or a byte stream then
     * this is the byte offset into that stream, but if the input source is
     * a character media then the offset is the character offset.
     * Returns -1 if there is no offset available.
     *
     * @return the offset of input source stream, or -1 if there is
     * no offset available
     */
    long getStreamOffset();
}
