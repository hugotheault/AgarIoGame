package sae.launch.agario.models;

public class PelletFactory implements EntityFactory {

    @Override
    public Pellet factory(int id, double x, double y, double masse) {
        return new Pellet(id,x,y,masse);
    }
}
