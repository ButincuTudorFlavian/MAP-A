
package interpreter.view;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RunExampleCommandTest {

    @TempDir
    Path dir;

    @Test
    void exampleExecutesOnlyOnce() throws Exception {

        Path logFile = dir.resolve("log1.txt");

        PrgState state = PrgState.of(Examples.EX1);

        Repository repo = new Repository(
                state,
                logFile.toString()
        );

        Controller controller = new Controller(repo, false);

        RunExampleCommand command = new RunExampleCommand(
                "1",
                "Example 1",
                controller
        );

        command.execute();

        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));

        String firstLog = Files.readString(logFile);

        // Încercăm să executăm încă o dată.
        command.execute();

        String secondLog = Files.readString(logFile);

        // Nu trebuie să se mai execute sau să scrie în log.
        assertEquals(firstLog, secondLog);
        assertEquals(1, state.getOut().size());
    }
}
