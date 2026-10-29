
package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.*;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private PrgState createState(IStmt program) {

        return new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                program
        );
    }

    @Test
    void oneStepExecutesSingleStatement() throws MyException {

        IStmt program = new VarDeclStmt(
                "a",
                new IntType()
        );

        PrgState state = createState(program);

        Controller controller = new Controller(
                new Repository(state),
                false
        );

        PrgState result = controller.oneStep(state);

        assertSame(state, result);

        assertEquals(
                new IntValue(0),
                state.getSymTable().lookup("a")
        );

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void oneStepOnEmptyStackThrowsException() throws MyException {

        PrgState state = createState(new NopStmt());

        Controller controller = new Controller(
                new Repository(state),
                false
        );

        state.getExeStack().pop();

        assertThrows(
                StatementException.class,
                () -> controller.oneStep(state)
        );
    }

    @Test
    void allStepExecutesCompleteProgram() throws MyException {

        IStmt program = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt(
                                "v",
                                new ValueExp(new IntValue(2))
                        ),
                        new PrintStmt(new VarExp("v"))
                )
        );

        PrgState state = createState(program);

        Controller controller = new Controller(
                new Repository(state),
                false
        );

        controller.allStep();

        assertTrue(state.getExeStack().isEmpty());

        assertEquals(
                new IntValue(2),
                state.getSymTable().lookup("v")
        );

        assertEquals(1, state.getOut().size());

        assertEquals(
                new IntValue(2),
                state.getOut().get(0)
        );
    }
}
