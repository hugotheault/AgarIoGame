package sae.launch.agario.models;

import javafx.scene.paint.Color;
import java.util.Random;

public class Pellet extends Entity {
    private final Color color;

    public Pellet(int ID, double x, double y, double masse) {
        super(ID, x, y, masse);
        Random random = new Random();
        this.color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public Color getColor() {
        return color;
    }
}
