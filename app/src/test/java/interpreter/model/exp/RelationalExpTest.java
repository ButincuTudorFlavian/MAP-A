
package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelationalExpTest {

    private void assertResult(
            RelOp op,
            int left,
            int right,
            boolean expected
    ) throws MyException {

        RelationalExp exp = new RelationalExp(
                op,
                new ValueExp(new IntValue(left)),
                new ValueExp(new IntValue(right))
        );

        assertEquals(
                new BoolValue(expected),
                exp.eval(new MyDictionary<>())
        );
    }

    @Test
    void testLessThan() throws MyException {
        assertResult(RelOp.LT, 3, 5, true);
        assertResult(RelOp.LT, 5, 3, false);
    }

    @Test
    void testLessThanOrEqual() throws MyException {
        assertResult(RelOp.LE, 5, 5, true);
        assertResult(RelOp.LE, 7, 5, false);
    }

    @Test
    void testEqual() throws MyException {
        assertResult(RelOp.EQ, 5, 5, true);
        assertResult(RelOp.EQ, 5, 7, false);
    }

    @Test
    void testNotEqual() throws MyException {
        assertResult(RelOp.NE, 5, 7, true);
        assertResult(RelOp.NE, 5, 5, false);
    }

    @Test
    void testGreaterThan() throws MyException {
        assertResult(RelOp.GT, 7, 5, true);
        assertResult(RelOp.GT, 3, 5, false);
    }

    @Test
    void testGreaterThanOrEqual() throws MyException {
        assertResult(RelOp.GE, 5, 5, true);
        assertResult(RelOp.GE, 3, 5, false);
    }

    @Test
    void firstOperandMustBeInteger() {
        RelationalExp exp = new RelationalExp(
                RelOp.LT,
                new ValueExp(new BoolValue(true)),
                new ValueExp(new IntValue(5))
        );

        assertThrows(
                ExpressionException.class,
                () -> exp.eval(new MyDictionary<>())
        );
    }

    @Test
    void secondOperandMustBeInteger() {
        RelationalExp exp = new RelationalExp(
                RelOp.LT,
                new ValueExp(new IntValue(5)),
                new ValueExp(new BoolValue(true))
        );

        assertThrows(
                ExpressionException.class,
                () -> exp.eval(new MyDictionary<>())
        );
    }
}
