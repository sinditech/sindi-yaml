/**
 * 
 */
package za.co.sindi.yaml.stream;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import za.co.sindi.commons.utils.IOUtils;
import za.co.sindi.yaml.YamlBoolean;
import za.co.sindi.yaml.YamlContext;
import za.co.sindi.yaml.YamlException;
import za.co.sindi.yaml.YamlMapping;
import za.co.sindi.yaml.YamlNode;
import za.co.sindi.yaml.YamlNull;
import za.co.sindi.yaml.YamlNumber;
import za.co.sindi.yaml.YamlScalar;
import za.co.sindi.yaml.YamlSequence;
import za.co.sindi.yaml.YamlString;
import za.co.sindi.yaml.node.ScalarResolver;
import za.co.sindi.yaml.syntax.AliasExpander;
import za.co.sindi.yaml.syntax.EventParser;
import za.co.sindi.yaml.syntax.Mark;
import za.co.sindi.yaml.syntax.RawEvent;
import za.co.sindi.yaml.syntax.Scanner;

/**
 * @author Buhake Sindi
 * @since 08 November 2026
 */
public class YamlParserImpl implements YamlParser {

    private enum Ctx { KEY, VALUE, SEQ }
    
    private record Item(Event event, String text, YamlScalar scalar, Mark mark, boolean merge) { }
	
    private final Deque<Ctx> STACK = new ArrayDeque<>();
	private final YamlContext yamlContext;
	private final Reader reader;
	private AliasExpander events;
	private Item pending;
    private Item current;
    private boolean finished;
    private boolean closed;

	/**
	 * @param reader
	 * @param yamlContext
	 */
	public YamlParserImpl(Reader reader, YamlContext yamlContext) {
		super();
		this.reader = Objects.requireNonNull(reader, "reader");;
		this.yamlContext = yamlContext;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		if (closed) return false;
        init();
        while (pending == null && !finished) {
            RawEvent raw = events.next();
            if (raw == null) { finished = true; break; }
            pending = translate(raw);
        }
        return pending != null;
	}

	@Override
	public Event next() {
		// TODO Auto-generated method stub
		if (!hasNext()) throw new NoSuchElementException("No more YAML events");
		current = pending;
        pending = null;
        return current.event();
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		Item c = cur();
        return switch (c.event()) {
            case KEY_NAME -> c.text();
            case VALUE_STRING -> ((YamlString) c.scalar()).value();
            case VALUE_NUMBER -> c.scalar().toString();
            default -> throw new IllegalStateException("getString() is not valid for " + c.event());
        };
	}

	@Override
	public boolean isIntegralNumber() {
		// TODO Auto-generated method stub
		return number().isIntegral();
	}

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return (int) number().value();
	}

	@Override
	public long getLong() {
		// TODO Auto-generated method stub
		return (long)number().value();
	}

	@Override
	public BigDecimal getBigDecimal() {
		// TODO Auto-generated method stub
		return (BigDecimal)number().value();
	}

	@Override
	public YamlLocation getLocation() {
		// TODO Auto-generated method stub
		return current == null ? YamlLocationImpl.UNKNOWN : current.mark();
	}

	@Override
	public YamlMapping getMapping() {
		// TODO Auto-generated method stub
		if (cur().event() != Event.START_MAPPING) throw new IllegalStateException("Not at START_MAPPING but " + cur().event());
        return readMapping();
	}

	@Override
	public YamlNode getValue() {
		// TODO Auto-generated method stub
		Item c = cur();
        return switch (c.event()) {
            case START_MAPPING -> readMapping();
            case START_SEQUENCE -> readSequence();
            case KEY_NAME -> new za.co.sindi.yaml.node.YamlString(c.text());
            case VALUE_STRING, VALUE_NUMBER, VALUE_TRUE, VALUE_FALSE, VALUE_NULL -> c.scalar();
            default -> throw new IllegalStateException("getValue() is not valid for " + c.event());
        };
	}

	@Override
	public YamlSequence getSequence() {
		// TODO Auto-generated method stub
		if (cur().event() != Event.START_SEQUENCE) throw new IllegalStateException("Not at START_SEQUENCE but " + cur().event());
        return readSequence();
	}

	@Override
	public Stream<YamlNode> getSequenceStream() {
		// TODO Auto-generated method stub
		return getSequence().stream();
	}

	@Override
	public Stream<Entry<String, YamlNode>> getMappingStream() {
		// TODO Auto-generated method stub
		return getMapping().entrySet().stream();
	}

	@Override
	public Stream<YamlNode> getNodeStream() {
		// TODO Auto-generated method stub
		return getMapping().values().stream();
	}

	@Override
	public void skipSequence() {
		// TODO Auto-generated method stub
		if (cur().event() == Event.START_SEQUENCE) skip();
	}

	@Override
	public void skipMapping() {
		// TODO Auto-generated method stub
		if (cur().event() == Event.START_MAPPING) skip();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
        try { 
        	reader.close();
        	closed = true;
        } catch (IOException e) { 
        	throw new YamlException("I/O error while closing", e); 
        }
	}

    private void init() {
        if (events != null) return;
        try {
            char[] input = IOUtils.toString(reader).toCharArray(); //Input.readAll(reader);
            events = new AliasExpander(new EventParser(new Scanner(input)), yamlContext.maxParsingCharactersLimit());
        } catch (IOException e) {
            throw new YamlException("I/O error while reading YAML", e);
        }
    }
    
    private Item item(Event e, RawEvent raw, String text, YamlScalar scalar, boolean merge) {
        return new Item(e, text, scalar, raw.start(), merge);
    }

    private Item translate(RawEvent e) {
        Ctx top = STACK.peek();
        switch (e.kind()) {
            case STREAM_START -> { return null; }
            case STREAM_END -> { finished = true; return null; }
            case DOCUMENT_START -> { return item(Event.START_DOCUMENT, e, null, null, false); }
            case DOCUMENT_END -> { return item(Event.END_DOCUMENT, e, null, null, false); }
            case ALIAS -> throw new IllegalStateException("Aliases must have been expanded");
            case SCALAR -> {
                if (top == Ctx.KEY) {
                    STACK.pop();
                    STACK.push(Ctx.VALUE);
                    boolean merge = e.plain() && e.tag() == null && "<<".equals(e.value());
                    return item(Event.KEY_NAME, e, e.value(), null, merge);
                }
                YamlScalar v;
                try {
                    v = ScalarResolver.resolve(e.value(), e.plain(), e.tag());
                } catch (IllegalArgumentException ex) {
                    throw new YamlParsingException(ex.getMessage(), e.start());
                }
                if (top == Ctx.VALUE) { STACK.pop(); STACK.push(Ctx.KEY); }
                Event ev = switch (v) {
                    case YamlString _ -> Event.VALUE_STRING;
                    case YamlNumber<?> _ -> Event.VALUE_NUMBER;
                    case YamlBoolean b -> b.value() ? Event.VALUE_TRUE : Event.VALUE_FALSE;
                    case YamlNull _ -> Event.VALUE_NULL;
                    default -> throw new YamlParsingException("Unknown YamlScalar event.", e.start());
                };
                return item(ev, e, null, v, false);
            }
            case MAPPING_START, SEQUENCE_START -> {
                if (top == Ctx.KEY) {
                    throw new YamlParsingException("Collections as mapping keys are not supported", e.start());
                }
                if (STACK.size() >= yamlContext.maxDepth()) {
                    throw new YamlParsingException("Maximum nesting depth of " + yamlContext.maxDepth() + " exceeded", e.start());
                }
                if (top == Ctx.VALUE) { STACK.pop(); STACK.push(Ctx.KEY); }
                boolean mapping = e.kind() == RawEvent.Kind.MAPPING_START;
                STACK.push(mapping ? Ctx.KEY : Ctx.SEQ);
                return item(mapping ? Event.START_MAPPING : Event.START_SEQUENCE, e, null, null, false);
            }
            case MAPPING_END -> { STACK.pop(); return item(Event.END_MAPPING, e, null, null, false); }
            case SEQUENCE_END -> { STACK.pop(); return item(Event.END_SEQUENCE, e, null, null, false); }
        }
        throw new IllegalStateException("Unhandled event " + e.kind());
    }

    // ------------------------------------------------------------------ accessors

    private Item cur() {
        if (current == null) throw new IllegalStateException("next() has not been called");
        return current;
    }

    @SuppressWarnings("unchecked")
	private <X extends Number> YamlNumber<X> number() {
        Item c = cur();
        if (c.event() != Event.VALUE_NUMBER) throw new IllegalStateException("Not at VALUE_NUMBER but " + c.event());
        return (YamlNumber<X>) c.scalar();
    }

    private YamlNode readValue() {
        Item c = cur();
        return switch (c.event()) {
            case START_MAPPING -> readMapping();
            case START_SEQUENCE -> readSequence();
            case VALUE_STRING, VALUE_NUMBER, VALUE_TRUE, VALUE_FALSE, VALUE_NULL -> c.scalar();
            default -> throw new YamlParsingException("Unexpected " + c.event(), c.mark());
        };
    }

    private YamlMapping readMapping() {
        Map<String, YamlNode> map = new LinkedHashMap<>();
        Set<String> explicit = new HashSet<>();
        while (next() != Event.END_MAPPING) {
            Item key = cur();
            next();
            YamlNode node = readValue();
            if (key.merge()) {
                merge(map, node, key.mark());
            } else {
                if (!yamlContext.allowDuplicateKeys() && !explicit.add(key.text())) {
                    throw new YamlParsingException("Duplicate mapping key '" + key.text() + "'", key.mark());
                }
                map.put(key.text(), node);
            }
        }
        return new za.co.sindi.yaml.node.YamlMapping(map);
    }

    /** Merge key semantics: entries already present (explicit keys, earlier merges) win. */
    private static void merge(Map<String, YamlNode> target, YamlNode source, Mark at) {
        if (source instanceof YamlMapping m) {
            for (Map.Entry<String, YamlNode> e : m.entrySet()) target.putIfAbsent(e.getKey(), e.getValue());
        } else if (source instanceof YamlSequence s) {
            for (YamlNode v : s) {
                if (!(v instanceof YamlMapping)) throw new YamlParsingException("A merge key sequence may only contain mappings", at);
                merge(target, v, at);
            }
        } else {
            throw new YamlParsingException("The value of a merge key '<<' must be a mapping or a sequence of mappings", at);
        }
    }

    private YamlSequence readSequence() {
        List<YamlNode> list = new ArrayList<>();
        while (next() != Event.END_SEQUENCE) list.add(readValue());
        return new za.co.sindi.yaml.node.YamlSequence(list);
    }

    private void skip() {
        int depth = 1;
        while (depth > 0) {
            switch (next()) {
                case START_MAPPING, START_SEQUENCE -> depth++;
                case END_MAPPING, END_SEQUENCE -> depth--;
                default -> { }
            }
        }
    }
}
