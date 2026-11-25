
package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.ReadHeapExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NewStmt;
import interpreter.model.stmt.NopStmt;
import interpreter.model.stmt.PrintStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import interpreter.repository.Repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GarbageCollectorTest {

    @TempDir
    Path tempDir;

    @Test
    void extractsAddressesFromSymTable() {

        List<IValue> values = List.of(
                new RefValue(3, new IntType()),
                new IntValue(0),
                new RefValue(
                        2,
                        new RefType(new IntType())
                )
        );

        Set<Integer> addresses =
                GarbageCollector.getAddrFromSymTable(values);

        assertEquals(
                Set.of(2, 3),
                addresses
        );
    }

    @Test
    void unsafeCollectorLosesIndirectReference() {

        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(20),
                2, new RefValue(1, new IntType()),
                3, new IntValue(30),
                4, new IntValue(7)
        );

        Set<Integer> roots = Set.of(2, 3);

        Map<Integer, IValue> kept =
                GarbageCollector.unsafeGarbageCollector(
                        roots,
                        heap
                );

        assertEquals(
                Set.of(2, 3),
                kept.keySet()
        );

        assertFalse(kept.containsKey(1));
    }

    @Test
    void safeCollectorKeepsIndirectReference() {

        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(20),
                2, new RefValue(1, new IntType()),
                3, new IntValue(30),
                4, new IntValue(7)
        );

        Set<Integer> roots = Set.of(2, 3);

        Map<Integer, IValue> kept =
                GarbageCollector.safeGarbageCollector(
                        roots,
                        heap
                );

        assertEquals(
                Set.of(1, 2, 3),
                kept.keySet()
        );

        assertEquals(
                new IntValue(20),
                kept.get(1)
        );

        assertFalse(kept.containsKey(4));
    }

    @Test
    void lab8ExerciseKeepsAddressesTwoAndThree() {

        List<IValue> symTableValues = List.of(
                new RefValue(
                        3,
                        new RefType(new IntType())
                ),
                new IntValue(0)
        );

        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(7),
                2, new IntValue(8),
                3, new RefValue(2, new IntType()),
                4, new RefValue(1, new IntType()),
                5, new RefValue(
                        4,
                        new RefType(new IntType())
                )
        );

        Set<Integer> roots =
                GarbageCollector.getAddrFromSymTable(
                        symTableValues
                );

        Map<Integer, IValue> unsafe =
                GarbageCollector.unsafeGarbageCollector(
                        roots,
                        heap
                );

        Map<Integer, IValue> safe =
                GarbageCollector.safeGarbageCollector(
                        roots,
                        heap
                );

        assertEquals(
                Set.of(3),
                unsafe.keySet()
        );

        assertEquals(
                Set.of(2, 3),
                safe.keySet()
        );
    }

    @Test
    void noReferencesMeansEmptyHeap() {

        Map<Integer, IValue> heap = Map.of(
                1, new IntValue(10),
                2, new IntValue(20)
        );

        Map<Integer, IValue> result =
                GarbageCollector.safeGarbageCollector(
                        Set.of(),
                        heap
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void reachableAddressesTerminatesWithCycle() {

        Map<Integer, IValue> heap = Map.of(
                1, new RefValue(2, new IntType()),
                2, new RefValue(1, new IntType()),
                3, new IntValue(99)
        );

        Set<Integer> reachable =
                GarbageCollector.reachableAddresses(
                        Set.of(1),
                        heap
                );

        assertEquals(
                Set.of(1, 2),
                reachable
        );
    }

    @Test
    void ex9PrintsTwentyWithSafeGarbageCollector()
            throws MyException {

        IStmt program = sequence(
                new VarDeclStmt(
                        "v",
                        new RefType(new IntType())
                ),
                new NewStmt(
                        "v",
                        new ValueExp(new IntValue(20))
                ),
                new VarDeclStmt(
                        "a",
                        new RefType(
                                new RefType(new IntType())
                        )
                ),
                new NewStmt(
                        "a",
                        new VarExp("v")
                ),
                new NewStmt(
                        "v",
                        new ValueExp(new IntValue(30))
                ),
                new PrintStmt(
                        new ReadHeapExp(
                                new ReadHeapExp(
                                        new VarExp("a")
                                )
                        )
                )
        );

        PrgState state = PrgState.of(program);

        Repository repo = new Repository(
                state,
                tempDir.resolve("ex9.log").toString()
        );

        Controller controller = new Controller(repo, false);

        controller.allStep();

        assertEquals(
                List.of(new IntValue(20)),
                state.getOut().toList()
        );

        assertEquals(
                Set.of(1, 2, 3),
                state.getHeap().getContent().keySet()
        );

        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void logsHeapBeforeAndAfterCollection()
            throws MyException, IOException {

        IStmt program = sequence(
                new VarDeclStmt(
                        "v",
                        new RefType(new IntType())
                ),
                new NewStmt(
                        "v",
                        new ValueExp(new IntValue(20))
                ),
                new NewStmt(
                        "v",
                        new ValueExp(new IntValue(30))
                )
        );

        PrgState state = PrgState.of(program);

        Path logFile = tempDir.resolve("gc.log");

        Repository repo = new Repository(
                state,
                logFile.toString()
        );

        Controller controller = new Controller(repo, false);

        controller.allStep();

        assertEquals(
                Set.of(2),
                state.getHeap().getContent().keySet()
        );

        String log = Files.readString(logFile);

        assertTrue(
                log.contains(
                        "Heap:\n1 --> 20\n2 --> 30"
                )
        );

        assertTrue(
                log.contains(
                        "Heap:\n2 --> 30"
                )
        );
    }

    private static IStmt sequence(IStmt... statements) {

        if (statements.length == 0) {
            return new NopStmt();
        }

        IStmt result = statements[statements.length - 1];

        for (int i = statements.length - 2; i >= 0; i--) {
            result = new CompStmt(
                    statements[i],
                    result
            );
        }

        return result;
    }
}
