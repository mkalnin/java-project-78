package hexlet.code.schemas;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Base class for all schemas. Holds a set of named checks and applies them to values.
 *
 * @param <T> type of the value being validated
 */
public abstract class BaseSchema<T> {

    private final Map<String, Predicate<T>> checks = new LinkedHashMap<>();
    private boolean required;

    /**
     * Adds or replaces a named check. Calling it again with the same name replaces the previous
     * check.
     *
     * @param name unique name of the check
     * @param check predicate that returns true for valid values
     */
    protected final void addCheck(String name, Predicate<T> check) {
        checks.put(name, check);
    }

    /**
     * Marks the value as required, so that {@code null} (and empty values for some types) is
     * invalid.
     *
     * @return this schema for chaining
     */
    protected final BaseSchema<T> setRequired() {
        required = true;
        return this;
    }

    /**
     * Checks whether the given value satisfies all constraints of the schema.
     *
     * @param value value to validate
     * @return true if the value is valid
     */
    public boolean isValid(T value) {
        if (value == null) {
            return !required;
        }
        if (required && isEmpty(value)) {
            return false;
        }
        if (!required && isEmpty(value)) {
            return true;
        }
        return checks.values().stream().allMatch(check -> check.test(value));
    }

    /**
     * Converts an arbitrary value to the type this schema validates. Used for nested validation,
     * where values come from an untyped container. Returns {@code null} for values of a different
     * type, so that they are treated as missing.
     *
     * @param value value to convert
     * @return the value as {@code T}, or {@code null} if it is not of the expected type
     */
    @SuppressWarnings("unchecked")
    public T cast(Object value) {
        return (T) value;
    }

    /**
     * Tells whether the value should be treated as missing for this schema type.
     *
     * @param value non-null value
     * @return true if the value is considered empty
     */
    protected boolean isEmpty(T value) {
        return false;
    }
}
