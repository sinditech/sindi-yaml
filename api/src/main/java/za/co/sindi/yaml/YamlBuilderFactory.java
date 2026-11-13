/**
 * 
 */
package za.co.sindi.yaml;

import java.util.Collection;
import java.util.Map;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public interface YamlBuilderFactory {

	/**
     * Creates a {@code YamlMappingBuilder} instance that is used to build
     * {@link YamlMapping}.
     *
     * @return a JSON mapping builder
     */
    YamlMappingBuilder createMappingBuilder();

    /**
     * Creates a {@code YamlMappingBuilder} instance, initialized with an mapping.
     *
     * @param mapping the initial mapping in the builder
     * @return a JSON mapping builder
     * @throws NullPointerException if specified mapping is {@code null}
     *
     * @since 1.1
     */
    default YamlMappingBuilder createMappingBuilder(YamlMapping mapping) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a {@code YamlMappingBuilder} instance, initialized with the specified mapping.
     *
     * @param mapping the initial mapping in the builder
     * @return a JSON mapping builder
     * @throws NullPointerException if specified mapping is {@code null}
     *
     * @since 1.1
     */
    default YamlMappingBuilder createMappingBuilder(Map<String, Object> object) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a {@code YamlSequenceBuilder} instance that is used to build
     * {@link YamlSequence}
     *
     * @return a JSON sequence builder
     */
    YamlSequenceBuilder createSequenceBuilder();

    /**
     * Creates a {@code YamlSequenceBuilder} instance, initialized with an sequence.
     *
     * @param sequence the initial sequence in the builder
     * @return a JSON sequence builder
     * @throws NullPointerException if specified sequence is {@code null}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder createSequenceBuilder(YamlSequence sequence) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a {@code YamlSequenceBuilder} instance,
     * initialized with the content of specified collection.
     *
     * @param collection the initial data for the builder
     * @return a JSON sequence builder
     * @throws NullPointerException if specified collection is {@code null}
     *
     * @since 1.1
     */
    default YamlSequenceBuilder createSequenceBuilder(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns read-only map of supported provider specific configuration
     * properties that are used to configure the created JSON builders.
     * If there are any specified configuration properties that are not
     * supported by the provider, they won't be part of the returned map.
     *
     * @return a map of supported provider specific properties that are used
     * to configure the builders. The map be empty but not null.
     */
    Map<String, ?> getConfigInUse();
}
