/**
 * 
 */
package za.co.sindi.yaml;

import java.util.Collection;
import java.util.Map;

/**
 * @author Buhake Sindi
 * @since 13 November 2026
 */
public record YamlBuilderFactoryImpl(YamlContext yamlContext) implements YamlBuilderFactory {

	@Override
	public YamlMappingBuilder createMappingBuilder() {
		// TODO Auto-generated method stub
		return new YamlMappingBuilderImpl();
	}

	@Override
	public YamlMappingBuilder createMappingBuilder(YamlMapping mapping) {
		// TODO Auto-generated method stub
		return YamlBuilderFactory.super.createMappingBuilder(mapping);
	}

	@Override
	public YamlMappingBuilder createMappingBuilder(Map<String, Object> object) {
		// TODO Auto-generated method stub
		return YamlBuilderFactory.super.createMappingBuilder(object);
	}

	@Override
	public YamlSequenceBuilder createSequenceBuilder() {
		// TODO Auto-generated method stub
		return new YamlSequenceBuilderImpl();
	}

	@Override
	public YamlSequenceBuilder createSequenceBuilder(YamlSequence sequence) {
		// TODO Auto-generated method stub
		return new YamlSequenceBuilderImpl(sequence);
	}

	@Override
	public YamlSequenceBuilder createSequenceBuilder(Collection<?> collection) {
		// TODO Auto-generated method stub
		return new YamlSequenceBuilderImpl(collection);
	}

	@Override
	public Map<String, ?> getConfigInUse() {
		// TODO Auto-generated method stub
		return yamlContext.raw();
	}
}
