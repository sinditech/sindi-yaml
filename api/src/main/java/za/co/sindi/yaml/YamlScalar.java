/**
 * 
 */
package za.co.sindi.yaml;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public sealed interface YamlScalar extends YamlNode permits YamlNull, YamlString, YamlNumber, YamlBoolean {

	@Override
	default Kind getKind() {
		// TODO Auto-generated method stub
		return Kind.SCALAR;
	}

}
