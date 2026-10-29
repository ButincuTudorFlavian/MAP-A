
package interpreter.model.type;

import interpreter.model.value.BoolValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoolTypeTest {

    @Test
    void defaultValueIsFalse() {
        BoolType type = new BoolType();

        assertEquals(new BoolValue(false), type.defaultValue());
    }

    @Test
    void booleanValueHasCorrectType() {
        BoolValue value = new BoolValue(true);

        assertEquals(new BoolType(), value.getType());
    }

    @Test
    void trueValueWorks() {
        BoolValue value = new BoolValue(true);

        assertTrue(value.val());
    }

    @Test
    void falseValueWorks() {
        BoolValue value = new BoolValue(false);

        assertFalse(value.val());
    }

    @Test
    void booleanValueToStringWorks() {
        BoolValue value = new BoolValue(true);

        assertEquals("true", value.toString());
    }

    @Test
    void differentBooleanValuesAreNotEqual() {
        BoolValue first = new BoolValue(true);
        BoolValue second = new BoolValue(false);

        assertNotEquals(first, second);
    }
}
