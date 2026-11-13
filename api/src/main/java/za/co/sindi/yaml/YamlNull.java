/**
 * 
 */
package za.co.sindi.yaml;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public non-sealed interface YamlNull extends YamlScalar {

	enum Type  { EXPLICIT_NULL, TILDE, EMPTY_VALUE }
}
