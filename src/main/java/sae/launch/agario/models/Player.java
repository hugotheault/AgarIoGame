package sae.launch.agario.models;

public class Player extends MovableObject {
    public Player(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public double getSpeed(double xCursor,double yCursor) {
        return 0;
    }
}
