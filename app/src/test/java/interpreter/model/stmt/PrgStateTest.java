
package interpreter.model;

import interpreter.exceptions.MyException;
import interpreter.model.adt.*;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrgStateTest {

    @Test
    void constructorPushesOriginalProgram() throws MyException {
        IStmt program = new NopStmt();

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        assertFalse(state.getExeStack().isEmpty());

        assertSame(
                program,
                state.getExeStack().pop()
        );

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void initialSymbolTableAndOutputAreEmpty() {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new NopStmt()
        );

        assertFalse(state.getSymTable().isDefined("a"));
        assertEquals(0, state.getOut().size());
    }

    @Test
    void originalProgramIsPreserved() {
        IStmt program = new NopStmt();

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        assertSame(program, state.getOriginalProgram());
    }

    @Test
    void settersReplaceStateStructures() {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new NopStmt()
        );

        IStack<IStmt> newStack = new MyStack<>();
        IDictionary<String, IValue> newTable =
                new MyDictionary<>();
        IList<IValue> newOut = new MyList<>();

        state.setExeStack(newStack);
        state.setSymTable(newTable);
        state.setOut(newOut);

        assertSame(newStack, state.getExeStack());
        assertSame(newTable, state.getSymTable());
        assertSame(newOut, state.getOut());
    }

    @Test
    void toStringDisplaysProgramState() {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new NopStmt()
        );

        state.getSymTable().put("a", new IntValue(10));
        state.getOut().add(new IntValue(10));

        String result = state.toString();

        assertTrue(result.contains("ExeStack"));
        assertTrue(result.contains("SymTable"));
        assertTrue(result.contains("Out"));
        assertTrue(result.contains("10"));
    }
}
