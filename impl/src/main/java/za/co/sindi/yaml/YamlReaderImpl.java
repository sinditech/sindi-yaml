/**
 * 
 */
package za.co.sindi.yaml;

import java.io.Reader;

import za.co.sindi.yaml.stream.YamlParser.Event;
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
	public YamlStructure read() {
		// TODO Auto-generated method stub
		ensureNotRead();
		readDone = true;
		try {
            Event root = advanceToRoot();
            return switch (root) {
                case START_MAPPING -> parser.getMapping();
                case START_SEQUENCE -> parser.getSequence();
                default -> throw new YamlParsingException(
                        "Expected a YAML mapping, sequence at the document root but got " + root,
                        parser.getLocation());
            };
        } catch (IllegalStateException ise) {
            throw new YamlParsingException(ise.getMessage(), ise, parser.getLocation());
        }
	}

	@Override
	public YamlMapping readMapping() {
		// TODO Auto-generated method stub
		ensureNotRead();
		readDone = true;
		try {
            Event root = advanceToRoot();
            if (root != Event.START_MAPPING) {
                throw new YamlParsingException(
                        "Expected START_MAPPING at the document root but got " + root,
                        parser.getLocation());
            }
            return parser.getMapping();
        } catch (IllegalStateException ise) {
            throw new YamlParsingException(ise.getMessage(), ise, parser.getLocation());
        }
	}

	@Override
	public YamlSequence readSequence() {
		// TODO Auto-generated method stub
		ensureNotRead();
		readDone = true;
		try {
            Event root = advanceToRoot();
            if (root != Event.START_SEQUENCE) {
                throw new YamlParsingException(
                        "Expected START_SEQUENCE at the document root but got " + root,
                        parser.getLocation());
            }
            return parser.getSequence();
        } catch (IllegalStateException ise) {
            throw new YamlParsingException(ise.getMessage(), ise, parser.getLocation());
        }
	}

	@Override
	public YamlNode readNode() {
		// TODO Auto-generated method stub
		ensureNotRead();
		readDone = true;
		try {
            Event root = advanceToRoot();
            return switch (root) {
                case START_MAPPING -> parser.getMapping();
                case START_SEQUENCE -> parser.getSequence();
                case VALUE_STRING, VALUE_NUMBER, VALUE_TRUE, VALUE_FALSE, VALUE_NULL -> parser.getValue();
                default -> throw new YamlParsingException(
                        "Expected a YAML mapping, sequence or scalar at the document root but got " + root,
                        parser.getLocation());
            };
        } catch (IllegalStateException ise) {
            throw new YamlParsingException(ise.getMessage(), ise, parser.getLocation());
        }
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		parser.close();
		readDone = true;
	}
	
	private void ensureNotRead() {
		if (readDone) {
			throw new IllegalStateException("Reader read already called.");
		}
	}
	
	/**
     * Advances the parser past document framing events until the root value
     * of the first document is current ({@code START_MAPPING},
     * {@code START_SEQUENCE}, or a scalar value event).
     *
     * @return the event at the root of the first document
     */
    private Event advanceToRoot() {
        while (parser.hasNext()) {
            Event e = parser.next();
            switch (e) {
                case START_DOCUMENT, END_DOCUMENT -> {
                    // Multi-doc streams: keep scanning until a real root appears.
                    // END_DOCUMENT of an empty doc is ignored the same way.
                }
                case START_MAPPING, START_SEQUENCE,
                        VALUE_STRING, VALUE_NUMBER, VALUE_TRUE, VALUE_FALSE, VALUE_NULL -> {
                    return e;
                }
                default -> throw new YamlParsingException(
                        "Unexpected event while locating document root: " + e,
                        parser.getLocation());
            }
        }
        throw new YamlParsingException("No YAML document found in the input", parser.getLocation());
    }
}
