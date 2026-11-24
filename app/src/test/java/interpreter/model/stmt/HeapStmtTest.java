
package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapStmtTest {

    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());

        new VarDeclStmt(
                "v",
                new RefType(new IntType())
        ).execute(state);
    }

    @Test
    void newAllocatesAndUpdatesReference() throws MyException {
        new NewStmt(
                "v",
                new ValueExp(new IntValue(20))
        ).execute(state);

        assertEquals(
                new RefValue(1, new IntType()),
                state.getSymTable().lookup("v")
        );

        assertEquals(
                new IntValue(20),
                state.getHeap().get(1)
        );
    }

    @Test
    void newUsesNewAddressEveryTime() throws MyException {
        new NewStmt(
                "v", new ValueExp(new IntValue(20))
        ).execute(state);

        new NewStmt(
                "v", new ValueExp(new IntValue(30))
        ).execute(state);

        assertEquals(
                new RefValue(2, new IntType()),
                state.getSymTable().lookup("v")
        );

        assertEquals(new IntValue(20), state.getHeap().get(1));
        assertEquals(new IntValue(30), state.getHeap().get(2));
    }

    @Test
    void newWithUndeclaredVariableThrows() {
        assertThrows(
                StatementException.class,
                () -> new NewStmt(
                        "missing",
                        new ValueExp(new IntValue(20))
                ).execute(state)
        );
    }

    @Test
    void newWithNonReferenceVariableThrows()
            throws MyException {

        new VarDeclStmt("x", new IntType()).execute(state);

        assertThrows(
                StatementException.class,
                () -> new NewStmt(
                        "x",
                        new ValueExp(new IntValue(20))
                ).execute(state)
        );
    }

    @Test
    void newWithWrongTypeThrows() {
        assertThrows(
                StatementException.class,
                () -> new NewStmt(
                        "v",
                        new ValueExp(new BoolValue(true))
                ).execute(state)
        );

        assertTrue(state.getHeap().getContent().isEmpty());
    }

    @Test
    void newSupportsNestedReferences() throws MyException {
        new NewStmt(
                "v", new ValueExp(new IntValue(20))
        ).execute(state);

        new VarDeclStmt(
                "a",
                new RefType(new RefType(new IntType()))
        ).execute(state);

        new NewStmt("a", new VarExp("v")).execute(state);

        assertEquals(
                new RefValue(2, new RefType(new IntType())),
                state.getSymTable().lookup("a")
        );

        assertEquals(
                new RefValue(1, new IntType()),
                state.getHeap().get(2)
        );
    }

    @Test
    void writeHeapChangesExistingValue() throws MyException {
        new NewStmt(
                "v", new ValueExp(new IntValue(20))
        ).execute(state);

        new WriteHeapStmt(
                "v", new ValueExp(new IntValue(30))
        ).execute(state);

        assertEquals(
                new IntValue(30),
                state.getHeap().get(1)
        );

        assertEquals(
                new RefValue(1, new IntType()),
                state.getSymTable().lookup("v")
        );
    }

    @Test
    void writeHeapWithUndeclaredVariableThrows() {
        assertThrows(
                StatementException.class,
                () -> new WriteHeapStmt(
                        "missing",
                        new ValueExp(new IntValue(30))
                ).execute(state)
        );
    }

    @Test
    void writeHeapWithNonReferenceThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);

        assertThrows(
                StatementException.class,
                () -> new WriteHeapStmt(
                        "x",
                        new ValueExp(new IntValue(30))
                ).execute(state)
        );
    }

    @Test
    void writeHeapWithMissingAddressThrows() {
        assertThrows(
                StatementException.class,
                () -> new WriteHeapStmt(
                        "v",
                        new ValueExp(new IntValue(30))
                ).execute(state)
        );
    }

    @Test
    void writeHeapWithWrongTypeThrows() throws MyException {
        new NewStmt(
                "v", new ValueExp(new IntValue(20))
        ).execute(state);

        assertThrows(
                StatementException.class,
                () -> new WriteHeapStmt(
                        "v",
                        new ValueExp(new BoolValue(true))
                ).execute(state)
        );

        assertEquals(
                new IntValue(20),
                state.getHeap().get(1)
        );
    }
}
