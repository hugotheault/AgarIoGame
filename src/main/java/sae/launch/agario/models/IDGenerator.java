package sae.launch.agario.models;

public class IDGenerator {

    private static IDGenerator generator;
    private int IDcounter;

   private IDGenerator(){
       IDcounter = 1;
   }
    public static IDGenerator getGenerator(){
        if( generator == null){
            generator = new IDGenerator();
        }
        return generator;
    }

    public int NextID(){
       return IDcounter++ ;
    }


}
