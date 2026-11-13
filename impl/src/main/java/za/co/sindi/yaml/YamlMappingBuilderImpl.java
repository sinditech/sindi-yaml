/**
 * 
 */
package za.co.sindi.yaml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author Buhake Sindi
 * @since 08 November 2026
 */
public class YamlMappingBuilderImpl implements YamlMappingBuilder {

	private Map<String, YamlNode> valueMap;
	
	/**
	 * 
	 */
	public YamlMappingBuilderImpl() {
		super();
		// TODO Auto-generated constructor stub
		valueMap = new LinkedHashMap<>();
	}
	
	/**
	 * @param mapping
	 */
	public YamlMappingBuilderImpl(final YamlMapping mapping) {
		super();
		// TODO Auto-generated constructor stub
		valueMap = new LinkedHashMap<>();
		valueMap.putAll(mapping);
	}

	/**
	 * @param mapping
	 */
    public YamlMappingBuilderImpl(Map<String, ?> map) {
        this.valueMap = new LinkedHashMap<>();
    	populate(map);
    }

	@Override
	public YamlMappingBuilder add(String name, YamlNode value) {
		// TODO Auto-generated method stub
		validateName(name);
        validateValue(value);
		valueMap.put(name, value);
		return this;
	}

	@Override
	public YamlMappingBuilder add(String name, String value) {
		// TODO Auto-generated method stub
		validateName(name);
        validateValue(value);
		valueMap.put(name, new za.co.sindi.yaml.node.YamlString(value));
		return this;
	}

	@Override
	public YamlMappingBuilder add(String name, BigInteger value) {
		// TODO Auto-generated method stub
		validateName(name);
        validateValue(value);
		valueMap.put(name, new za.co.sindi.yaml.node.YamlBigIntegerNumber(value));
		return this;
	}

	@Override
	public YamlMappingBuilder add(String name, BigDecimal value) {
		// TODO Auto-generated method stub
		validateName(name);
        validateValue(value);
		valueMap.put(name, new za.co.sindi.yaml.node.YamlBigDecimalNumber(value));
		return this;
	}

	@Override
	public YamlMappingBuilder add(String name, int value) {
		// TODO Auto-generated method stub
		validateName(name);
		valueMap.put(name, new za.co.sindi.yaml.node.YamlIntegerNumber(value));
		return this;
	}

	@Override
	public YamlMappingBuilder add(String name, long value) {
		// TODO Auto-generated method stub
		validateName(name);
		valueMap.put(name, new za.co.sindi.yaml.node.YamlLongNumber(value));
		return this;
	}

	@Override
	public YamlMappingBuilder add(String name, double value) {
		// TODO Auto-generated method stub
		validateName(name);
		valueMap.put(name, new za.co.sindi.yaml.node.YamlDoubleNumber(value));
		return this;
	}

	@Override
	public YamlMappingBuilder add(String name, boolean value) {
		// TODO Auto-generated method stub
		validateName(name);
		valueMap.put(name, new za.co.sindi.yaml.node.YamlBoolean(value));
		return this;
	}

	@Override
	public YamlMappingBuilder addNull(String name) {
		// TODO Auto-generated method stub
		validateName(name);
		valueMap.put(name, new za.co.sindi.yaml.node.YamlNull());
		return this;
	}

	@Override
	public YamlMappingBuilder add(String name, YamlMappingBuilder builder) {
		// TODO Auto-generated method stub
		validateName(name);
        validateValue(builder);
		valueMap.put(name, builder.build());
		return this;
	}

	@Override
	public YamlMappingBuilder add(String name, YamlSequenceBuilder builder) {
		// TODO Auto-generated method stub
		validateName(name);
        validateValue(builder);
		valueMap.put(name, builder.build());
		return this;
	}

	@Override
	public YamlMapping build() {
		// TODO Auto-generated method stub
		return new za.co.sindi.yaml.node.YamlMapping(valueMap);
	}
	
	private void validateName(String name) {
        if (name == null) {
            throw new NullPointerException("A YAML mapping name is null.");
        }
    }

    private void validateValue(Object value) {
        if (value == null) {
            throw new NullPointerException("A YAML value is null.");
        }
    }
    
    private void populate(Map<String, ?> map) {
        final Set<String> fields = map.keySet();
        for (String field : fields) {
            Object value = map.get(field);
            if (value instanceof Optional) {
                ((Optional<?>) value).ifPresent(v ->
                        this.valueMap.put(field, Values.of(v)));
            } else {
                this.valueMap.put(field, Values.of(value));
            }
        }
    }
}
