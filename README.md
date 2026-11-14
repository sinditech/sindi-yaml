# sindi-yaml
YAML API (version 1.2 and higher) and a pure-Java implementation for modern Java applications.

Sindi YAML provides a **Jakarta JSON-P–style API** for YAML: an object model (`YamlMapping` / `YamlSequence` / scalars), builders, a streaming pull parser and generator, and high-level readers/writers. The design separates a stable **API** module from a pluggable **implementation**, discovered via `ServiceLoader`.

| | |
|---|---|
| **Group ID** | `za.co.sindi.yaml` |
| **Artifacts** | `sindi-yaml-api`, `sindi-yaml-impl` |
| **Version** | `0.0.1-SNAPSHOT` |
| **License** | [Apache License 2.0](LICENSE) |
| **Author** | Buhake Sindi — [Sindi Technologies (Pty) Ltd](https://www.sindi.co.za) |
| **Repository** | https://github.com/sinditech/sindi-yaml |
| **Java** | **25+** |

---

## Features

* **YAML 1.2 core schema** scalar resolution (`null`, booleans, integers, floats, `.inf` / `.nan`)
* **Object model** — immutable `YamlMapping`, `YamlSequence`, and typed scalars (`YamlString`, `YamlNumber`, `YamlBoolean`, `YamlNull`)
* **Builders** — `YamlMappingBuilder` / `YamlSequenceBuilder` (and factories) for constructing documents in code
* **Streaming pull parser** (`YamlParser`) — event-based, JsonParser-like API with document framing
* **Streaming generator** (`YamlGenerator`) — write mappings, sequences, and scalars incrementally
* **High-level I/O** — `YamlReader` / `YamlWriter` for whole-document read/write
* **Anchors, aliases, and merge keys** (`<<`) resolved when building the object model
* **SPI-based provider** — swap or ship alternate implementations without changing application code
* **Configurable limits** — max nesting depth, alias-expansion budget, duplicate-key policy, generator style

---

## Requirements

* **JDK 25** or newer
* Maven 3.9+ (for building from source)

---

## Installation

You need **both** the API and the implementation on the classpath. The API alone cannot load a provider.

### Maven

```xml
<dependency>
  <groupId>za.co.sindi.yaml</groupId>
  <artifactId>sindi-yaml-api</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
  <groupId>za.co.sindi.yaml</groupId>
  <artifactId>sindi-yaml-impl</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <scope>runtime</scope>
</dependency>
```

For snapshots, add the Central Portal Snapshots repository (also declared in this project’s root `pom.xml`):

```xml
<repository>
  <id>central-portal-snapshots</id>
  <url>https://central.sonatype.com/repository/maven-snapshots/</url>
  <releases><enabled>false</enabled></releases>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
```

Or build and install from source (see below).

---

## Quick start

### Read a document into the object model

Prefer `YamlReader` for whole-document loading (it skips `START_DOCUMENT` / `END_DOCUMENT` and materialises the root node):

```java
import java.io.StringReader;
import za.co.sindi.yaml.Yaml;
import za.co.sindi.yaml.YamlMapping;
import za.co.sindi.yaml.YamlReader;

String document = """
    defaults: &defaults
      adapter: postgres
      pool: 5
    production:
      <<: *defaults
      host: db.example.org
      replicas: [r1, r2]
    """;

try (YamlReader reader = Yaml.createReader(new StringReader(document))) {
    YamlMapping root = reader.readMapping();
    YamlMapping production = root.getYamlMapping("production");
    System.out.println(production.getString("adapter")); // postgres (via merge)
    System.out.println(production.getInt("pool"));       // 5
}
```

`read()` returns a `YamlNode` (mapping, sequence, or scalar).  
`readMapping()` / `readSequence()` require that root kind.

### Build and write a document

```java
import java.io.StringWriter;
import java.util.Map;
import za.co.sindi.yaml.*;

YamlMapping built = Yaml.createMappingBuilder()
        .add("name", "demo")
        .add("port", 8080)
        .add("enabled", true)
        .add("tags", Yaml.createSequenceBuilder().add("a").add("b"))
        .add("motd", "line one\nline two\n")
        .build();

StringWriter out = new StringWriter();
try (YamlWriter writer = Yaml.createWriterFactory(Map.of(YamlConfig.INDENT, 4))
        .createWriter(out)) {
    writer.writeMapping(built);
}
System.out.println(out);
```

### Pull parsing (streaming)

```java
import java.io.StringReader;
import za.co.sindi.yaml.Yaml;
import za.co.sindi.yaml.stream.YamlParser;

try (YamlParser parser = Yaml.createParser(new StringReader("a: [1, two]\nb: true\n"))) {
    while (parser.hasNext()) {
        YamlParser.Event e = parser.next();
        boolean hasText = e == YamlParser.Event.KEY_NAME
                || e == YamlParser.Event.VALUE_STRING
                || e == YamlParser.Event.VALUE_NUMBER;
        System.out.println(e + (hasText ? " " + parser.getString() : ""));
    }
}
```

To materialise a subtree while parsing, advance until `START_MAPPING` or `START_SEQUENCE`, then call `parser.getMapping()` / `parser.getSequence()`. Those methods require that `next()` has already positioned the parser on the matching start event.

### Streaming generation

```java
import java.io.StringWriter;
import za.co.sindi.yaml.Yaml;
import za.co.sindi.yaml.stream.YamlGenerator;

StringWriter stream = new StringWriter();
try (YamlGenerator g = Yaml.createGenerator(stream)) {
    g.writeStartMapping().write("kind", "first").writeEnd();
    g.writeStartSequence().write(1).write(2).writeEnd();
}
```

---

## Configuration

Factory methods accept a `Map<String, ?>` of provider properties. Keys are defined on `YamlConfig`:

| Property | Type | Default | Purpose |
|----------|------|---------|---------|
| `YamlConfig.INDENT` | `Integer` (1–9) | `2` | Spaces per indentation level (generator) |
| `YamlConfig.INDENT_SEQUENCES` | `Boolean` | `true` | Indent block sequences under their parent key |
| `YamlConfig.FLOW_STYLE` | `Boolean` | `false` | Emit collections in flow style (`{a: 1}`, `[1, 2]`) |
| `YamlConfig.EXPLICIT_DOCUMENT_START` | `Boolean` | `false` | Always write `---` |
| `YamlConfig.MAX_DEPTH` | `Integer` | implementation default | Maximum nesting depth |
| `YamlConfig.MAX_ALIAS_EXPANSION` | `Integer` | implementation default | Cap on events from alias expansion |
| `YamlConfig.ALLOW_DUPLICATE_KEYS` | `Boolean` | `false` | If `true`, last duplicate key wins |

Example:

```java
var factory = Yaml.createReaderFactory(Map.of(
        YamlConfig.MAX_DEPTH, 256,
        YamlConfig.ALLOW_DUPLICATE_KEYS, false));
YamlReader reader = factory.createReader(input);
```

---

## Project layout

```
sindi-yaml/
├── pom.xml                 # parent POM (Java 25, modules, publishing)
├── api/                    # sindi-yaml-api — public API & SPI
│   └── src/main/java/za/co/sindi/yaml/
│       ├── Yaml.java                 # static entry points
│       ├── YamlMapping, YamlSequence, scalars, builders
│       ├── YamlReader / YamlWriter
│       ├── YamlConfig
│       ├── spi/YamlProvider          # ServiceLoader SPI
│       └── stream/                   # YamlParser, YamlGenerator, …
└── impl/                   # sindi-yaml-impl — default provider
    └── src/main/
        ├── java/za/co/sindi/yaml/
        │   ├── spi/YamlProviderImpl
        │   ├── YamlReaderImpl, YamlWriterImpl, …
        │   ├── stream/YamlParserImpl, YamlGeneratorImpl
        │   ├── syntax/                 # Scanner, EventParser, AliasExpander
        │   └── node/                   # concrete nodes, ScalarResolver
        └── resources/META-INF/services/
            └── za.co.sindi.yaml.spi.YamlProvider
```

### Modules

| Module | Artifact | Role |
|--------|----------|------|
| `api` | `sindi-yaml-api` | Interfaces, model types, SPI — **no** implementation classes |
| `impl` | `sindi-yaml-impl` | Default provider, parser, generator, scalar resolution |

Applications depend on the API at compile time and the impl at runtime (or compile time). Alternate providers register via:

```
META-INF/services/za.co.sindi.yaml.spi.YamlProvider
```

---

## API overview

| Concern | Types | Entry points |
|---------|--------|--------------|
| Facade | `Yaml` | `createParser`, `createReader`, `createWriter`, `createGenerator`, builders, `createValue` |
| Object model | `YamlNode`, `YamlMapping`, `YamlSequence`, `YamlScalar`, … | `YamlReader.read*()`, builders, parser `getMapping()` / `getSequence()` |
| Pull parser | `YamlParser`, `YamlParser.Event` | `Yaml.createParser(...)` |
| Generator | `YamlGenerator` | `Yaml.createGenerator(...)` |
| Configuration | `YamlConfig` | maps passed to `*Factory` methods |
| SPI | `YamlProvider` | `ServiceLoader` + default `YamlProviderImpl` |

The programming model intentionally mirrors **Jakarta JSON Processing** (`Json`, `JsonReader`, `JsonParser`, …) so Java developers can transfer existing mental models to YAML.

---

## Building from source

```bash
git clone https://github.com/sinditech/sindi-yaml.git
cd sindi-yaml
mvn clean install
```

Requires **JDK 25+**.

CI workflows live under `.github/workflows/` (build, snapshot, and release).

---

## Status

This project is under active development (`0.0.1-SNAPSHOT`). APIs may still evolve before a stable release. Feedback and contributions are welcome via GitHub issues and pull requests.

---

## License

Licensed under the [Apache License, Version 2.0](LICENSE).

```
This README matches the repo’s module split, SPI model, Java 25 requirement, config keys, and the intended JSON-P-style usage (including preferring `YamlReader` over calling `getMapping()` on a fresh parser).
```
