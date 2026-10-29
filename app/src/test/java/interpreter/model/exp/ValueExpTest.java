
package interpreter.model.exp;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.BoolValue;
import interpreter.exceptions.MyException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueExpTest {

    @Test
    void integerValueExpressionReturnsCorrectValue() throws MyException {
        IDictionary<String, IValue> symTable =
                new MyDictionary<>();

        IExp expression = new ValueExp(new IntValue(10));

        assertEquals(
                new IntValue(10),
                expression.eval(symTable)
        );
    }

    @Test
    void booleanValueExpressionReturnsCorrectValue() throws MyException {
        IDictionary<String, IValue> symTable =
                new MyDictionary<>();

        IExp expression = new ValueExp(new BoolValue(true));

        assertEquals(
                new BoolValue(true),
                expression.eval(symTable)
        );
    }

    @Test
    void valueExpressionToStringWorks() {
        IExp expression = new ValueExp(new IntValue(25));

        assertEquals("25", expression.toString());
    }
}
