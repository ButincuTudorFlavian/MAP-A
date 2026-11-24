
package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.NewStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadHeapExpTest {

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
    void readsValueFromHeap() throws MyException {
        new NewStmt(
                "v",
                new ValueExp(new IntValue(20))
        ).execute(state);

        ReadHeapExp exp = new ReadHeapExp(
                new VarExp("v")
        );

        assertEquals(
                new IntValue(20),
                exp.eval(state.getSymTable(), state.getHeap())
        );
    }

    @Test
    void nonReferenceExpressionThrows() {
        ReadHeapExp exp = new ReadHeapExp(
                new ValueExp(new IntValue(20))
        );

        assertThrows(
                ExpressionException.class,
                () -> exp.eval(
                        state.getSymTable(),
                        state.getHeap()
                )
        );
    }

    @Test
    void nullReferenceThrows() {
        ReadHeapExp exp = new ReadHeapExp(
                new VarExp("v")
        );

        assertThrows(
                ExpressionException.class,
                () -> exp.eval(
                        state.getSymTable(),
                        state.getHeap()
                )
        );
    }

    @Test
    void missingHeapAddressThrows() {
        ReadHeapExp exp = new ReadHeapExp(
                new ValueExp(
                        new RefValue(99, new IntType())
                )
        );

        assertThrows(
                ExpressionException.class,
                () -> exp.eval(
                        state.getSymTable(),
                        state.getHeap()
                )
        );
    }

    @Test
    void readsNestedReferences() throws MyException {
        new NewStmt(
                "v",
                new ValueExp(new IntValue(20))
        ).execute(state);

        new VarDeclStmt(
                "a",
                new RefType(new RefType(new IntType()))
        ).execute(state);

        new NewStmt(
                "a",
                new VarExp("v")
        ).execute(state);

        ReadHeapExp exp = new ReadHeapExp(
                new ReadHeapExp(
                        new VarExp("a")
                )
        );

        assertEquals(
                new IntValue(20),
                exp.eval(
                        state.getSymTable(),
                        state.getHeap()
                )
        );
    }
}
