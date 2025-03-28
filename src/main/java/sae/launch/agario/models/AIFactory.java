package sae.launch.agario.models;

public class AIFactory implements EntityFactory {
    /**
     * Creates a new AI
     * @param id    Id
     * @param x     Coordinate x
     * @param y     Coordinate y
     * @param mass  Mass of th Ai
     * @return  a new instance of AI
     */
    @Override
    public AI factory(int id, double x, double y, double mass) {
        return new AI(id,x,y,mass, null, null);
    }
}
