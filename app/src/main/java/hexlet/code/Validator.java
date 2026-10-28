package hexlet.code;

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
}
