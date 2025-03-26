package sae.launch.agario.models;

public class PlayerFactory implements EntityFactory {
    @Override
    public PlayerLeaf factory(int id, double x, double y, double masse) {
        return new PlayerLeaf(id,x,y,masse,false);
    }
}
