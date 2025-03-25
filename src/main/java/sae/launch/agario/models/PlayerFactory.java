package sae.launch.agario.models;

public class PlayerFactory implements EntityFactory {
    @Override
    public Player factory(int id, double x, double y, double masse) {
        return new Player(id,x,y,masse);
    }
}
