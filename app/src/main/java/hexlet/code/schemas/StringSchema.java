package hexlet.code.schemas;

/** Schema for validating string values. */
public class StringSchema extends BaseSchema<String> {

    /**
     * Makes the value required: {@code null} and empty strings become invalid.
     *
     * @return this schema for chaining
     */
    public StringSchema required() {
        setRequired();
        return this;
    }

    /**
     * Adds a minimum length constraint. Replaces any previously set minimum length.
     *
     * @param length minimal allowed length
     * @return this schema for chaining
     */
    public StringSchema minLength(int length) {
        addCheck("minLength", value -> value.length() >= length);
        return this;
    }

    /**
     * Adds a constraint that the string must contain the given substring. Replaces any previously
     * set substring constraint.
     *
     * @param substring substring that must be present
     * @return this schema for chaining
     */
    public StringSchema contains(String substring) {
        addCheck("contains", value -> value.contains(substring));
        return this;
    }

    @Override
    protected final boolean isEmpty(String value) {
        return value.isEmpty();
    }
}
