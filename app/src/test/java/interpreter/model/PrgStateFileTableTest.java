
package interpreter.model;

import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.StringValue;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class PrgStateFileTableTest {

    @Test
    void factoryCreatesEmptyStructures() {
        NopStmt program = new NopStmt();

        PrgState state = PrgState.of(program);

        assertTrue(state.getSymTable().toMap().isEmpty());
        assertTrue(state.getOut().toList().isEmpty());
        assertTrue(state.getFileTable().toMap().isEmpty());

        // Programul inițial trebuie introdus în ExeStack
        assertEquals(1, state.getExeStack().toList().size());
        assertEquals(program, state.getExeStack().toList().get(0));
    }

    @Test
    void fileTableStoresReadersUsingStringValueKeys() throws Exception {
        PrgState state = PrgState.of(new NopStmt());

        try (BufferedReader reader =
                     new BufferedReader(new StringReader("15\n50\n"))) {

            state.getFileTable().put(
                    new StringValue("test.in"),
                    reader
            );

            assertTrue(
                    state.getFileTable().isDefined(
                            new StringValue("test.in")
                    )
            );

            assertSame(
                    reader,
                    state.getFileTable().lookup(
                            new StringValue("test.in")
                    )
            );

            state.getFileTable().remove(
                    new StringValue("test.in")
            );

            assertTrue(state.getFileTable().toMap().isEmpty());
        }
    }

    @Test
    void oldConstructorStillCreatesEmptyFileTable() {
        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new NopStmt()
        );

        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void differentProgramStatesHaveDifferentFileTables() {
        PrgState first = PrgState.of(new NopStmt());
        PrgState second = PrgState.of(new NopStmt());

        assertNotSame(
                first.getFileTable(),
                second.getFileTable()
        );
    }
}
