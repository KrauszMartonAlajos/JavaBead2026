package agentic.workflow.llm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

//testWith(testCase("testContainsExistingType"), "Ellenőrzi, hogy a benne levő sématípus megtalálható.")
//testWith(testCase("testContainsMissingType"), "Ellenőrzi, hogy a nem szereplő sématípus nem található meg.");
//testWith(testCase("testSize"), "Ellenőrzi, hogy a méret megegyezik a konstruktor argumentumainak számával.");


public class StructuredOutputTest {
	@Test
	public void testContainsExistingType(){
		SchemaType[] types = {SchemaType.STRING, SchemaType.INT, SchemaType.BOOLEAN};
		StructuredOutput output = new StructuredOutput(types);
		
		assertTrue(output.contains(SchemaType.STRING));
		assertTrue(output.contains(SchemaType.INT));
		assertTrue(output.contains(SchemaType.BOOLEAN));
	}

    @Test
    public void testContainsMissingType(){
        SchemaType[] types = {SchemaType.STRING, SchemaType.INT, SchemaType.BOOLEAN};
        StructuredOutput output = new StructuredOutput(types);

        assertFalse(output.contains(SchemaType.LIST_INT));
        assertFalse(output.contains(SchemaType.LIST_STRING));
        assertFalse(output.contains(SchemaType.MAP_STRING_STRING));
    }

    @Test
    public void testSize(){
        SchemaType[] types3 = {SchemaType.STRING, SchemaType.INT, SchemaType.BOOLEAN}; //3 hosszú
        SchemaType[] types2 = {SchemaType.INT, SchemaType.BOOLEAN}; //2 hosszú
        SchemaType[] types1 = {SchemaType.BOOLEAN}; //1 hosszú
        SchemaType[] types0 = {}; //0 hosszú

        StructuredOutput output3 = new StructuredOutput(types3);
        StructuredOutput output2 = new StructuredOutput(types2);
        StructuredOutput output1 = new StructuredOutput(types1);

        assertEquals(output3.size(),3);
        assertEquals(output2.size(),2);
        assertEquals(output1.size(),1);
        assertThrows(IllegalArgumentException.class, () -> new StructuredOutput(types0)); //ez hibát kell dobjon mert létre se jöhet öres tömbbel
    }
}