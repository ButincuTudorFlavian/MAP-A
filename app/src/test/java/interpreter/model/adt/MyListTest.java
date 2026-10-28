
package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {

    private IList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new MyList<>();
    }

    @Test
    void newListIsEmpty() {
        assertEquals(0, list.size());
    }

    @Test
    void addIncreasesSize() {
        list.add(10);

        assertEquals(1, list.size());

        list.add(20);

        assertEquals(2, list.size());
    }

    @Test
    void getReturnsCorrectElement() throws ADTException {
        list.add(10);
        list.add(20);
        list.add(30);

        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
    }

    @Test
    void elementsKeepInsertionOrder() throws ADTException {
        list.add(5);
        list.add(15);
        list.add(25);

        assertEquals(5, list.get(0));
        assertEquals(15, list.get(1));
        assertEquals(25, list.get(2));
    }

    @Test
    void invalidIndexThrowsException() {
        list.add(10);

        assertThrows(
                ADTException.class,
                () -> list.get(5)
        );
    }

    @Test
    void negativeIndexThrowsException() {
        list.add(10);

        assertThrows(
                ADTException.class,
                () -> list.get(-1)
        );
    }

    @Test
    void getFromEmptyListThrowsException() {
        assertThrows(
                ADTException.class,
                () -> list.get(0)
        );
    }

    @Test
    void listSupportsDifferentTypes() throws ADTException {
        IList<String> names = new MyList<>();

        names.add("Ana");
        names.add("Maria");

        assertEquals("Ana", names.get(0));
        assertEquals("Maria", names.get(1));
        assertEquals(2, names.size());
    }

    @Test
    void toStringDisplaysElements() {
        list.add(10);
        list.add(20);

        assertEquals("[10, 20]", list.toString());
    }
}
