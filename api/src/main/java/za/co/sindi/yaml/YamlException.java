/**
 * 
 */
package za.co.sindi.yaml;

/**
 * <code>YamlException</code> indicates that some exception happened during
 * YAML processing.
 * 
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public class YamlException extends RuntimeException {

	/**
	 * @param message
	 * @param cause
	 */
	public YamlException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public YamlException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public YamlException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
