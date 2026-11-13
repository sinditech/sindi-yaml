/**
 * 
 */
package za.co.sindi.yaml;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import za.co.sindi.yaml.YamlNull.Type;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 * 
 */
public record YamlContext(int maxParsingCharactersLimit, int maxDepth, Type outputNullType, boolean allowDuplicateKeys, int indent, boolean indentSequences, boolean flow, boolean explicitStart, Map<String, ?> raw) {

	/** Default maximum level of nesting. */
    private static final int DEFAULT_MAX_DEPTH = 100; //1000;

    /** Default maximum number of characters to parse from one document. */
    private static final int DEFAULT_MAX_PARSING_CHARACTERS_LIMIT = 2_000_000; //15_000_000;
    
    /** Default Null Type **/
    private static final Type DEFAULT_OUTPUT_NULL_TYPE = Type.EXPLICIT_NULL;
    
    public static final YamlContext DEFAULT = from(Map.of());
    
    public YamlContext {
    	if (maxParsingCharactersLimit < 1 || maxDepth < 1) throw new IllegalArgumentException("Limits must be positive");
    }
    
    public static YamlContext from(Map<String, ?> config) {
        Map<String, Object> raw = Collections.unmodifiableMap(new LinkedHashMap<>(config));
        return new YamlContext(
        		intProp(config, YamlConfig.MAX_ALIAS_EXPANSION, DEFAULT_MAX_PARSING_CHARACTERS_LIMIT, 0, Integer.MAX_VALUE),
        		intProp(config, YamlConfig.MAX_DEPTH, DEFAULT_MAX_DEPTH, 1, Integer.MAX_VALUE),
        		DEFAULT_OUTPUT_NULL_TYPE,
                boolProp(config, YamlConfig.ALLOW_DUPLICATE_KEYS, false),
                intProp(config, YamlConfig.INDENT, 2, 1, 9),
                boolProp(config, YamlConfig.INDENT_SEQUENCES, true),
                boolProp(config, YamlConfig.FLOW_STYLE, false),
                boolProp(config, YamlConfig.EXPLICIT_DOCUMENT_START, false),
                raw);
    }

    private static int intProp(Map<String, ?> c, String key, int def, int min, int max) {
        Object v = c.get(key);
        if (v == null) return def;
        int n;
        try {
            n = v instanceof Number num ? num.intValue() : Integer.parseInt(v.toString().trim());
        } catch (NumberFormatException e) {
            throw new YamlException("Configuration property " + key + " must be an integer but was '" + v + "'");
        }
        if (n < min || n > max) throw new YamlException("Configuration property " + key + " must be in [" + min + ", " + max + "] but was " + n);
        return n;
    }

    private static boolean boolProp(Map<String, ?> c, String key, boolean def) {
        Object v = c.get(key);
        if (v == null) return def;
        if (v instanceof Boolean b) return b;
        String s = v.toString().trim();
        if (s.equalsIgnoreCase("true")) return true;
        if (s.equalsIgnoreCase("false")) return false;
        throw new YamlException("Configuration property " + key + " must be a boolean but was '" + v + "'");
    }
}
