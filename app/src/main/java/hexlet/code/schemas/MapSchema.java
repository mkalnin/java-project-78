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
}
