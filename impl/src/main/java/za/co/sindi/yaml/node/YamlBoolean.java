/**
 * 
 */
package za.co.sindi.yaml.node;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public record YamlBoolean(boolean value) implements za.co.sindi.yaml.YamlBoolean {

//	private static final Set<String> TRUE = Set.of("true", "yes", "on");
//	private static final Set<String> FALSE = Set.of("false", "no", "off");
	
	public YamlBoolean(String value) {
		String trimmedValue = value.trim().toLowerCase();
//		boolean bool = TRUE.contains(trimmedValue);
//		if (!bool) { 
//			bool = FALSE.contains(trimmedValue);
//			if (bool) bool = !bool;
//		}
		
		this(Boolean.valueOf(trimmedValue));
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Boolean.toString(value);
	}
}
