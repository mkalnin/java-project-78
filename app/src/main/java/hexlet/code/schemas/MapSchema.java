package hexlet.code.schemas;

import java.util.Map;

/** Schema for validating {@link Map} values. */
public class MapSchema extends BaseSchema<Map<?, ?>> {

    /**
     * Makes the value required: {@code null} becomes invalid.
     *
     * @return this schema for chaining
     */
    public MapSchema required() {
        setRequired();
        return this;
    }

    /**
     * Adds a constraint on the number of key-value pairs. Replaces any previously set size.
     *
     * @param size exact number of entries the map must contain
     * @return this schema for chaining
     */
    public MapSchema sizeof(int size) {
        addCheck("sizeof", value -> value.size() == size);
        return this;
    }

    /**
     * Adds a constraint that every key present in the given schemas map must have a value valid
     * according to the corresponding schema. Keys missing from the validated map are not checked.
     * Replaces any previously set shape.
     *
     * @param schemas schemas for the values of the validated map, keyed by property name
     * @return this schema for chaining
     */
    public MapSchema shape(Map<String, ? extends BaseSchema<?>> schemas) {
        addCheck(
                "shape",
                value ->
                        schemas.entrySet().stream()
                                .allMatch(
                                        entry ->
                                                isValidValue(
                                                        entry.getValue(),
                                                        value.get(entry.getKey()))));
        return this;
    }

    private static <V> boolean isValidValue(BaseSchema<V> schema, Object value) {
        return schema.isValid(schema.cast(value));
    }
}
