package za.co.sindi.yaml.syntax;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.co.sindi.yaml.stream.YamlParsingException;

/**
 * Decorator over {@link EventParser} (stage 3) that records anchored nodes and replaces every alias
 * event by a replay of the recorded events, so downstream stages never see anchors or aliases.
 * Recording happens <em>after</em> expansion, so aliases inside anchored nodes expand correctly.
 * The number of replayed events is capped to defuse "billion laughs" style documents.
 */
public final class AliasExpander {

    private static final class Recording {
        final String name;
        final List<RawEvent> events = new ArrayList<>();
        int depth;
        Recording(String name) { this.name = name; }
    }

    private final EventParser source;
    private final long maxExpanded;
    private final Map<String, List<RawEvent>> anchors = new HashMap<>();
    private final Deque<Recording> recordings = new ArrayDeque<>();
    private final Deque<RawEvent> replay = new ArrayDeque<>();
    private long expanded;

    public AliasExpander(EventParser source, long maxExpanded) {
        this.source = source;
        this.maxExpanded = maxExpanded;
    }

    /** @return the next alias-free event, or {@code null} after STREAM_END */
    public RawEvent next() {
        for (;;) {
            boolean replayed = !replay.isEmpty();
            RawEvent e = replayed ? replay.poll() : source.next();
            if (e == null) return null;
            switch (e.kind()) {
                case DOCUMENT_START -> { anchors.clear(); recordings.clear(); }
                case ALIAS -> { expand(e); continue; }
                default -> { }
            }
            if (e.anchor() != null && !replayed) {
                Recording r = new Recording(e.anchor());
                recordings.push(r);
            }
            RawEvent clean = e.withoutAnchor();
            record(clean, e.anchor() != null && !replayed);
            return clean;
        }
    }

    private void record(RawEvent e, boolean startsRecording) {
        // Newest recording is first in the deque; iterate over a snapshot since we may remove entries.
        for (Recording r : new ArrayList<>(recordings)) {
            r.events.add(e);
            switch (e.kind()) {
                case MAPPING_START, SEQUENCE_START -> r.depth++;
                case MAPPING_END, SEQUENCE_END -> r.depth--;
                default -> { }
            }
            if (r.depth == 0) {
                anchors.put(r.name, r.events);
                recordings.remove(r);
            }
        }
    }

    private void expand(RawEvent alias) {
        List<RawEvent> events = anchors.get(alias.value());
        if (events == null) {
            boolean recursive = recordings.stream().anyMatch(r -> r.name.equals(alias.value()));
            throw new YamlParsingException(recursive
                    ? "Recursive alias '*" + alias.value() + "' is not supported"
                    : "Unknown anchor '" + alias.value() + "'", alias.start());
        }
        expanded += events.size();
        if (expanded > maxExpanded) {
            throw new YamlParsingException("Alias expansion exceeds the limit of " + maxExpanded
                    + " events (possible entity-expansion attack)", alias.start());
        }
        // Replay events carry the position of the alias so errors point at the use site.
        for (RawEvent e : events) replay.add(e);
    }
}
