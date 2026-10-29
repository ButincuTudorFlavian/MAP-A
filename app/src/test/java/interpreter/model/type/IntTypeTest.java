
package interpreter.model.type;

import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntTypeTest {

    @Test
    void defaultValueIsZero() {
        IntType type = new IntType();

        assertEquals(new IntValue(0), type.defaultValue());
    }

    @Test
    void integerValueHasCorrectType() {
        IntValue value = new IntValue(25);

        assertEquals(new IntType(), value.getType());
    }

    @Test
    void integerValueToStringWorks() {
        IntValue value = new IntValue(42);

        assertEquals("42", value.toString());
    }

    @Test
    void negativeIntegerValueWorks() {
        IntValue value = new IntValue(-10);

        assertEquals(-10, value.val());
    }

    @Test
    void integerValuesAreEqual() {
        IntValue first = new IntValue(10);
        IntValue second = new IntValue(10);

        assertEquals(first, second);
    }
}
