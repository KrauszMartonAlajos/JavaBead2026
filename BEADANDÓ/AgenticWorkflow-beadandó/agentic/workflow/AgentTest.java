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

//.testWith(testCase("testStepCount"), "Ellenőrzi, hogy egy lépés hozzáadása után a tárolt lépések száma növekszik.")
//.testWith(testCase("testAddDuplicateStepRejected"), "Ellenőrzi, hogy azonos nevű lépések nem elfogadhatók.");
//.testWith(testCase("testStepCount"), "Ellenőrzi, hogy a lépésszám megegyezik a hozzáadott lépések számával.");

//.testWith(testCase("findStepByName"), "Ellenőrzi, hogy egy meglévő lépés név alapján megtalálható.")
//.testWith(testCase("findStepByNameMissing"), "Ellenőrzi, hogy hiányzó lépés esetén `null` az eredmény.");

//.testWith(testCase("testLoadAgentSuccess"), "Ellenőrzi, hogy egy érvényes workflow fájl sikeresen betölthető.")
//.testWith(testCase("testLoadAgentRejectsMissingHeader"), "Ellenőrzi, hogy a hiányzó AGENT fejléc formátumhibát okoz.")
//.testWith(testCase("testLoadAgentRejectsDuplicateStepNames"), "Ellenőrzi, hogy a fájlban lévő azonos lépésnevek nem elfogadhatók.");



public class AgentTest {
	@Test
	public void testExpectsStructuredOutput(){
		SchemaType[] types = {SchemaType.STRING, SchemaType.INT, SchemaType.BOOLEAN};
		StructuredOutput output = new StructuredOutput(types);

        WorkflowStep wfs = new WorkflowStep("step", "prompt", "systemprompt", output);

        Agent agent = new Agent("Ödön");

        int size1 = agent.getStepCount();

        agent.addStep(wfs); //+1 lépés

        int size2 = agent.getStepCount();
		
		assertTrue(size2 > size1);
        assertTrue(size2 == 1);
	}

    @Test    
    public void testAddDuplicateStepRejected(){
        SchemaType[] types = {SchemaType.STRING, SchemaType.INT, SchemaType.BOOLEAN};
		StructuredOutput output = new StructuredOutput(types);

        WorkflowStep wfs = new WorkflowStep("step", "prompt", "systemprompt", output);

        Agent agent = new Agent("Ödön");

        agent.addStep(wfs);

        assertThrows(IllegalArgumentException.class, () -> agent.addStep(wfs));
    }

    @Test 
    public void findStepByName(){
        SchemaType[] types = {SchemaType.STRING, SchemaType.INT, SchemaType.BOOLEAN};
		StructuredOutput output = new StructuredOutput(types);

        WorkflowStep wfs = new WorkflowStep("step", "prompt", "systemprompt", output);

        Agent agent = new Agent("Ödön");

        agent.addStep(wfs);

        assertEquals(agent.findStepByName("step"),wfs);
    }

    @Test
    public void findStepByNameMissing(){
        SchemaType[] types = {SchemaType.STRING, SchemaType.INT, SchemaType.BOOLEAN};
		StructuredOutput output = new StructuredOutput(types);

        //WorkflowStep wfs = new WorkflowStep("step", "prompt", "systemprompt", output);

        Agent agent_empty = new Agent("Benzines_Lancfűrész");

        //agent.addStep(wfs);

        assertEquals(agent_empty.findStepByName("step"),null);
    }

    @Test 
    public void testStepCount(){
        SchemaType[] types = {SchemaType.STRING, SchemaType.INT, SchemaType.BOOLEAN};
		StructuredOutput output = new StructuredOutput(types);

        WorkflowStep wfs = new WorkflowStep("step", "prompt", "systemprompt", output);

        Agent agent = new Agent("Ödön");

        agent.addStep(wfs);

        assertEquals(agent.getStepCount(), 1);
    }

    @Test
    public void testLoadAgentSuccess() throws Exception{
        Agent agent = Agent.loadAgent("test_workflow.txt");
        
        assertEquals(agent.getName(), "TestAgent");
        assertEquals(agent.getStepCount(), 2);
        
        WorkflowStep step1 = agent.findStepByName("step1");
        WorkflowStep step2 = agent.findStepByName("step2");
        
        assertTrue(step1 != null);
        assertTrue(step2 != null);
        assertEquals(step1.getName(), "step1");
        assertEquals(step2.getName(), "step2");
    }

    @Test
    public void testLoadAgentRejectsMissingHeader(){
        assertThrows(WorkflowFormatException.class, () -> {Agent.loadAgent("test_no_header.txt");});
    }

    @Test
    public void testLoadAgentRejectsDuplicateStepNames(){
        assertThrows(WorkflowFormatException.class, () -> {Agent.loadAgent("test_dupe.txt");});
    }

}