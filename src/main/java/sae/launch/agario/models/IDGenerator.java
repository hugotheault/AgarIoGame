package sae.launch.agario.models;

public class IDGenerator {

    private static IDGenerator generator;
    private int IDcounter;

    private void IDGenerator(){
        this.IDcounter = 0;
    }

    public int NextID(){
        IDcounter+=1;
        return IDcounter;
    }

    public static IDGenerator getGenerator() {
        if(generator == null){
            generator = new IDGenerator();
        }
        return generator;
    }

}
