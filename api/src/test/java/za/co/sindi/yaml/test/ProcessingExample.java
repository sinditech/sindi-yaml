package za.co.sindi.yaml.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import za.co.sindi.yaml.Yaml;
import za.co.sindi.yaml.YamlConfig;
import za.co.sindi.yaml.YamlMapping;
import za.co.sindi.yaml.YamlWriter;
import za.co.sindi.yaml.stream.YamlGenerator;
import za.co.sindi.yaml.stream.YamlParser;

/** YAML-P: reader, object model, builders, pull parser, generator and writer. */
public class ProcessingExample {

    private static final String DOCUMENT = """
            defaults: &defaults
              adapter: postgres
              pool: 5
            production:
              <<: *defaults
              host: db.example.org
              replicas: [r1, r2]
            """;

    public static void main(String[] args) {
        // 1. Read into the immutable object model (anchors, aliases and merge keys are resolved).
        YamlMapping root = (YamlMapping) Yaml.createParser(new StringReader(DOCUMENT)).getMapping();
        YamlMapping production = root.getYamlMapping("production");
        System.out.println("adapter   = " + production.getString("adapter"));      // merged from *defaults
        System.out.println("pool      = " + production.getInt("pool"));
        System.out.println("replica 1 = " + root.getValue("/production/replicas/1"));  // pointer lookup

        // 2. Build a new document with the builders, then write it.
        YamlMapping built = Yaml.createMappingBuilder()
                .add("name", "demo")
                .add("port", 8080)
                .add("enabled", true)
                .add("tags", Yaml.createSequenceBuilder().add("a").add("b"))
                .add("motd", "line one\nline two\n")
                .build();
        StringWriter out = new StringWriter();
        try (YamlWriter writer = Yaml.createWriterFactory(Map.of(YamlConfig.INDENT, 4)).createWriter(out)) {
            writer.writeMapping(built);
        }
        System.out.println("--- written ---\n" + out);

        // 3. Pull parsing: no object model is built.
        try (YamlParser parser = Yaml.createParser(new StringReader("a: [1, two]\nb: true\n"))) {
            while (parser.hasNext()) {
                YamlParser.Event e = parser.next();
                boolean hasText = e == YamlParser.Event.KEY_NAME || e == YamlParser.Event.VALUE_STRING || e == YamlParser.Event.VALUE_NUMBER;
                System.out.println(e + (hasText ? " " + parser.getString() : "") + "  @line " + parser.getLocation().getLineNumber());
            }
        }

        // 4. Streaming generation, several documents in one stream.
        StringWriter stream = new StringWriter();
        try (YamlGenerator g = Yaml.createGenerator(stream)) {
            g.writeStartMapping().write("kind", "first").writeEnd();
            g.writeStartSequence().write(1).write(2).writeEnd();
        }
        System.out.println("--- generated ---\n" + stream);
    }
}
