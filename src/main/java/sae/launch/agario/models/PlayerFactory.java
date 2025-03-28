package sae.launch.agario.models;

public class PlayerFactory implements EntityFactory {
    /**
     * Creates a new Player
     * @param id    Id
     * @param x     Coordinate x
     * @param y     Coordinate y
     * @param masse  Mass of the Player
     * @return  a new instance of Player
     */
    @Override
    public Player factory(int id, double x, double y, double masse) {
        return new Player(id,x,y,masse);
    }
}
