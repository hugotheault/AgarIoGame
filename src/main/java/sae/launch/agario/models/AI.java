package sae.launch.agario.models;

public class AI extends MovableObject {
    public AI(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
    }

    @Override
    public double getSpeed() {
        return this.getBaseMouvementSpeed() / this.getMass();
    }

    @Override
    public double getSpeed(double xCursor, double yCursor) {
        throw new IllegalCallerException("AI ne peux pas appeler cette méthode");
    }

}
