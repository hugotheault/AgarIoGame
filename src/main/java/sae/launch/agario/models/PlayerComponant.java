package sae.launch.agario.models;

import javafx.scene.canvas.GraphicsContext;

public abstract class PlayerComponant extends MovableObject{

    private double x, y;
    private double speed;

    public PlayerComponant(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
    }

    public abstract void updatePosition();

    public abstract double split();

    public double getX() {return x;}
    public double getY() {return y;}
    public double getSpeed() {return speed;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setSpeed(double speed) {this.speed = speed;}

}
