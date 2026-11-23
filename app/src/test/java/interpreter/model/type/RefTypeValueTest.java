
package interpreter.model.type;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.value.RefValue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefTypeValueTest {

    @Test
    void referenceHasDefaultAddressZero() {
        RefType type = new RefType(new IntType());

        assertEquals(
                new RefValue(0, new IntType()),
                type.defaultValue()
        );
    }

    @Test
    void nestedReferenceTypesAreEqual() {
        RefType first = new RefType(
                new RefType(new IntType())
        );

        RefType second = new RefType(
                new RefType(new IntType())
        );

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void DifferentReferenceTypesAreNotEqual() {
        RefType simple = new RefType(new IntType());

        RefType nested = new RefType(
                new RefType(new IntType())
        );

        assertNotEquals(simple, nested);
    }

    @Test
    void referenceValueReturnsItsReferenceType() {
        RefValue value = new RefValue(
                1,
                new IntType()
        );

        assertEquals(
                new RefType(new IntType()),
                value.getType()
        );
    }

    @Test
    void declaringReferenceUsesDefaultValue() throws MyException {
        PrgState state = PrgState.of(new NopStmt());

        new VarDeclStmt(
                "v",
                new RefType(new IntType())
        ).execute(state);

        assertEquals(
                new RefValue(0, new IntType()),
                state.getSymTable().lookup("v")
        );
    }

    @Test
    void referenceToReferenceHasCorrectType() {
        RefValue value = new RefValue(
                2,
                new RefType(new IntType())
        );

        assertEquals(
                new RefType(new RefType(new IntType())),
                value.getType()
        );
    }
}
