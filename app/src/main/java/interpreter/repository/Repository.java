
package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {

    private final List<PrgState> programStates;
    private final String logFilePath;

    public Repository(
            PrgState initialState,
            String logFilePath
    ) {
        this.programStates = new ArrayList<>();
        this.programStates.add(initialState);
        this.logFilePath = logFilePath;
    }

    // Păstrăm constructorul vechi pentru A2.
    public Repository(PrgState initialState) {
        this(initialState, "log.txt");
    }

    @Override
    public PrgState getCrtPrg() {
        return programStates.get(0);
    }

    @Override
    public void logPrgStateExec() throws MyException {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(logFilePath, true)
        )) {
            writer.write(getCrtPrg().toLogString());
            writer.newLine();

        } catch (IOException e) {
            throw new RepositoryException(
                    "Cannot write log file "
                            + logFilePath
                            + ": " + e.getMessage()
            );
        }
    }
}
