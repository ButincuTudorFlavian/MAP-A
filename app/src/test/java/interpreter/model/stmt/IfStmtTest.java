
package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IfStmtTest {

    @Test
    void trueConditionSelectsThenBranch() throws MyException {
        IStmt thenStmt = new NopStmt();
        IStmt elseStmt = new NopStmt();

        IStmt program = new IfStmt(
                new ValueExp(new BoolValue(true)),
                thenStmt,
                elseStmt
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getExeStack().pop().execute(state);

        assertSame(thenStmt, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void falseConditionSelectsElseBranch() throws MyException {
        IStmt thenStmt = new NopStmt();
        IStmt elseStmt = new NopStmt();

        IStmt program = new IfStmt(
                new ValueExp(new BoolValue(false)),
                thenStmt,
                elseStmt
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getExeStack().pop().execute(state);

        assertSame(elseStmt, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void integerConditionThrowsException() {
        IStmt program = new IfStmt(
                new ValueExp(new IntValue(10)),
                new NopStmt(),
                new NopStmt()
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        assertThrows(
                StatementException.class,
                () -> state.getExeStack().pop().execute(state)
        );

        assertTrue(state.getExeStack().isEmpty());
    }
}
