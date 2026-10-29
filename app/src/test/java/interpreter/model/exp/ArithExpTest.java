
package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArithExpTest {

    private final IDictionary<String, IValue> symTable =
            new MyDictionary<>();

    private IExp num(int value) {
        return new ValueExp(new IntValue(value));
    }

    @Test
    void additionWorks() throws MyException {

        IExp expression = new ArithExp(
                ArithOp.ADD,
                num(10),
                num(5)
        );

        assertEquals(
                new IntValue(15),
                expression.eval(symTable)
        );
    }

    @Test
    void subtractionWorks() throws MyException {

        IExp expression = new ArithExp(
                ArithOp.SUB,
                num(10),
                num(4)
        );

        assertEquals(
                new IntValue(6),
                expression.eval(symTable)
        );
    }

    @Test
    void multiplicationWorks() throws MyException {

        IExp expression = new ArithExp(
                ArithOp.MUL,
                num(5),
                num(6)
        );

        assertEquals(
                new IntValue(30),
                expression.eval(symTable)
        );
    }

    @Test
    void divisionWorks() throws MyException {

        IExp expression = new ArithExp(
                ArithOp.DIV,
                num(20),
                num(4)
        );

        assertEquals(
                new IntValue(5),
                expression.eval(symTable)
        );
    }

    @Test
    void divisionByZeroThrowsException() {

        IExp expression = new ArithExp(
                ArithOp.DIV,
                num(10),
                num(0)
        );

        ExpressionException exception = assertThrows(
                ExpressionException.class,
                () -> expression.eval(symTable)
        );

        assertEquals(
                "division by zero",
                exception.getMessage()
        );
    }

    @Test
    void booleanFirstOperandThrowsException() {

        IExp expression = new ArithExp(
                ArithOp.ADD,
                new ValueExp(new BoolValue(true)),
                num(5)
        );

        assertThrows(
                ExpressionException.class,
                () -> expression.eval(symTable)
        );
    }

    @Test
    void booleanSecondOperandThrowsException() {

        IExp expression = new ArithExp(
                ArithOp.ADD,
                num(5),
                new ValueExp(new BoolValue(false))
        );

        assertThrows(
                ExpressionException.class,
                () -> expression.eval(symTable)
        );
    }

    @Test
    void nestedExpressionsWork() throws MyException {

        IExp multiplication = new ArithExp(
                ArithOp.MUL,
                num(3),
                num(5)
        );

        IExp addition = new ArithExp(
                ArithOp.ADD,
                num(2),
                multiplication
        );

        assertEquals(
                new IntValue(17),
                addition.eval(symTable)
        );
    }

    @Test
    void negativeResultWorks() throws MyException {

        IExp expression = new ArithExp(
                ArithOp.SUB,
                num(5),
                num(10)
        );

        assertEquals(
                new IntValue(-5),
                expression.eval(symTable)
        );
    }
}
