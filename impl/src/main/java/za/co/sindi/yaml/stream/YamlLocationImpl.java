/**
 * 
 */
package za.co.sindi.yaml.stream;

/**
 * @author Buhake Sindi
 * @since 09 November 2026
 */
public class YamlLocationImpl implements YamlLocation {
	
	public static final YamlLocation UNKNOWN = new YamlLocationImpl(-1, -1, -1);
	
	private final long lineNumber;
	private final long columnNumber;
	private final long streamOffset;

	/**
	 * @param lineNumber
	 * @param columnNumber
	 * @param streamOffset
	 */
	public YamlLocationImpl(long lineNumber, long columnNumber, long streamOffset) {
		super();
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
		this.streamOffset = streamOffset;
	}

	@Override
	public long getLineNumber() {
		// TODO Auto-generated method stub
		return lineNumber;
	}

	@Override
	public long getColumnNumber() {
		// TODO Auto-generated method stub
		return columnNumber;
	}

	@Override
	public long getStreamOffset() {
		// TODO Auto-generated method stub
		return streamOffset;
	}
}
