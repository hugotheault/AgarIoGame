package sae.launch.agario.models;

public class Camera {
    private double x;
    private double y;
    private double zoomFactor;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public void setZoomFactor(double zoomFactor) {this.zoomFactor = zoomFactor;}
    public double getZoomFactor() {return zoomFactor;}
}
