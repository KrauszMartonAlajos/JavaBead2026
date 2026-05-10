package agentic.workflow;

import agentic.workflow.WorkflowStep;
import agentic.workflow.llm.StructuredOutput;
import agentic.workflow.llm.SchemaType;
import java.util.List;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Agent{
    private String name;
    private final List<WorkflowStep> steps;

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<WorkflowStep> getSteps(){
        return this.steps; //clone kéne !!!
    }

    public Agent(String name){
        if(name.isEmpty()){
            throw new IllegalArgumentException("a név nem lehet `null`, üres vagy csak szóközökből álló.");
        }
        this.name = name;
        this.steps = new ArrayList<WorkflowStep>();
    }

    private boolean containsStepName(WorkflowStep step){
        for(int i = 0;i<steps.size();i++)
        {
            if(steps.get(i).getName().equals(step.getName())){
                return true;
            }
        }
        return false;
    }

    public void addStep(WorkflowStep step){
        if(step == null){
            throw new IllegalArgumentException("a lépés nem lehet `null`, és nem létezhet már másik lépés ugyanazzal a névvel.");
        }
        if(containsStepName(step) == false){
            steps.add(step);
        }
        else{
            throw new IllegalArgumentException("a lépés nem lehet `null`, és nem létezhet már másik lépés ugyanazzal a névvel.");
        }
    }

    public int getStepCount(){
        return steps.size();
    }

    public WorkflowStep findStepByName(String stepName){
        if(stepName == null || stepName.strip().isEmpty()){
            throw new IllegalArgumentException("a lépés neve nem lehet `null`, üres vagy csak szóközökből álló.");
        }
        for(int i = 0;i<steps.size();i++){
            if(steps.get(i).getName().equals(stepName.strip())){
                return steps.get(i);
            }
        }
        return null;
    }

    public void run(){
        for(int i = 0;i<steps.size(); i++){
            System.out.println(steps.get(i).getName());
            System.out.println(steps.get(i).getStructuredOutput());
        }
    }
    

    public static Agent loadAgent(String filename) throws IOException, WorkflowFormatException{
        if(filename == null || filename.strip().isEmpty()){
            throw new IllegalArgumentException("a fájlnév nem lehet `null`, üres vagy csak szóközökből álló.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line = reader.readLine();

            if(line == null){
                throw new WorkflowFormatException("a fájl tartalma hibás formátumú.");
            }

            String[] headerParts = line.split(":", 2);
            if(headerParts.length != 2 || !headerParts[0].strip().equals("AGENT") || headerParts[1].strip().isEmpty()){
                throw new WorkflowFormatException("a fájl tartalma hibás formátumú.");
            }

            Agent agent = new Agent(headerParts[1].strip());
            while(true){
                WorkflowStep step = parseStep(reader);
                if(step == null){
                    break;
                }
                if(agent.findStepByName(step.getName()) != null){
                    throw new WorkflowFormatException("a fájl tartalma hibás formátumú.");
                }
                agent.addStep(step);
            }
            return agent;
        }
    }

    private static WorkflowStep parseStep(BufferedReader reader) throws IOException, WorkflowFormatException{
        String line = reader.readLine();
        
        if(line == null){
            return null;
        }

        if(!line.strip().equals("STEP")){
            throw new WorkflowFormatException("a lépés tartalma hibás vagy hiányos. x");
        }

        String name = null;
        String prompt = null;
        String systemPrompt = null;
        SchemaType outputType = null;

        while((line = reader.readLine()) != null){
            String trimmed = line.strip();
            if(trimmed.isEmpty()){
                continue;
            }
            if(trimmed.equals("ENDSTEP")){
                break;
            }

            String[] parts = trimmed.split("=", 2);
            if(parts.length != 2){
                throw new WorkflowFormatException("a lépés tartalma hibás vagy hiányos. xx");
            }

            String key = parts[0].strip();
            String value = parts[1].strip();
            switch(key){
                case "name":
                    name = value;
                    break;
                case "prompt":
                    prompt = value;
                    break;
                case "systemPrompt":
                    systemPrompt = value;
                    break;
                case "output":
                    try{
                        outputType = SchemaType.valueOf(value);
                    }
                    catch(IllegalArgumentException exception){
                        throw new WorkflowFormatException("a lépés tartalma hibás vagy hiányos. xxx");
                    }
                    break;
                default:
                    throw new WorkflowFormatException("a lépés tartalma hibás vagy hiányos. xxxx");
            }
        }

        if(line == null || name == null || prompt == null || systemPrompt == null || outputType == null){
            throw new WorkflowFormatException("a lépés tartalma hibás vagy hiányos. xxxxx");
        }

        return new WorkflowStep(name, prompt, systemPrompt, new StructuredOutput(new SchemaType[]{outputType}));
    }

    //Ezek kellenek hogy meglegyen minden kivéve amihez nekem kell tesztet írni
}