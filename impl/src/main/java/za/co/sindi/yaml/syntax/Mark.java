package za.co.sindi.yaml.syntax;

import za.co.sindi.yaml.stream.YamlLocation;

/** A 0-based position in the input; exposed through the 1-based {@link YamlLocation} contract. */
public record Mark(int index, int line, int column) implements YamlLocation {
    @Override public long getLineNumber() { return line + 1L; }
    @Override public long getColumnNumber() { return column + 1L; }
    @Override public long getStreamOffset() { return index; }
}
