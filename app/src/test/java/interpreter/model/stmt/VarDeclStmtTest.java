
package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.type.IntType;
import interpreter.model.type.BoolType;
import interpreter.model.value.IntValue;
import interpreter.model.value.BoolValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarDeclStmtTest {

    @Test
    void integerVariableGetsDefaultValue() throws MyException {
        IStmt program = new VarDeclStmt("a", new IntType());

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getExeStack().pop().execute(state);

        assertTrue(state.getSymTable().isDefined("a"));

        assertEquals(
                new IntValue(0),
                state.getSymTable().lookup("a")
        );
    }

    @Test
    void booleanVariableGetsDefaultValue() throws MyException {
        IStmt program = new VarDeclStmt("flag", new BoolType());

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getExeStack().pop().execute(state);

        assertEquals(
                new BoolValue(false),
                state.getSymTable().lookup("flag")
        );
    }

    @Test
    void declaringSameVariableTwiceThrowsException()
            throws MyException {

        IStmt program = new VarDeclStmt("a", new IntType());

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );

        state.getExeStack().pop().execute(state);

        IStmt secondDeclaration =
                new VarDeclStmt("a", new IntType());

        assertThrows(
                StatementException.class,
                () -> secondDeclaration.execute(state)
        );
    }
}
