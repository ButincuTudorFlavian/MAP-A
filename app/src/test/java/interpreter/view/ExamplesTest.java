
package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.repository.Repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class A4ExamplesTest {

    @TempDir
    Path tempDir;

    private PrgState runExample(
            IStmt program,
            String logName
    ) throws MyException {

        PrgState state = PrgState.of(program);

        Repository repo = new Repository(
                state,
                tempDir.resolve(logName).toString()
        );

        Controller controller = new Controller(repo, false);

        controller.allStep();

        assertTrue(state.getExeStack().isEmpty());

        return state;
    }

    @Test
    void ex1PrintsTwo() throws MyException {

        PrgState state = runExample(
                Examples.EX1,
                "ex1.log"
        );

        assertEquals(
                List.of(new IntValue(2)),
                state.getOut().toList()
        );

        assertEquals(
                new IntValue(2),
                state.getSymTable().lookup("v")
        );
    }

    @Test
    void ex2PrintsEighteen() throws MyException {

        PrgState state = runExample(
                Examples.EX2,
                "ex2.log"
        );

        assertEquals(
                List.of(new IntValue(18)),
                state.getOut().toList()
        );

        assertEquals(
                new IntValue(17),
                state.getSymTable().lookup("a")
        );

        assertEquals(
                new IntValue(18),
                state.getSymTable().lookup("b")
        );
    }

    @Test
    void ex3ExecutesThenBranch() throws MyException {

        PrgState state = runExample(
                Examples.EX3,
                "ex3.log"
        );

        assertEquals(
                List.of(new IntValue(2)),
                state.getOut().toList()
        );

        assertEquals(
                new BoolValue(true),
                state.getSymTable().lookup("a")
        );

        assertEquals(
                new IntValue(2),
                state.getSymTable().lookup("v")
        );
    }

    @Test
    void ex4ThrowsTypeMismatchException() {

        PrgState state = PrgState.of(Examples.EX4);

        Repository repo = new Repository(
                state,
                tempDir.resolve("ex4.log").toString()
        );

        Controller controller = new Controller(repo, false);

        assertThrows(
                StatementException.class,
                controller::allStep
        );

        assertTrue(state.getOut().toList().isEmpty());
    }

    @Test
    void ex5ReadsTwoNumbersFromFile()
            throws MyException, IOException {

        Path inputFile = tempDir.resolve("numbers.in");

        Files.writeString(
                inputFile,
                "15\n50\n"
        );

        PrgState state = runExample(
                Examples.fileExample(inputFile.toString()),
                "ex5.log"
        );

        assertEquals(
                List.of(
                        new IntValue(15),
                        new IntValue(50)
                ),
                state.getOut().toList()
        );

        assertEquals(
                new IntValue(50),
                state.getSymTable().lookup("varc")
        );

        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void ex6PrintsSmallerNumber() throws MyException {

        PrgState state = runExample(
                Examples.EX6,
                "ex6.log"
        );

        assertEquals(
                List.of(new IntValue(5)),
                state.getOut().toList()
        );
    }

    @Test
    void ex7PrintsTrueForNotEqual() throws MyException {

        PrgState state = runExample(
                Examples.EX7,
                "ex7.log"
        );

        assertEquals(
                List.of(new BoolValue(true)),
                state.getOut().toList()
        );

        assertEquals(
                new BoolValue(true),
                state.getSymTable().lookup("c")
        );
    }

    @Test
    void ex8AllocatesReferences() throws MyException {

        PrgState state = runExample(
                Examples.EX8,
                "ex8.log"
        );

        assertEquals(
                List.of(
                        new RefValue(1, new IntType()),
                        new RefValue(
                                2,
                                new RefType(new IntType())
                        )
                ),
                state.getOut().toList()
        );

        assertEquals(
                Set.of(1, 2),
                state.getHeap().getContent().keySet()
        );

        assertEquals(
                new IntValue(20),
                state.getHeap().get(1)
        );

        assertEquals(
                new RefValue(1, new IntType()),
                state.getHeap().get(2)
        );
    }

    @Test
    void ex9ReadsFromHeap() throws MyException {

        PrgState state = runExample(
                Examples.EX9,
                "ex9.log"
        );

        assertEquals(
                List.of(
                        new IntValue(20),
                        new IntValue(25)
                ),
                state.getOut().toList()
        );
    }

    @Test
    void ex10WritesToHeap() throws MyException {

        PrgState state = runExample(
                Examples.EX10,
                "ex10.log"
        );

        assertEquals(
                List.of(
                        new IntValue(20),
                        new IntValue(35)
                ),
                state.getOut().toList()
        );

        assertEquals(
                new IntValue(30),
                state.getHeap().get(1)
        );

        assertEquals(
                Set.of(1),
                state.getHeap().getContent().keySet()
        );
    }

    @Test
    void ex11KeepsIndirectReferences() throws MyException {

        PrgState state = runExample(
                Examples.EX11,
                "ex11.log"
        );

        assertEquals(
                List.of(new IntValue(20)),
                state.getOut().toList()
        );

        assertEquals(
                Set.of(1, 2, 3),
                state.getHeap().getContent().keySet()
        );

        assertEquals(
                new IntValue(20),
                state.getHeap().get(1)
        );

        assertEquals(
                new RefValue(1, new IntType()),
                state.getHeap().get(2)
        );

        assertEquals(
                new IntValue(30),
                state.getHeap().get(3)
        );
    }

    @Test
    void ex12RunsWhileLoop() throws MyException {

        PrgState state = runExample(
                Examples.EX12,
                "ex12.log"
        );

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
}
