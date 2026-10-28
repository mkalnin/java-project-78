package hexlet.code.schemas;

/** Schema for validating number values. */
public class NumberSchema extends BaseSchema<Integer> {

    /**
     * Makes the value required: {@code null} becomes invalid.
     *
     * @return this schema for chaining
     */
    public NumberSchema required() {
        setRequired();
        return this;
    }

    /**
     * Adds a constraint that the number must be positive (greater than zero).
     *
     * @return this schema for chaining
     */
    public NumberSchema positive() {
        addCheck("positive", value -> value > 0);
        return this;
    }

    /**
     * Adds an inclusive range constraint. Replaces any previously set range.
     *
     * @param min lower bound (inclusive)
     * @param max upper bound (inclusive)
     * @return this schema for chaining
     */
    public NumberSchema range(int min, int max) {
        addCheck("range", value -> value >= min && value <= max);
        return this;
    }
}
