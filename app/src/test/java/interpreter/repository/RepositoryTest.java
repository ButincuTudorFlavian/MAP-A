
package interpreter.repository;

import interpreter.model.PrgState;
import interpreter.model.adt.MyStack;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.stmt.NopStmt;
import interpreter.model.value.IntValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {

    private PrgState initialState;
    private IRepository repository;

    @BeforeEach
    void setUp() {
        initialState = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                new NopStmt()
        );

        repository = new Repository(initialState);
    }

    @Test
    void repositoryStoresInitialProgramState() {
        assertNotNull(repository.getCrtPrg());
    }

    @Test
    void getCrtPrgReturnsCorrectState() {
        PrgState result = repository.getCrtPrg();

        assertSame(initialState, result);
    }

    @Test
    void repositoryPreservesProgramVariables() {
        initialState.getSymTable().put(
                "a",
                new IntValue(10)
        );

        PrgState result = repository.getCrtPrg();

        assertTrue(result.getSymTable().isDefined("a"));
    }

    @Test
    void repositoryPreservesOutput() {
        initialState.getOut().add(new IntValue(25));

        PrgState result = repository.getCrtPrg();

        assertEquals(1, result.getOut().size());
    }

    @Test
    void repositoryReturnsSameStateInstance() {
        PrgState first = repository.getCrtPrg();
        PrgState second = repository.getCrtPrg();

        assertSame(first, second);
    }
}
