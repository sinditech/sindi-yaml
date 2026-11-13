/**
 * 
 */
package za.co.sindi.yaml.node;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public record YamlString(String value, ScalarStyle style) implements za.co.sindi.yaml.YamlString {

	enum ScalarStyle {
	    PLAIN,          // Unquoted: entry: value
	    SINGLE_QUOTED,  // Single quotes: entry: 'value'
	    DOUBLE_QUOTED,  // Double quotes with escapes: entry: "value\n"
	    LITERAL,        // Multi-line literal: entry: |
	    FOLDED          // Multi-line folded: entry: >
	}
	
	public YamlString {
		value = value == null ? "" : value;
		style = style == null ? ScalarStyle.PLAIN : style;
	}
	
	public YamlString(String value) {
		this(value, ScalarStyle.PLAIN);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String val = value();
        switch (style) {
            case SINGLE_QUOTED:
                return "'" + val.replace("'", "''") + "'";
            case DOUBLE_QUOTED:
                return "\"" + val.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
            case LITERAL:
                return "|\n  " + val.replace("\n", "\n  ");
            case FOLDED:
                return ">\n  " + val.replace("\n", "\n  ");
            case PLAIN:
            default:
                return val;
        }
	}
}
