package agentic.workflow;

import agentic.workflow.llm.StructuredOutput;

public class WorkflowStep{
    private String name;
    private String prompt;
    private String systemPrompt;
    private StructuredOutput structuredOutput;

    public WorkflowStep(String name, String prompt, String systemPrompt, StructuredOutput structuredOutput){
        if(name.isEmpty() || prompt.isEmpty() || systemPrompt.isEmpty() || structuredOutput == null)
        {
            throw new IllegalArgumentException("a `name`, `prompt` és `systemPrompt` nem lehet üres, a `structuredOutput` pedig nem lehet `null`.");
        }

        this.name = name;
        this.prompt = prompt;
        this.systemPrompt = systemPrompt;
        this.structuredOutput = structuredOutput;
    }

    public StructuredOutput getStructuredOutput(){
        return this.structuredOutput;
    }

    public void setStructuredOutput(StructuredOutput structuredOutput) throws IllegalArgumentException{
        if(structuredOutput == null || structuredOutput.size() == 0){
            throw new IllegalArgumentException();
        }
        this.structuredOutput = structuredOutput;
    }

    public boolean expectsStructuredOutput(){
        return null != structuredOutput && structuredOutput.size() >= 1; 
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        if(name == null){
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public String getPrompt(){
        return this.prompt;
    }

    public void setPrompt(String prompt){
        if(prompt == null){
            throw new IllegalArgumentException();
        }
        this.prompt = prompt;
    }

    public String getSystemPrompt(){
        return this.systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt){
        if(systemPrompt == null){
            throw new IllegalArgumentException();
        }
        this.systemPrompt = systemPrompt;
    }

    public String simulateResponse(){
        if(structuredOutput == null || structuredOutput.size() == 0){
            return "";
        }

        switch(structuredOutput.getSchemaTypes()[0]){
            case INT:
                return "0";
            case STRING:
                return "sample";
            case BOOLEAN:
                return "true";
            case LIST_INT:
                return "[1,2,3]";
            case LIST_STRING:
                return "[\"a\",\"b\"]";
            case MAP_STRING_STRING:
                return "{\"kulcs\":\"érték\"}";
            default:
                return "";
        }
    }

}