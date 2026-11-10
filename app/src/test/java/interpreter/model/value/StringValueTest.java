
package interpreter.model.value;

import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValueTest {

    @Test
    void getTypeReturnsStringType() {
        StringValue value = new StringValue("hello");

        assertEquals(new StringType(), value.getType());
    }

    @Test
    void equalValuesHaveSameHashCode() {
        StringValue first = new StringValue("test.in");
        StringValue second = new StringValue("test.in");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void differentValuesAreNotEqual() {
        assertNotEquals(
                new StringValue("first"),
                new StringValue("second")
        );
    }

    @Test
    void toStringAddsQuotes() {
        StringValue value = new StringValue("hello");

        assertEquals("\"hello\"", value.toString());
    }
}
