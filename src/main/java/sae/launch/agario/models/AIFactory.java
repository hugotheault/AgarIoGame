package sae.launch.agario.models;

public class AIFactory implements EntityFactory {
    @Override
    public Entity factory(int id, double x, double y, double mass) {
        return new AI(id,x,y,mass);
    }
}
