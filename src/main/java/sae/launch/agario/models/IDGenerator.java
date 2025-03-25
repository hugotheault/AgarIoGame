package sae.launch.agario.models;

public class IDGenerator {

    private IDGenerator generator;
    private int IDcounter;

    private void IDGenerator(){
        this.IDcounter = 0;
    }

    public int NextID(){
        IDcounter+=1;
        return IDcounter;
    }

    public IDGenerator getGenerator() {
        return generator;
    }
}
