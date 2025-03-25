package sae.launch.agario.models;

public abstract class Entity {
    private final int ID;
    private double x;
    private double y;
    private double mass;

    public Entity(int ID, double x, double y, double mass){
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.mass = mass;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public int getID() {
        return ID;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMass() {
        return mass;
    }
}
