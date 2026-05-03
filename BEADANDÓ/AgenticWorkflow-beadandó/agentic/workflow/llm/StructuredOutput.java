package agentic.workflow.llm;

//import agentic.workflow.llm.SchemaType;

public class StructuredOutput{
    private final SchemaType[] schemaTypes;

    public SchemaType[] getSchemaTypes(){
        return this.schemaTypes.clone();
    }

    public StructuredOutput(SchemaType[] schemaTypes)
    {
        if(schemaTypes.length == 0){
            throw new IllegalArgumentException();
        }
        for(int i = 0;i<schemaTypes.length;i++)
        {
            if(schemaTypes[i] == null)
            {
                throw new NullPointerException();
            }
        }
        this.schemaTypes = schemaTypes;
    }

    public boolean contains(SchemaType schemaType){
        if(schemaType == null){
            throw new IllegalArgumentException();
        }
        for(int i = 0;i<schemaTypes.length;i++)
        {
            if(schemaType == schemaTypes[i]){
                return true;
            }
        }
        return false;
    }

    public int size(){
        return schemaTypes.length;
    }
}