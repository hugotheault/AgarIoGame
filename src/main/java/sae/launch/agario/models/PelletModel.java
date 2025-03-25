package sae.launch.agario.models;

public class PelletModel {

    // Coordonnées - Coordinates
    private static double x;
    private static double y;

    // Rayon de la pastille - pellet's radius
    private final static double radius = 5.;

    // Getter
    public double getRadius(){
        return this.radius;
    }

    public double getX(){
        return this.x;
    }

    public static double getY() {
        return y;
    }

    // Setter

    public static void setX(double x) {
        PelletModel.x = x;
    }

    public static void setY(double y) {
        PelletModel.y = y;
    }

}
