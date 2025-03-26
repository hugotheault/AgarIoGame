package sae.launch.agario.models;

public interface PlayerComponant {
    void updatePosition(double deltaX, double deltaY);
    double getMass();
    double getX();
    double getY();
    void setX(double x);
    void setY(double y);

    int getID();

    double getRay();

    void setMass(double v);

    double getSpeed(double xCursor, double yCursor, double paneCenterX,double paneCenterY);

    boolean canEat(Entity cible);
}
