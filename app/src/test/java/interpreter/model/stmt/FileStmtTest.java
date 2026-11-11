
package interpreter.model.stmt;

import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStmtTest {

    @TempDir
    Path dir;

    private PrgState state;
    private ValueExp file;
    private Path filePath;

    @BeforeEach
    void setUp() throws Exception {
        filePath = dir.resolve("numbers.in");
        Files.writeString(filePath, "15\n50\n");

        file = new ValueExp(
                new StringValue(filePath.toString())
        );

        state = PrgState.of(new NopStmt());

        new VarDeclStmt("x", new IntType()).execute(state);
    }

    @AfterEach
    void closeRemainingReaders() {
        for (BufferedReader reader :
                state.getFileTable().toMap().values()) {
            try {
                reader.close();
            } catch (IOException ignored) {
                // Test cleanup
            }
        }
    }

    @Test
    void openFileAddsReaderToFileTable() throws Exception {
        new OpenRFileStmt(file).execute(state);

        assertTrue(state.getFileTable().isDefined(
                new StringValue(filePath.toString())
        ));
    }

    @Test
    void readFileReadsNumbersAndThenZero() throws Exception {
        new OpenRFileStmt(file).execute(state);

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(
                new IntValue(15),
                state.getSymTable().lookup("x")
        );

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(
                new IntValue(50),
                state.getSymTable().lookup("x")
        );

        new ReadFileStmt(file, "x").execute(state);
        assertEquals(
                new IntValue(0),
                state.getSymTable().lookup("x")
        );
    }

    @Test
    void emptyFileReturnsZero() throws Exception {
        Files.writeString(filePath, "");

        new OpenRFileStmt(file).execute(state);
        new ReadFileStmt(file, "x").execute(state);

        assertEquals(
                new IntValue(0),
                state.getSymTable().lookup("x")
        );
    }

    @Test
    void closeFileRemovesReader() throws Exception {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);

        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void openingSameFileTwiceThrows() throws Exception {
        new OpenRFileStmt(file).execute(state);

        assertThrows(
                StatementException.class,
                () -> new OpenRFileStmt(file).execute(state)
        );
    }

    @Test
    void openingMissingFileThrows() {
        ValueExp missing = new ValueExp(
                new StringValue(
                        dir.resolve("missing.in").toString()
                )
        );

        assertThrows(
                StatementException.class,
                () -> new OpenRFileStmt(missing).execute(state)
        );
    }

    @Test
    void openingNonStringThrows() {
        ValueExp invalid = new ValueExp(new IntValue(5));

        assertThrows(
                StatementException.class,
                () -> new OpenRFileStmt(invalid).execute(state)
        );
    }

    @Test
    void readingUndeclaredVariableThrows() throws Exception {
        new OpenRFileStmt(file).execute(state);

        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(file, "missing")
                        .execute(state)
        );
    }

    @Test
    void readingNonIntegerVariableThrows() throws Exception {
        new VarDeclStmt("b", new BoolType()).execute(state);
        new OpenRFileStmt(file).execute(state);

        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(file, "b").execute(state)
        );
    }

    @Test
    void readingNonStringFileNameThrows() {
        ValueExp invalid = new ValueExp(new BoolValue(true));

        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(invalid, "x")
                        .execute(state)
        );
    }

    @Test
    void readingUnopenedFileThrows() {
        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state)
        );
    }

    @Test
    void readingInvalidNumberThrows() throws Exception {
        Files.writeString(filePath, "hello\n");

        new OpenRFileStmt(file).execute(state);

        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state)
        );
    }

    @Test
    void readingClosedReaderThrows() throws Exception {
        BufferedReader reader = new BufferedReader(
                new StringReader("7\n")
        );
        reader.close();

        StringValue name = new StringValue("closed.in");
        state.getFileTable().put(name, reader);

        assertThrows(
                StatementException.class,
                () -> new ReadFileStmt(
                        new ValueExp(name), "x"
                ).execute(state)
        );
    }

    @Test
    void closingNonStringFileNameThrows() {
        ValueExp invalid = new ValueExp(new IntValue(10));

        assertThrows(
                StatementException.class,
                () -> new CloseRFileStmt(invalid).execute(state)
        );
    }

    @Test
    void closingUnopenedFileThrows() {
        assertThrows(
                StatementException.class,
                () -> new CloseRFileStmt(file).execute(state)
        );
    }

    @Test
    void closingWithIOExceptionThrows() {
        BufferedReader brokenReader = new BufferedReader(
                new StringReader("")
        ) {
            @Override
            public void close() throws IOException {
                super.close();
                throw new IOException("Simulated close failure");
            }
        };

        StringValue name = new StringValue("broken.in");
        state.getFileTable().put(name, brokenReader);

        assertThrows(
                StatementException.class,
                () -> new CloseRFileStmt(
                        new ValueExp(name)
                ).execute(state)
        );

        assertTrue(state.getFileTable().isDefined(name));
    }
}
