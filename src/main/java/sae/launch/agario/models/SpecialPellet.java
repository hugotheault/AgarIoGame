package sae.launch.agario.models;

import java.util.Random;

public class SpecialPellet extends Pellet{

    Effect effect;

    public SpecialPellet(int ID, double x, double y, double masse) {
        super(ID, x, y, masse+30);
        randomEffect();
    }

    public void randomEffect(){
        Random random = new Random();
        this.effect = Effect.values()[random.nextInt(4)];
    }

    public Effect getEffect(){
        return this.effect;
    }

}
