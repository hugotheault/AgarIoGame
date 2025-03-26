package sae.launch.agario.models;

public class AI extends MovableObject {
    public AI(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public double getSpeed(double xCursor, double yCursor,double paneCenterX,double paneCenterY) {
        return 0;
    }

}
