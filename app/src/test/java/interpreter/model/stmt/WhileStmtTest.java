
package interpreter.model.stmt;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.*;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WhileStmtTest {

    @TempDir
    Path tempDir;

    private PrgState runLoop(int start) throws MyException {

        IStmt body = new CompStmt(
                new PrintStmt(new VarExp("v")),
                new AssignStmt(
                        "v",
                        new ArithExp(
                                ArithOp.SUB,
                                new VarExp("v"),
                                new ValueExp(new IntValue(1))
                        )
                )
        );

        IStmt loop = new WhileStmt(
                new RelationalExp(
                        RelOp.GT,
                        new VarExp("v"),
                        new ValueExp(new IntValue(0))
                ),
                body
        );

        IStmt program = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new AssignStmt(
                                "v",
                                new ValueExp(new IntValue(start))
                        ),
                        new CompStmt(
                                loop,
                                new PrintStmt(new VarExp("v"))
                        )
                )
        );

        PrgState state = PrgState.of(program);

        Repository repo = new Repository(
                state,
                tempDir.resolve("while-log.txt").toString()
        );

        Controller controller = new Controller(repo, false);
        controller.allStep();

        return state;
    }

    @Test
    void whileRunsZeroTimes() throws MyException {
        PrgState state = runLoop(0);

        assertEquals(
                List.of(new IntValue(0)),
                state.getOut().toList()
        );

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void whileRunsOnce() throws MyException {
        PrgState state = runLoop(1);

        assertEquals(
                List.of(
                        new IntValue(1),
                        new IntValue(0)
                ),
                state.getOut().toList()
        );

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void whileRunsSeveralTimes() throws MyException {
        PrgState state = runLoop(4);

        assertEquals(
                List.of(
                        new IntValue(4),
                        new IntValue(3),
                        new IntValue(2),
                        new IntValue(1),
                        new IntValue(0)
                ),
                state.getOut().toList()
        );

        assertEquals(
                new IntValue(0),
                state.getSymTable().lookup("v")
        );

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void nonBooleanConditionThrowsException() {
        PrgState state = PrgState.of(new NopStmt());

        WhileStmt loop = new WhileStmt(
                new ValueExp(new IntValue(1)),
                new NopStmt()
        );

        assertThrows(
                StatementException.class,
                () -> loop.execute(state)
        );
    }

    @Test
    void trueConditionPushesBodyBeforeLoop()
            throws MyException {

        PrgState state = PrgState.of(new NopStmt());

        IStmt body = new PrintStmt(
                new ValueExp(new IntValue(10))
        );

        WhileStmt loop = new WhileStmt(
                new ValueExp(new BoolValue(true)),
                body
        );

        loop.execute(state);

        assertSame(
                body,
                state.getExeStack().pop()
        );

        assertSame(
                loop,
                state.getExeStack().pop()
        );
    }

    @Test
    void falseConditionDoesNotPushBody()
            throws MyException {

        PrgState state = PrgState.of(new NopStmt());

        WhileStmt loop = new WhileStmt(
                new ValueExp(new BoolValue(false)),
                new PrintStmt(
                        new ValueExp(new IntValue(10))
                )
        );

        int before = state.getExeStack().toList().size();

        loop.execute(state);

        assertEquals(
                before,
                state.getExeStack().toList().size()
        );
    }
}