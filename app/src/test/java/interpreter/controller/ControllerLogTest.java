
package interpreter.controller;

import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ControllerLogTest {

    @TempDir
    Path dir;

    @Test
    void logContainsAllFourSectionsInOrder() throws Exception {

        Path logFile = dir.resolve("log.txt");

        PrgState state = PrgState.of(Examples.EX1);
        Repository repo = new Repository(
                state, logFile.toString()
        );

        Controller controller = new Controller(repo, false);
        controller.allStep();

        String log = Files.readString(logFile);

        int a = log.indexOf("ExeStack:");
        int b = log.indexOf("SymTable:");
        int c = log.indexOf("Out:");
        int d = log.indexOf("FileTable:");

        assertTrue(a >= 0);
        assertTrue(a < b);
        assertTrue(b < c);
        assertTrue(c < d);
    }

    @Test
    void logContainsInitialAndFinalStates() throws Exception {

        Path logFile = dir.resolve("log.txt");

        PrgState state = PrgState.of(new NopStmt());
        Repository repo = new Repository(
                state, logFile.toString()
        );

        new Controller(repo, false).allStep();

        String log = Files.readString(logFile);

        long stateCount = log.lines()
                .filter(line -> line.equals("ExeStack:"))
                .count();

        // Starea inițială + starea după nop.
        assertEquals(2, stateCount);

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void logContainsExecutionResults() throws Exception {

        Path logFile = dir.resolve("log.txt");

        PrgState state = PrgState.of(Examples.EX1);
        Repository repo = new Repository(
                state, logFile.toString()
        );

        new Controller(repo, false).allStep();

        String log = Files.readString(logFile);

        assertTrue(log.contains("v --> 2"));
        assertTrue(log.contains("Out:\n2"));

        assertEquals(
                new IntValue(2),
                state.getOut().get(0)
        );
    }

    @Test
    void invalidLogPathThrowsRepositoryException() {

        Path invalidPath = dir.resolve("missing")
                .resolve("log.txt");

        PrgState state = PrgState.of(new NopStmt());

        Repository repo = new Repository(
                state, invalidPath.toString()
        );

        assertThrows(
                interpreter.exceptions.RepositoryException.class,
                repo::logPrgStateExec
        );
    }
}
