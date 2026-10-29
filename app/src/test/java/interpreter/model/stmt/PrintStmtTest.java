
package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.IntValue;
import interpreter.model.value.BoolValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrintStmtTest {

    @Test
    void printAddsIntegerToOutput() throws MyException {
        IStmt program = new PrintStmt(
                new ValueExp(new IntValue(42))
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getExeStack().pop().execute(state);

        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(42), state.getOut().get(0));
    }

    @Test
    void printAddsBooleanToOutput() throws MyException {
        IStmt program = new PrintStmt(
                new ValueExp(new BoolValue(true))
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getExeStack().pop().execute(state);

        assertEquals(
                new BoolValue(true),
                state.getOut().get(0)
        );
    }

    @Test
    void printCanReadVariableValue() throws MyException {
        IStmt program = new PrintStmt(new VarExp("a"));

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getSymTable().put("a", new IntValue(100));

        state.getExeStack().pop().execute(state);

        assertEquals(
                new IntValue(100),
                state.getOut().get(0)
        );
    }

    @Test
    void printUndefinedVariableThrowsException() {
        IStmt program = new PrintStmt(new VarExp("missing"));

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        assertThrows(
                ExpressionException.class,
                () -> state.getExeStack().pop().execute(state)
        );

        assertEquals(0, state.getOut().size());
    }
}
