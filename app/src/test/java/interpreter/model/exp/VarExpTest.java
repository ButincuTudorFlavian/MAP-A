
package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;

import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.BoolValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarExpTest {

    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    @Test
    void variableExpressionReturnsCorrectInteger()
            throws MyException {

        symTable.put("a", new IntValue(10));

        IExp expression = new VarExp("a");

        assertEquals(
                new IntValue(10),
                expression.eval(symTable)
        );
    }

    @Test
    void variableExpressionReturnsCorrectBoolean()
            throws MyException {

        symTable.put("flag", new BoolValue(true));

        IExp expression = new VarExp("flag");

        assertEquals(
                new BoolValue(true),
                expression.eval(symTable)
        );
    }

    @Test
    void undefinedVariableThrowsException() {

        IExp expression = new VarExp("x");

        assertThrows(
                ExpressionException.class,
                () -> expression.eval(symTable)
        );
    }

    @Test
    void variableExpressionReflectsUpdatedValue()
            throws MyException {

        symTable.put("a", new IntValue(5));

        IExp expression = new VarExp("a");

        assertEquals(
                new IntValue(5),
                expression.eval(symTable)
        );

        symTable.update("a", new IntValue(20));

        assertEquals(
                new IntValue(20),
                expression.eval(symTable)
        );
    }
}
