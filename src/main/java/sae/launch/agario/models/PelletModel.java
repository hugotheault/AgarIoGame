package sae.launch.agario.models;

public class PelletModel {

    // Coordonnées - Coordinates
    private static double x;
    private static double y;
    private final static double radius = 5.; // Rayon de la pastille - pellet's radius

    // Constructeur - Constructor
    public PelletModel(double x, double y) {
        setX(x);
        setY(y);
    }

    // Getter
    public double getRadius(){
        return this.radius;
    }

    public double getX(){
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    // Setter
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

}
