package hexlet.code;

import hexlet.code.schemas.MapSchema;
import hexlet.code.schemas.NumberSchema;
import hexlet.code.schemas.StringSchema;

/** Entry point of the validation library. */
public class Validator {

    /**
     * Creates a new schema for validating string values.
     *
     * @return a fresh {@link StringSchema} instance
     */
    public StringSchema string() {
        return new StringSchema();
    }

    /**
     * Creates a new schema for validating number values.
     *
     * @return a fresh {@link NumberSchema} instance
     */
    public NumberSchema number() {
        return new NumberSchema();
    }

    /**
     * Creates a new schema for validating map values.
     *
     * @return a fresh {@link MapSchema} instance
     */
    public MapSchema map() {
        return new MapSchema();
    }
}
