/**
 * 
 */
package za.co.sindi.yaml;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public sealed interface YamlNode permits YamlStructure, YamlScalar {

	public enum Kind { NULL, SCALAR, SEQUENCE, MAPPING }

	/**
     * Returns the kind of this YAML node.
     *
     * @return YAML kind
     */
	Kind getKind();
	
    /**
     * Returns YAML text for this YAML node.
     *
     * @return YAML text
     */
    @Override
    String toString();
}
