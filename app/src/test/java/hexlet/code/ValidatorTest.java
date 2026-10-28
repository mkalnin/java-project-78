package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.schemas.BaseSchema;
import hexlet.code.schemas.MapSchema;
import hexlet.code.schemas.NumberSchema;
import hexlet.code.schemas.StringSchema;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidatorTest {

    private static final int MIN_LENGTH = 5;
    private static final int LONG_MIN_LENGTH = 10;
    private static final int SHORT_MIN_LENGTH = 4;

    private static final int POSITIVE_NUMBER = 10;
    private static final int NEGATIVE_NUMBER = -10;
    private static final int ZERO = 0;
    private static final int RANGE_MIN = 5;
    private static final int RANGE_MAX = 10;
    private static final int RANGE_MIDDLE = 7;
    private static final int BELOW_RANGE = 4;
    private static final int ABOVE_RANGE = 11;
    private static final int NARROW_RANGE_MIN = 6;
    private static final int NARROW_RANGE_MAX = 9;
    private static final int NEGATIVE_IN_RANGE = -5;

    private static final int MAP_SIZE = 2;
    private static final int OTHER_MAP_SIZE = 1;

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String AGE = "age";
    private static final int MIN_LAST_NAME_LENGTH = 2;
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 120;

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

    @Test
    void testNumberReturnsNewSchemaEachTime() {
        NumberSchema first = validator.number();
        NumberSchema second = validator.number();

        assertThat(first).isNotNull().isNotSameAs(second);
    }

    @Test
    void testEmptyNumberSchemaAcceptsAnyNumber() {
        NumberSchema schema = validator.number();

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid(RANGE_MIN)).isTrue();
        assertThat(schema.isValid(NEGATIVE_IN_RANGE)).isTrue();
        assertThat(schema.isValid(ZERO)).isTrue();
    }

    @Test
    void testNumberRequired() {
        NumberSchema schema = validator.number();

        assertThat(schema.isValid(null)).isTrue();

        schema.required();

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(POSITIVE_NUMBER)).isTrue();
        assertThat(schema.isValid(NEGATIVE_NUMBER)).isTrue();
        assertThat(schema.isValid(ZERO)).isTrue();
    }

    @Test
    void testNumberPositive() {
        NumberSchema schema = validator.number().positive();

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid(POSITIVE_NUMBER)).isTrue();
        assertThat(schema.isValid(NEGATIVE_NUMBER)).isFalse();
        assertThat(schema.isValid(ZERO)).isFalse();
    }

    @Test
    void testNumberPositiveWithRequired() {
        NumberSchema schema = validator.number().positive().required();

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(POSITIVE_NUMBER)).isTrue();
        assertThat(schema.isValid(NEGATIVE_NUMBER)).isFalse();
        assertThat(schema.isValid(ZERO)).isFalse();
    }

    @Test
    void testNumberRange() {
        NumberSchema schema = validator.number().range(RANGE_MIN, RANGE_MAX);

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid(RANGE_MIN)).isTrue();
        assertThat(schema.isValid(POSITIVE_NUMBER)).isTrue();
        assertThat(schema.isValid(RANGE_MIDDLE)).isTrue();
        assertThat(schema.isValid(BELOW_RANGE)).isFalse();
        assertThat(schema.isValid(ABOVE_RANGE)).isFalse();
    }

    @Test
    void testNumberRangeReplacesPreviousConstraint() {
        NumberSchema schema = validator.number().range(RANGE_MIN, RANGE_MAX);

        schema.range(NARROW_RANGE_MIN, NARROW_RANGE_MAX);

        assertThat(schema.isValid(RANGE_MIN)).isFalse();
        assertThat(schema.isValid(POSITIVE_NUMBER)).isFalse();
        assertThat(schema.isValid(NARROW_RANGE_MIN)).isTrue();
        assertThat(schema.isValid(NARROW_RANGE_MAX)).isTrue();
    }

    @Test
    void testNumberAllConstraintsWorkTogether() {
        NumberSchema schema = validator.number().required().positive().range(RANGE_MIN, RANGE_MAX);

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(RANGE_MIN)).isTrue();
        assertThat(schema.isValid(POSITIVE_NUMBER)).isTrue();
        assertThat(schema.isValid(BELOW_RANGE)).isFalse();
        assertThat(schema.isValid(ABOVE_RANGE)).isFalse();
        assertThat(schema.isValid(NEGATIVE_IN_RANGE)).isFalse();
        assertThat(schema.isValid(ZERO)).isFalse();
    }

    @Test
    void testNumberSchemasAreIndependent() {
        NumberSchema first = validator.number().required();
        NumberSchema second = validator.number();

        assertThat(first.isValid(null)).isFalse();
        assertThat(second.isValid(null)).isTrue();
    }

    @Test
    void testMapReturnsNewSchemaEachTime() {
        MapSchema first = validator.map();
        MapSchema second = validator.map();

        assertThat(first).isNotNull().isNotSameAs(second);
    }

    @Test
    void testEmptyMapSchemaAcceptsAnyMap() {
        MapSchema schema = validator.map();

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid(new HashMap<>())).isTrue();
        assertThat(schema.isValid(Map.of("key1", "value1"))).isTrue();
    }

    @Test
    void testMapRequired() {
        MapSchema schema = validator.map();

        assertThat(schema.isValid(null)).isTrue();

        schema.required();

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(new HashMap<>())).isTrue();
        assertThat(schema.isValid(Map.of("key1", "value1"))).isTrue();
    }

    @Test
    void testMapSizeof() {
        MapSchema schema = validator.map().sizeof(MAP_SIZE);

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid(new HashMap<>())).isFalse();
        assertThat(schema.isValid(Map.of("key1", "value1"))).isFalse();
        assertThat(schema.isValid(Map.of("key1", "value1", "key2", "value2"))).isTrue();
        assertThat(schema.isValid(Map.of("key1", "value1", "key2", "value2", "key3", "value3")))
                .isFalse();
    }

    @Test
    void testMapSizeofWithRequired() {
        MapSchema schema = validator.map().required().sizeof(MAP_SIZE);

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(new HashMap<>())).isFalse();
        assertThat(schema.isValid(Map.of("key1", "value1"))).isFalse();
        assertThat(schema.isValid(Map.of("key1", "value1", "key2", "value2"))).isTrue();
    }

    @Test
    void testMapSizeofReplacesPreviousConstraint() {
        MapSchema schema = validator.map().sizeof(MAP_SIZE);

        schema.sizeof(OTHER_MAP_SIZE);

        assertThat(schema.isValid(Map.of("key1", "value1"))).isTrue();
        assertThat(schema.isValid(Map.of("key1", "value1", "key2", "value2"))).isFalse();
    }

    @Test
    void testMapExampleFromDescription() {
        MapSchema schema = validator.map();

        assertThat(schema.isValid(null)).isTrue();

        schema.required();

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(new HashMap<>())).isTrue();

        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        assertThat(schema.isValid(data)).isTrue();

        schema.sizeof(MAP_SIZE);

        assertThat(schema.isValid(data)).isFalse();

        data.put("key2", "value2");
        assertThat(schema.isValid(data)).isTrue();
    }

    @Test
    void testMapSchemasAreIndependent() {
        MapSchema first = validator.map().required();
        MapSchema second = validator.map();

        assertThat(first.isValid(null)).isFalse();
        assertThat(second.isValid(null)).isTrue();
    }

    @Test
    void testShapeExampleFromDescription() {
        MapSchema schema = validator.map();

        Map<String, BaseSchema<String>> schemas = new HashMap<>();
        schemas.put(FIRST_NAME, validator.string().required());
        schemas.put(LAST_NAME, validator.string().required().minLength(MIN_LAST_NAME_LENGTH));

        schema.shape(schemas);

        Map<String, String> human1 = new HashMap<>();
        human1.put(FIRST_NAME, "John");
        human1.put(LAST_NAME, "Smith");
        assertThat(schema.isValid(human1)).isTrue();

        Map<String, String> human2 = new HashMap<>();
        human2.put(FIRST_NAME, "John");
        human2.put(LAST_NAME, null);
        assertThat(schema.isValid(human2)).isFalse();

        Map<String, String> human3 = new HashMap<>();
        human3.put(FIRST_NAME, "Anna");
        human3.put(LAST_NAME, "B");
        assertThat(schema.isValid(human3)).isFalse();
    }

    @Test
    void testShapeAcceptsNullMapWhenNotRequired() {
        MapSchema schema = validator.map().shape(Map.of(FIRST_NAME, validator.string().required()));

        assertThat(schema.isValid(null)).isTrue();
        assertThat(schema.isValid(new HashMap<>())).isFalse();
    }

    @Test
    void testShapeWithRequiredMap() {
        MapSchema schema =
                validator.map().required().shape(Map.of(FIRST_NAME, validator.string().required()));

        assertThat(schema.isValid(null)).isFalse();
        assertThat(schema.isValid(new HashMap<>())).isFalse();
        assertThat(schema.isValid(Map.of(FIRST_NAME, "John"))).isTrue();
    }

    @Test
    void testShapeIgnoresKeysMissingFromMap() {
        MapSchema schema = validator.map().shape(Map.of(FIRST_NAME, validator.string().required()));

        assertThat(schema.isValid(new HashMap<>())).isFalse();
        assertThat(schema.isValid(Map.of(FIRST_NAME, "John"))).isTrue();
        assertThat(schema.isValid(Map.of(FIRST_NAME, "John", "extra", "value"))).isTrue();
    }

    @Test
    void testShapeWithNumberSchema() {
        MapSchema schema =
                validator
                        .map()
                        .shape(Map.of(AGE, validator.number().required().range(MIN_AGE, MAX_AGE)));

        assertThat(schema.isValid(Map.of(AGE, MIN_AGE))).isTrue();
        assertThat(schema.isValid(Map.of(AGE, MAX_AGE))).isTrue();
        assertThat(schema.isValid(Map.of(AGE, MIN_AGE - 1))).isFalse();
        assertThat(schema.isValid(Map.of(AGE, MAX_AGE + 1))).isFalse();
        assertThat(schema.isValid(new HashMap<>())).isFalse();
    }

    @Test
    void testShapeWithNestedMapSchema() {
        MapSchema addressSchema =
                validator
                        .map()
                        .shape(
                                Map.of(
                                        "city",
                                        validator
                                                .string()
                                                .required()
                                                .minLength(MIN_LAST_NAME_LENGTH)));

        MapSchema schema = validator.map().shape(Map.of("address", addressSchema));

        assertThat(schema.isValid(Map.of("address", Map.of("city", "Moscow")))).isTrue();
        assertThat(schema.isValid(Map.of("address", Map.of("city", "M")))).isFalse();
        assertThat(schema.isValid(Map.of("address", new HashMap<>()))).isFalse();
    }

    @Test
    void testShapeReplacesPreviousConstraint() {
        MapSchema schema = validator.map().shape(Map.of(FIRST_NAME, validator.string().required()));

        schema.shape(Map.of(LAST_NAME, validator.string().required()));

        assertThat(schema.isValid(Map.of(LAST_NAME, "Smith"))).isTrue();
        assertThat(schema.isValid(Map.of(FIRST_NAME, "John"))).isFalse();
    }

    @Test
    void testShapeWorksTogetherWithSizeof() {
        MapSchema schema =
                validator
                        .map()
                        .sizeof(MAP_SIZE)
                        .shape(Map.of(FIRST_NAME, validator.string().required()));

        assertThat(schema.isValid(Map.of(FIRST_NAME, "John"))).isFalse();
        assertThat(schema.isValid(Map.of(FIRST_NAME, "John", LAST_NAME, "Smith"))).isTrue();
        assertThat(schema.isValid(Map.of(FIRST_NAME, "", LAST_NAME, "Smith"))).isFalse();
    }
}
