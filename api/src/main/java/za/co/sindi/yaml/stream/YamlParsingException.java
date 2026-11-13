/**
 * 
 */
package za.co.sindi.yaml.stream;

import za.co.sindi.yaml.YamlException;

/**
 * @author Buhake Sindi
 * @since 09 November 2026
 */
public class YamlParsingException extends YamlException {

	/**
     * The location of the incorrect YAML.
     */
    private final YamlLocation location;

	/**
	 * @param message
	 * @param location
	 */
	public YamlParsingException(String message, YamlLocation location) {
		super(message);
		this.location = location;
	}

	/**
	 * @param message
	 * @param cause
	 * @param location
	 */
	public YamlParsingException(String message, Throwable cause, YamlLocation location) {
		super(message, cause);
		this.location = location;
	}

    /**
     * Return the location of the incorrect YAML.
     *
     * @return the non-null location of the incorrect YAML
     */
    public YamlLocation getLocation() {
        return location;
    }
}
