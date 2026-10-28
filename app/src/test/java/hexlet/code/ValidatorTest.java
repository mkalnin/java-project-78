package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ValidatorTest {
    @Test
    void testValidatorIsInstantiable() {
        assertThat(new Validator()).isNotNull();
    }
}
