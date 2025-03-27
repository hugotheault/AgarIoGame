package sae.launch.agario.models;

import sae.launch.agario.Boundary;
import sae.launch.agario.QuadTree;
import java.util.ArrayList;

public class Camera {
    private double x;
    private double y;
    private double zoomFactor;

    public Camera(double x, double y, double zoomFactor) {
        this.x = x;
        this.y = y;
        this.zoomFactor = zoomFactor;
    }

    public Camera() {}

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getZoomFactor() { return zoomFactor; }
    public void setZoomFactor(double zoomFactor) { this.zoomFactor = zoomFactor; }

    /**
     * Met à jour la position de la caméra en centrant sur les joueurs suivis.
     */
    public void updatePosition(QuadTree quadtree) {
        ArrayList<PlayerComposite> players = quadtree.getPlayers();

        if (players.isEmpty()) return;

        // Calcul du barycentre des joueurs suivis
        double sumX = 0, sumY = 0;
        int count = 0;
        for (PlayerLeaf player : players.get(0).getAllPlayer()) {
            if (player instanceof PlayerLeaf) {
                PlayerLeaf leaf = (PlayerLeaf) player;
                sumX += leaf.getX();
                sumY += leaf.getY();
                count++;
            }
        }

        if (count > 0) {
            this.x = sumX / count;
            this.y = sumY / count;
        }
    }

    /**
     * Retourne la zone visible en fonction de la position et du zoom de la caméra.
     */
    public Boundary getVisibleRegion(double screenWidth, double screenHeight) {
        double viewWidth = screenWidth / zoomFactor;
        double viewHeight = screenHeight / zoomFactor;

        double x1 = x - viewWidth / 2;
        double y1 = y - viewHeight / 2;
        double x2 = x + viewWidth / 2;
        double y2 = y + viewHeight / 2;

        return new Boundary(x1, y1, viewWidth, viewHeight);
    }
}
