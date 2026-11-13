/**
 * 
 */
package za.co.sindi.yaml;

import java.io.Reader;

import za.co.sindi.yaml.stream.YamlParser;
import za.co.sindi.yaml.stream.YamlParserImpl;
import za.co.sindi.yaml.stream.YamlParsingException;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public class YamlReaderImpl implements YamlReader {
	
	private final YamlParserImpl parser;
    private boolean readDone;

	/**
	 * @param parser
	 */
	public YamlReaderImpl(final Reader reader, final YamlContext yamlContext) {
		super();
		this.parser = new YamlParserImpl(reader, yamlContext);
	}

	@Override
	public YamlNode read() {
		// TODO Auto-generated method stub
		ensureRead();
		readDone = true;
        if (parser.hasNext()) {
            try {
            	YamlParser.Event e = parser.next();
                if (e == YamlParser.Event.START_SEQUENCE) {
                    return parser.getSequence();
                } else if (e == YamlParser.Event.START_MAPPING) {
                    return parser.getMapping();
                }
            } catch (IllegalStateException ise) {
                throw new YamlParsingException(ise.getMessage(), ise, parser.getLocation());
            }
        }
        throw new YamlException("Internal error.");
	}

	@Override
	public YamlMapping readMapping() {
		// TODO Auto-generated method stub
		ensureRead();
		readDone = true;
        if (parser.hasNext()) {
            try {
            	parser.next();
                return parser.getMapping();
            } catch (IllegalStateException ise) {
                throw new YamlParsingException(ise.getMessage(), ise, parser.getLocation());
            }
        }
        throw new YamlException("Internal error.");
	}

	@Override
	public YamlSequence readSequence() {
		// TODO Auto-generated method stub
		ensureRead();
		readDone = true;
        if (parser.hasNext()) {
            try {
            	parser.next();
                return parser.getSequence();
            } catch (IllegalStateException ise) {
                throw new YamlParsingException(ise.getMessage(), ise, parser.getLocation());
            }
        }
        throw new YamlException("Internal error.");
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		parser.close();
		readDone = true;
	}
	
	private void ensureRead() {
		if (readDone) {
			throw new IllegalStateException("Reader read already called.");
		}
	}
}
