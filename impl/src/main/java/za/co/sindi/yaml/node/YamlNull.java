/**
 * 
 */
package za.co.sindi.yaml.node;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public record YamlNull(/*Type type*/) implements za.co.sindi.yaml.YamlNull {
	
//	public YamlNull {
//		type = Objects.requireNonNull(type, "A YAML Null type is required.");
//	}
//	
//	public YamlNull() {
//		this(Type.EXPLICIT_NULL);
//	}

//	@Override
//	public Type getType() {
//		// TODO Auto-generated method stub
//		return type;
//	}
	
	public String toString(final Type nullType) {
		// TODO Auto-generated method stub
		return nullType == Type.EXPLICIT_NULL ? "null" : nullType == Type.TILDE ? "~" : "";
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return toString(Type.EXPLICIT_NULL);
	}
}
