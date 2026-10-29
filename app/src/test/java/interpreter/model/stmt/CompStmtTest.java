
package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompStmtTest {

    @Test
    void statementsArePushedInCorrectOrder() throws MyException {
        IStmt first = new NopStmt();
        IStmt second = new NopStmt();

        IStmt program = new CompStmt(first, second);

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getExeStack().pop().execute(state);

        assertSame(first, state.getExeStack().pop());
        assertSame(second, state.getExeStack().pop());

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void compoundStatementToStringWorks() {
        IStmt first = new NopStmt();
        IStmt second = new NopStmt();

        CompStmt statement = new CompStmt(first, second);

        assertEquals("(nop; nop)", statement.toString());
    }
}
