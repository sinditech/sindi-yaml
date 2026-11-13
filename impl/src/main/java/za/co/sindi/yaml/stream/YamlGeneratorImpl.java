/**
 * 
 */
package za.co.sindi.yaml.stream;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import za.co.sindi.yaml.YamlBoolean;
import za.co.sindi.yaml.YamlContext;
import za.co.sindi.yaml.YamlException;
import za.co.sindi.yaml.YamlMapping;
import za.co.sindi.yaml.YamlNode;
import za.co.sindi.yaml.YamlNull;
import za.co.sindi.yaml.YamlNull.Type;
import za.co.sindi.yaml.YamlNumber;
import za.co.sindi.yaml.YamlSequence;
import za.co.sindi.yaml.YamlString;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public class YamlGeneratorImpl implements YamlGenerator {
	
	private static final class Frame {
        final boolean mapping;
        final boolean flow;
        final int indent;          // column of this collection's entries (block style)
        final boolean inlineFirst; // first entry continues the parent's "- " line
        int count;
        boolean keyPending;

        Frame(boolean mapping, boolean flow, int indent, boolean inlineFirst) {
            this.mapping = mapping; this.flow = flow; this.indent = indent; this.inlineFirst = inlineFirst;
        }
    }

	private final Writer out;
    private final YamlContext yamlContext;
    private final Deque<Frame> stack = new ArrayDeque<>();
    private boolean atLineStart = true;
    private int documents;
    private boolean closed;
    
	/**
	 * @param out
	 * @param yamlContext
	 */
	public YamlGeneratorImpl(Writer out, YamlContext yamlContext) {
		super();
		this.out = out;
		this.yamlContext = yamlContext;
	}

	@Override
	public YamlGenerator writeStartMapping() {
		// TODO Auto-generated method stub
		return start(true);
	}

	@Override
	public YamlGenerator writeStartMapping(String name) {
		// TODO Auto-generated method stub
		writeKey(name);
		return writeStartMapping();
	}

	@Override
	public YamlGenerator writeKey(String name) {
		// TODO Auto-generated method stub
		checkOpen();
        if (name == null) throw new NullPointerException("name");
        Frame f = stack.peek();
        if (f == null || !f.mapping) throw state("A key can only be written inside a mapping");
        if (f.keyPending) throw state("The previous key has no value");
        if (f.flow) {
            if (f.count > 0) write0(", ");
        } else if (f.count == 0 && f.inlineFirst) {
            write0(" ");
        } else {
            newline();
            indent(f.indent);
        }
        write0(ScalarStyler.key(name, f.flow) + ":");
        f.count++;
        f.keyPending = true;
        return this;
	}

	@Override
	public YamlGenerator writeStartSequence() {
		// TODO Auto-generated method stub
		return start(false);
	}

	@Override
	public YamlGenerator writeStartSequence(String name) {
		// TODO Auto-generated method stub
		writeKey(name);
		return writeStartSequence();
	}

	@Override
	public YamlGenerator write(String name, YamlNode value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YamlGenerator write(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YamlGenerator write(String name, BigInteger value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YamlGenerator write(String name, BigDecimal value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YamlGenerator write(String name, int value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YamlGenerator write(String name, long value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YamlGenerator write(String name, double value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YamlGenerator write(String name, boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YamlGenerator writeNull(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public YamlGenerator writeEnd() {
		// TODO Auto-generated method stub
		checkOpen();
        Frame f = stack.peek();
        if (f == null) throw state("There is no open mapping or sequence to end");
        if (f.mapping && f.keyPending) throw state("The last key has no value");
        stack.pop();
        if (f.flow) write0(f.mapping ? "}" : "]");
        else if (f.count == 0) write0((stack.isEmpty() ? "" : " ") + (f.mapping ? "{}" : "[]"));
        endDocumentIfRoot();
        return this;
	}

	@Override
	public YamlGenerator write(YamlNode value) {
		// TODO Auto-generated method stub
		if (value == null) return writeNull();
        switch (value) {
            case YamlNull _ -> writeNull();
            case YamlBoolean _ -> scalarText(value.toString());
            case YamlNumber<?> _ -> scalarText(value.toString());
            case YamlString s -> emitString(s.value());
            case YamlMapping mapping -> {
                start(true);
                for (Map.Entry<String, YamlNode> e : mapping.entrySet()) {
                    writeKey(e.getKey());
                    write(e.getValue());
                }
                writeEnd();
            }
            case YamlSequence sequence -> {
                start(false);
                for (YamlNode node : sequence) write(node);
                writeEnd();
            }
        }
        return this;
	}

	@Override
	public YamlGenerator write(String value) {
		// TODO Auto-generated method stub
		if (value == null) return writeNull();
		emitString(value);
		return this;
	}

	@Override
	public YamlGenerator write(BigDecimal value) {
		// TODO Auto-generated method stub
		if (value == null) return writeNull();
		scalarText(value.toString());
		return this;
	}

	@Override
	public YamlGenerator write(BigInteger value) {
		// TODO Auto-generated method stub
		if (value == null) return writeNull();
		scalarText(value.toString());
		return this;
	}

	@Override
	public YamlGenerator write(int value) {
		// TODO Auto-generated method stub
		scalarText(Integer.toString(value));
		return this;
	}

	@Override
	public YamlGenerator write(long value) {
		// TODO Auto-generated method stub
		scalarText(Long.toString(value));
		return this;
	}

	@Override
	public YamlGenerator write(double value) {
		// TODO Auto-generated method stub
		scalarText(Double.toString(value));
		return this;
	}

	@Override
	public YamlGenerator write(boolean value) {
		// TODO Auto-generated method stub
		scalarText(Boolean.toString(value));
		return this;
	}

	@Override
	public YamlGenerator writeNull() {
		// TODO Auto-generated method stub
		if (yamlContext.outputNullType() == Type.EXPLICIT_NULL)
			scalarText("null");
		else if (yamlContext.outputNullType() == Type.TILDE)
			scalarText("~");
		else scalarText("");
		
		return this;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (closed) return;
        closed = true;
        try {
            if (!stack.isEmpty()) throw state("Generator closed while a mapping or sequence is still open");
            out.flush();
        } catch (IOException e) {
            throw new YamlException("I/O error while closing", e);
        } finally {
            try { out.close(); } catch (IOException ignored) { /* nothing sensible to do */ }
        }
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		try { 
			out.flush(); 
		} catch (IOException e) { 
			throw new YamlException("I/O error while flushing", e); 
		}
	}
	
    // ------------------------------------------------------------------ low level output

    private void write0(String s) {
        if (s.isEmpty()) return;
        try { 
        	out.write(s); 
        } catch (IOException e) { 
        	throw new YamlException("I/O error while writing YAML", e); 
        }
        atLineStart = s.charAt(s.length() - 1) == '\n';
    }

    private void newline() { if (!atLineStart) write0("\n"); }

    private void indent(int n) { if (n > 0) write0(" ".repeat(n)); }

    private YamlGenerationException state(String message) { return new YamlGenerationException(message); }

    private void checkOpen() { if (closed) throw new IllegalStateException("Generator is closed"); }

    // ------------------------------------------------------------------ structure

    /** Positions the output for a new value inside the current frame and returns that frame (null at root). */
    private Frame beginValue() {
        checkOpen();
        Frame f = stack.peek();
        if (f == null) {
            if (documents > 0 || yamlContext.explicitStart()) {
                newline();
                write0("---\n");
            }
            documents++;
            return null;
        }
        if (f.mapping) {
            if (!f.keyPending) throw state("A key must be written before a value inside a mapping");
            f.keyPending = false;
        } else {
            if (f.flow) {
                if (f.count > 0) write0(", ");
            } else {
                if (f.count == 0 && f.inlineFirst) write0(" ");
                else { newline(); indent(f.indent); }
                write0("-");
            }
            f.count++;
        }
        return f;
    }

    private static String separator(Frame parent) {
        return parent == null || (!parent.mapping && parent.flow) ? "" : " ";
    }

    private void endDocumentIfRoot() { if (stack.isEmpty()) newline(); }

    private void scalarText(String text) {
        Frame f = beginValue();
        write0(separator(f) + text);
        endDocumentIfRoot();
    }

    private void emitString(String s) {
        Frame top = stack.peek();
        boolean flow = top != null ? top.flow : yamlContext.flow();
        ScalarStyler.Style style = ScalarStyler.choose(s, flow, !flow);
        if (style == ScalarStyler.Style.LITERAL) literal(s);
        else scalarText(ScalarStyler.render(s, style));
    }

    private void literal(String s) {
        Frame f = beginValue();
        int trailing = 0;
        while (trailing < s.length() && s.charAt(s.length() - 1 - trailing) == '\n') trailing++;
        String body = s.substring(0, s.length() - trailing);
        String chomp = trailing == 0 ? "-" : trailing == 1 ? "" : "+";
        int contentIndent = (f == null ? 0 : f.indent) + yamlContext.indent();
        write0(separator(f) + "|" + chomp);
        for (String line : body.split("\n", -1)) {
            write0("\n");
            if (!line.isEmpty()) { indent(contentIndent); write0(line); }
        }
        if (trailing > 1) write0("\n".repeat(trailing));
        endDocumentIfRoot();
    }

    private YamlGenerator start(boolean mapping) {
        Frame parent = beginValue();
        boolean flow = yamlContext.flow() || (parent != null && parent.flow);
        if (flow) write0(separator(parent) + (mapping ? "{" : "["));
        int childIndent;
        if (parent == null || flow) childIndent = 0;
        else if (parent.mapping) childIndent = parent.indent + (mapping || yamlContext.indentSequences() ? yamlContext.indent() : 0);
        else childIndent = parent.indent + 2;
        boolean inline = parent != null && !parent.mapping && !parent.flow;
        stack.push(new Frame(mapping, flow, childIndent, inline));
        return this;
    }
}
