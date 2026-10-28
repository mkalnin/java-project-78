package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.schemas.StringSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidatorTest {

    private static final int MIN_LENGTH = 5;
    private static final int LONG_MIN_LENGTH = 10;
    private static final int SHORT_MIN_LENGTH = 4;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    @Test
    void testValidatorIsInstantiable() {
        assertThat(validator).isNotNull();
    }

    @Test
    void testStringReturnsNewSchemaEachTime() {
        StringSchema first = validator.string();
        StringSchema second = validator.string();

        assertThat(first).isNotNull().isNotSameAs(second);
    }

    @Test
    void testEmptySchemaAcceptsAnyString() {
        StringSchema schema = validator.string();

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid("")).isTrue();
        assertThat(schema.isValid("hexlet")).isTrue();
    }

    @Test
    void testRequired() {
        StringSchema schema = validator.string();

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid("")).isTrue();

        schema.required();

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid("")).isFalse();
        assertThat(schema.isValid("what does the fox say")).isTrue();
        assertThat(schema.isValid("hexlet")).isTrue();
    }

    @Test
    void testMinLength() {
        StringSchema schema = validator.string().minLength(MIN_LENGTH);

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid("")).isTrue();
        assertThat(schema.isValid("hex")).isFalse();
        assertThat(schema.isValid("hexle")).isTrue();
        assertThat(schema.isValid("hexlet")).isTrue();
    }

    @Test
    void testMinLengthWithRequired() {
        StringSchema schema = validator.string().required().minLength(MIN_LENGTH);

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid("")).isFalse();
        assertThat(schema.isValid("hex")).isFalse();
        assertThat(schema.isValid("hexlet")).isTrue();
    }

    @Test
    void testContains() {
        StringSchema schema = validator.string().contains("wh");

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid("")).isTrue();
        assertThat(schema.isValid("what does the fox say")).isTrue();
        assertThat(schema.isValid("hexlet")).isFalse();
    }

    @Test
    void testContainsReplacesPreviousConstraint() {
        StringSchema schema = validator.string().required();

        assertThat(schema.contains("wh").isValid("what does the fox say")).isTrue();
        assertThat(schema.contains("what").isValid("what does the fox say")).isTrue();
        assertThat(schema.contains("whatthe").isValid("what does the fox say")).isFalse();
        assertThat(schema.isValid("what does the fox say")).isFalse();
    }

    @Test
    void testMinLengthReplacesPreviousConstraint() {
        StringSchema schema = validator.string();

        assertThat(schema.minLength(LONG_MIN_LENGTH).minLength(SHORT_MIN_LENGTH).isValid("Hexlet"))
                .isTrue();
        assertThat(schema.minLength(LONG_MIN_LENGTH).isValid("Hexlet")).isFalse();
    }

    @Test
    void testAllConstraintsWorkTogether() {
        StringSchema schema = validator.string().required().minLength(MIN_LENGTH).contains("hex");

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid("")).isFalse();
        assertThat(schema.isValid("hex")).isFalse();
        assertThat(schema.isValid("hexlet")).isTrue();
        assertThat(schema.isValid("hexlet is great")).isTrue();
        assertThat(schema.isValid("what does the fox say")).isFalse();
    }

    @Test
    void testSchemasAreIndependent() {
        StringSchema first = validator.string().required();
        StringSchema second = validator.string();

        assertThat(first.isValid(null)).isFalse();
        assertThat(second.isValid(null)).isTrue();
    }
}
