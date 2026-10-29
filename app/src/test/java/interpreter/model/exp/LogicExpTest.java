
package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpTest {

    private final IDictionary<String, IValue> symTable =
            new MyDictionary<>();

    private IExp bool(boolean value) {
        return new ValueExp(new BoolValue(value));
    }

    @Test
    void andTrueTrueReturnsTrue() throws MyException {

        IExp expression = new LogicExp(
                LogicOp.AND,
                bool(true),
                bool(true)
        );

        assertEquals(
                new BoolValue(true),
                expression.eval(symTable)
        );
    }

    @Test
    void andTrueFalseReturnsFalse() throws MyException {

        IExp expression = new LogicExp(
                LogicOp.AND,
                bool(true),
                bool(false)
        );

        assertEquals(
                new BoolValue(false),
                expression.eval(symTable)
        );
    }

    @Test
    void orFalseTrueReturnsTrue() throws MyException {

        IExp expression = new LogicExp(
                LogicOp.OR,
                bool(false),
                bool(true)
        );

        assertEquals(
                new BoolValue(true),
                expression.eval(symTable)
        );
    }

    @Test
    void orFalseFalseReturnsFalse() throws MyException {

        IExp expression = new LogicExp(
                LogicOp.OR,
                bool(false),
                bool(false)
        );

        assertEquals(
                new BoolValue(false),
                expression.eval(symTable)
        );
    }

    @Test
    void integerFirstOperandThrowsException() {

        IExp expression = new LogicExp(
                LogicOp.AND,
                new ValueExp(new IntValue(10)),
                bool(true)
        );

        assertThrows(
                ExpressionException.class,
                () -> expression.eval(symTable)
        );
    }

    @Test
    void integerSecondOperandThrowsException() {

        IExp expression = new LogicExp(
                LogicOp.OR,
                bool(false),
                new ValueExp(new IntValue(5))
        );

        assertThrows(
                ExpressionException.class,
                () -> expression.eval(symTable)
        );
    }

    @Test
    void nestedLogicalExpressionsWork() throws MyException {

        IExp innerExpression = new LogicExp(
                LogicOp.OR,
                bool(false),
                bool(true)
        );

        IExp outerExpression = new LogicExp(
                LogicOp.AND,
                bool(true),
                innerExpression
        );

        assertEquals(
                new BoolValue(true),
                outerExpression.eval(symTable)
        );
    }

    @Test
    void logicalExpressionWithVariableWorks() throws MyException {

        symTable.put("flag", new BoolValue(true));

        IExp expression = new LogicExp(
                LogicOp.AND,
                new VarExp("flag"),
                bool(false)
        );

        assertEquals(
                new BoolValue(false),
                expression.eval(symTable)
        );
    }

    @Test
    void logicExpressionToStringWorks() {

        IExp expression = new LogicExp(
                LogicOp.AND,
                bool(true),
                bool(false)
        );

        assertEquals(
                "(true and false)",
                expression.toString()
        );
    }
}
