/**
 * 
 */
package za.co.sindi.yaml;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public final class YamlConfig {

	private YamlConfig() {
		throw new AssertionError("Private constructor.");
	}
	
	/** Generator: number of spaces per indentation level (Integer, 1..9, default 2). */
    public static final String INDENT = "za.co.sindi.yaml.indent";
    
    /** Generator: indent block sequences below their parent key (Boolean, default true). */
    public static final String INDENT_SEQUENCES = "za.co.sindi.yaml.indentSequences";
    
    /** Generator: emit every collection in flow style, {@code {a: 1}} / {@code [1, 2]} (Boolean, default false). */
    public static final String FLOW_STYLE = "za.co.sindi.yaml.flowStyle";
    
    /** Generator: always write the {@code ---} document start marker (Boolean, default false). */
    public static final String EXPLICIT_DOCUMENT_START = "za.co.sindi.yaml.explicitDocumentStart";
   
    /** Parser: maximum nesting depth of collections (Integer, default 1000). */
    public static final String MAX_DEPTH = "za.co.sindi.yaml.maxDepth";
    
    /** Parser: maximum number of events produced by alias expansion per stream (Integer, default 1,000,000). */
    public static final String MAX_ALIAS_EXPANSION = "za.co.sindi.yaml.maxAliasExpansion";
    
    /** Parser: tolerate duplicate mapping keys, last one wins (Boolean, default false). */
    public static final String ALLOW_DUPLICATE_KEYS = "za.co.sindi.yaml.allowDuplicateKeys";
}
