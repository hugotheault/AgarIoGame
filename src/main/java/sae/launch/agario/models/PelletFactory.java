package sae.launch.agario.models;

public class PelletFactory implements EntityFactory {

    /**
     * Build a new Pellet
     * @param id    Id
     * @param x     Coordinate x
     * @param y     Coordinate y
     * @param masse Mass of the Pellet
     * @return      a new instance of Pellet
     */
    @Override
    public Pellet factory(int id, double x, double y, double masse) {
        return new Pellet(id,x,y,masse);
    }
}
