package za.co.sindi.yaml.syntax;

/** A node-level parse event (the YAML 1.2 "serialization event" model) before scalar resolution. */
public record RawEvent(Kind kind, String anchor, String tag, String value, boolean plain, char style,
                       Mark start, Mark end) {

    public enum Kind {
        STREAM_START, STREAM_END, DOCUMENT_START, DOCUMENT_END,
        ALIAS, SCALAR, SEQUENCE_START, SEQUENCE_END, MAPPING_START, MAPPING_END
    }

    public static RawEvent simple(Kind kind, Mark start, Mark end) {
        return new RawEvent(kind, null, null, null, false, '\0', start, end);
    }

    public RawEvent withoutAnchor() {
        return anchor == null ? this : new RawEvent(kind, null, tag, value, plain, style, start, end);
    }
}
