package agentic.workflow;

import agentic.workflow.llm.SchemaType;
import agentic.workflow.llm.StructuredOutput;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

//.testWith(testCase("testExpectsStructuredOutput"), "Ellenőrzi, hogy a strukturált kimenettel rendelkező lépés esetén `true` a válasz.");

//expectsStructuredOutput
//public WorkflowStep(String name, String prompt, String systemPrompt, StructuredOutput structuredOutput){



public class WorkflowStepTest {
	@Test
	public void testExpectsStructuredOutput(){
		SchemaType[] types = {SchemaType.STRING, SchemaType.INT, SchemaType.BOOLEAN};
		StructuredOutput output = new StructuredOutput(types);

        WorkflowStep wfs = new WorkflowStep("step", "prompt", "systemprompt", output);
		
		assertTrue(wfs.expectsStructuredOutput());
	}
}