package za.co.sindi.yaml.syntax;

/** A lexical token produced by the {@link Scanner}. */
public record Token(Kind kind, String value, boolean plain, char style, Mark start, Mark end) {

    public enum Kind {
        STREAM_START, STREAM_END, DIRECTIVE, DOCUMENT_START, DOCUMENT_END,
        BLOCK_SEQUENCE_START, BLOCK_MAPPING_START, BLOCK_END,
        FLOW_SEQUENCE_START, FLOW_SEQUENCE_END, FLOW_MAPPING_START, FLOW_MAPPING_END,
        BLOCK_ENTRY, FLOW_ENTRY, KEY, VALUE, ALIAS, ANCHOR, TAG, SCALAR
    }

    static Token of(Kind kind, Mark start, Mark end) { return new Token(kind, null, false, '\0', start, end); }
    static Token named(Kind kind, String value, Mark start, Mark end) { return new Token(kind, value, false, '\0', start, end); }
}
