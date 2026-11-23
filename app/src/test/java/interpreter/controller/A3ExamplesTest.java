
package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class A3ExamplesTest {

    @TempDir
    Path dir;

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);

        Repository repo = new Repository(
                state,
                dir.resolve("log.txt").toString()
        );

        Controller controller = new Controller(repo, false);
        controller.allStep();

        return state;
    }

    @Test
    void example4ReadsTwoNumbers() throws Exception {
        Path file = dir.resolve("numbers.in");
        Files.writeString(file, "15\n50\n");

        PrgState state = run(
                Examples.fileExample(file.toString())
        );

        assertEquals(
                List.of(new IntValue(15), new IntValue(50)),
                state.getOut().toList()
        );

        assertTrue(state.getExeStack().isEmpty());
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void example4EmptyFilePrintsTwoZeros() throws Exception {
        Path file = dir.resolve("empty.in");
        Files.writeString(file, "");

        PrgState state = run(
                Examples.fileExample(file.toString())
        );

        assertEquals(
                List.of(new IntValue(0), new IntValue(0)),
                state.getOut().toList()
        );

        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void example4MissingFileThrows() {
        Path missing = dir.resolve("missing.in");

        assertThrows(
                StatementException.class,
                () -> run(
                        Examples.fileExample(missing.toString())
                )
        );
    }

    @Test
    void example6PrintsFive() throws Exception {
        PrgState state = run(Examples.EX6);

        assertEquals(
                List.of(new IntValue(5)),
                state.getOut().toList()
        );

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void example4WritesLog() throws Exception {
        Path file = dir.resolve("numbers.in");
        Files.writeString(file, "15\n50\n");

        run(Examples.fileExample(file.toString()));

        String log = Files.readString(dir.resolve("log.txt"));

        assertTrue(log.contains("ExeStack:"));
        assertTrue(log.contains("SymTable:"));
        assertTrue(log.contains("Out:"));
        assertTrue(log.contains("FileTable:"));
    }
}