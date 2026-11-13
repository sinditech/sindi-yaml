/**
 * 
 */
package za.co.sindi.yaml;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public sealed interface YamlStructure extends YamlNode permits YamlMapping, YamlSequence {

	/**
     * Resolves an RFC 6901 style pointer ({@code /server/ports/0}) against this structure.
     * The empty pointer designates the structure itself.
     *
     * @throws NoSuchElementException if nothing exists at the pointer
     * @throws IllegalArgumentException if the pointer is malformed
     */
    default YamlNode getValue(String pointer) {
        Objects.requireNonNull(pointer, "pointer");
        if (pointer.isEmpty()) return this;
        if (pointer.charAt(0) != '/') {
            throw new IllegalArgumentException("A YAML pointer must be empty or start with '/': " + pointer);
        }
        YamlNode current = this;
        for (String raw : pointer.substring(1).split("/", -1)) {
            String token = raw.replace("~1", "/").replace("~0", "~");
            if (current instanceof YamlMapping m && m.containsKey(token)) {
                current = m.get(token);
            } else if (current instanceof YamlSequence s) {
                int index;
                try { index = Integer.parseInt(token); }
                catch (NumberFormatException e) { throw new NoSuchElementException("No value at '" + pointer + "'"); }
                if (index < 0 || index >= s.size()) throw new NoSuchElementException("No value at '" + pointer + "'");
                current = s.get(index);
            } else {
                throw new NoSuchElementException("No value at '" + pointer + "'");
            }
        }
        return current;
    }

}
