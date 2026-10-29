
package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
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

class AssignStmtTest {

    @Test
    void assignmentUpdatesIntegerVariable() throws MyException {
        IStmt program = new AssignStmt(
                "a",
                new ValueExp(new IntValue(10))
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getSymTable().put("a", new IntValue(0));

        state.getExeStack().pop().execute(state);

        assertEquals(
                new IntValue(10),
                state.getSymTable().lookup("a")
        );
    }

    @Test
    void assignmentUpdatesBooleanVariable() throws MyException {
        IStmt program = new AssignStmt(
                "flag",
                new ValueExp(new BoolValue(true))
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getSymTable().put("flag", new BoolValue(false));

        state.getExeStack().pop().execute(state);

        assertEquals(
                new BoolValue(true),
                state.getSymTable().lookup("flag")
        );
    }

    @Test
    void assignmentToUndeclaredVariableThrowsException() {
        IStmt program = new AssignStmt(
                "x",
                new ValueExp(new IntValue(5))
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
    }

    @Test
    void assignmentWithWrongTypeThrowsException() {
        IStmt program = new AssignStmt(
                "a",
                new ValueExp(new BoolValue(true))
        );

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getSymTable().put("a", new IntValue(0));

        assertThrows(
                StatementException.class,
                () -> state.getExeStack().pop().execute(state)
        );
    }

    @Test
    void assignmentCanUseAnotherVariable() throws MyException {
        IStmt program = new AssignStmt("b", new VarExp("a"));

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getSymTable().put("a", new IntValue(25));
        state.getSymTable().put("b", new IntValue(0));

        state.getExeStack().pop().execute(state);

        assertEquals(
                new IntValue(25),
                state.getSymTable().lookup("b")
        );
    }
}
