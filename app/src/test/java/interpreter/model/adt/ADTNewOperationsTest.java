
package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ADTNewOperationsTest {

    @Test
    void stackToListStartsWithTopElement() {
        MyStack<Integer> stack = new MyStack<>();

        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(
                List.of(30, 20, 10),
                stack.toList()
        );
    }

    @Test
    void stackToListDoesNotModifyStack() throws ADTException {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(10);
        stack.push(20);

        List<Integer> copy = stack.toList();
        copy.clear();

        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());
    }

    @Test
    void listToListKeepsInsertionOrder() {
        MyList<Integer> list = new MyList<>();

        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(
                List.of(10, 20, 30),
                list.toList()
        );
    }

    @Test
    void listToListReturnsCopy() throws ADTException {
        MyList<Integer> list = new MyList<>();
        list.add(5);

        List<Integer> copy = list.toList();
        copy.clear();

        assertEquals(1, list.size());
        assertEquals(5, list.get(0));
    }

    @Test
    void dictionaryRemovesExistingKey() throws ADTException {
        MyDictionary<String, Integer> dictionary =
                new MyDictionary<>();

        dictionary.put("a", 10);
        dictionary.put("b", 20);

        dictionary.remove("a");

        assertFalse(dictionary.isDefined("a"));
        assertTrue(dictionary.isDefined("b"));
    }

    @Test
    void dictionaryRemoveMissingKeyThrows() {
        MyDictionary<String, Integer> dictionary =
                new MyDictionary<>();

        assertThrows(
                ADTException.class,
                () -> dictionary.remove("missing")
        );
    }

    @Test
    void dictionaryToMapReturnsReadOnlyCopy() {
        MyDictionary<String, Integer> dictionary =
                new MyDictionary<>();

        dictionary.put("a", 10);

        Map<String, Integer> copy = dictionary.toMap();

        assertEquals(10, copy.get("a"));

        assertThrows(
                UnsupportedOperationException.class,
                () -> copy.put("b", 20)
        );

        dictionary.put("c", 30);

        assertFalse(copy.containsKey("c"));
    }
}
