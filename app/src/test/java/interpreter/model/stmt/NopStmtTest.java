
package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NopStmtTest {

    @Test
    void nopDoesNotModifyProgramState() throws MyException {
        IStmt program = new NopStmt();

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        PrgState result =
                state.getExeStack().pop().execute(state);

        assertSame(state, result);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(0, state.getOut().size());
        assertFalse(state.getSymTable().isDefined("x"));
    }

    @Test
    void nopToStringWorks() {
        IStmt statement = new NopStmt();

        assertEquals("nop", statement.toString());
    }
}
