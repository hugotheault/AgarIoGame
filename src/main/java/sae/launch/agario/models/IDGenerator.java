package sae.launch.agario.models;

public class IDGenerator {

    private static IDGenerator generator;
    private int IDcounter;

    private IDGenerator() {

        this.IDcounter = 0;
        generator = this;
    }

    public int NextID() {
        IDcounter += 1;
        return IDcounter;
    }

    public static IDGenerator getGenerator() {
        if (generator == null) {
            return new IDGenerator();
        }
        return generator;
    }
}