package sae.launch.agario.models;

import javafx.scene.paint.Color;
import java.util.Random;

public class Pellet extends Entity {
    private final Color color; // Color of the circle on the game display

    /**
     *
     * @param ID ID of the pellet randomly generated with IDGenerator
     * @param x X-axis of the pellet
     * @param y Y-axis of the pellet
     * @param weight weight of the pellet, 10 by default
     */
    public Pellet(int ID, double x, double y, double weight) {
        super(ID, x, y, weight
        );
        Random random = new Random();
        this.color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    /**
     *
     * @return the color of the pellet
     */
    public Color getColor() {
        return color;
    }

    @Override
    public String toString(){
        return "id:"+this.getID()+"/cox:"+this.getX()+"/coy:"+this.getY()+"/mass:"+this.getMass()+"/radius:"+this.getRadius()+"/red:"+Math.round(this.color.getRed()*100.0)/100.0+"/green:"+Math.round(this.color.getGreen()*100.0)/100.0+"/blue:+"+Math.round(color.getBlue()*100.0)/100.0+"#";
    }
    public String toStringRounded(){
        return "id:"+this.getID()+"/cox:"+Math.round(this.getX())+"/coy:"+Math.round(this.getY())+"/mass:"+Math.round(this.getMass())+"/radius:"+this.getRadius()+"/red:"+Math.round(this.color.getRed()*100.0)/100.0+"/green:"+Math.round(this.color.getGreen()*100.0)/100.0+"/blue:+"+Math.round(color.getBlue()*100.0)/100.0+"#";
    }

}
