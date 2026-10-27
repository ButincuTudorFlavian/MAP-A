
package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {

    private IDictionary<String, Integer> dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new MyDictionary<>();
    }

    @Test
    void newDictionaryHasNoKeys() {
        assertFalse(dictionary.isDefined("a"));
        assertFalse(dictionary.isDefined("b"));
    }

    @Test
    void putMakesKeyAvailable() {
        dictionary.put("a", 10);

        assertTrue(dictionary.isDefined("a"));
    }

    @Test
    void lookupReturnsCorrectValue() throws ADTException {
        dictionary.put("a", 10);

        int result = dictionary.lookup("a");

        assertEquals(10, result);
    }

    @Test
    void differentKeysHaveDifferentValues() throws ADTException {
        dictionary.put("a", 10);
        dictionary.put("b", 20);
        dictionary.put("c", 30);

        assertEquals(10, dictionary.lookup("a"));
        assertEquals(20, dictionary.lookup("b"));
        assertEquals(30, dictionary.lookup("c"));
    }

    @Test
    void updateChangesExistingValue() throws ADTException {
        dictionary.put("a", 10);

        dictionary.update("a", 50);

        assertEquals(50, dictionary.lookup("a"));
    }

    @Test
    void updateDoesNotModifyOtherKeys() throws ADTException {
        dictionary.put("a", 10);
        dictionary.put("b", 20);

        dictionary.update("a", 100);

        assertEquals(100, dictionary.lookup("a"));
        assertEquals(20, dictionary.lookup("b"));
    }

    @Test
    void lookupMissingKeyThrowsException() {
        assertThrows(
                ADTException.class,
                () -> dictionary.lookup("missing")
        );
    }

    @Test
    void updateMissingKeyThrowsException() {
        assertThrows(
                ADTException.class,
                () -> dictionary.update("missing", 42)
        );
    }

    @Test
    void dictionarySupportsDifferentGenericTypes() throws ADTException {
        IDictionary<Integer, String> names = new MyDictionary<>();

        names.put(1, "Ana");
        names.put(2, "Maria");

        assertEquals("Ana", names.lookup(1));
        assertEquals("Maria", names.lookup(2));
    }

    @Test
    void repeatedLookupReturnsSameValue() throws ADTException {
        dictionary.put("counter", 15);

        assertEquals(15, dictionary.lookup("counter"));
        assertEquals(15, dictionary.lookup("counter"));
        assertEquals(15, dictionary.lookup("counter"));
    }

    @Test
    void toStringContainsStoredEntries() {
        dictionary.put("a", 10);
        dictionary.put("b", 20);

        String result = dictionary.toString();

        assertTrue(result.contains("a=10"));
        assertTrue(result.contains("b=20"));
    }
}
