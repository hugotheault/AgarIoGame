package sae.launch.agario.models;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sae.launch.agario.QuadTree;
import javafx.scene.effect.Glow;

import java.util.ArrayList;

public class GameRenderer {
    private Pane pane;
    private Camera camera;

    //Default constructor
    public GameRenderer(Pane pane){
        this.pane = pane;
        this.camera = new Camera();
        camera.setZoomFactor(0.1);
    }
    public GameRenderer(){
        this.camera = new Camera();
        camera.setZoomFactor(0.1);
    }

    /**
     * The method called every time the game is updated
     */
    public void updateVisuals(QuadTree quadTree, ArrayList<Player> players, int IDPlayer) {
        camera.updatePosition(quadTree, IDPlayer);
        render(quadTree, players);
    }

    /**
     * Render all the Entities on the pane
     */
    private void render(QuadTree quadTree, ArrayList<Player> players) {
        // Exécute les opérations sur le thread de JavaFX
        Platform.runLater(() -> {
            pane.getChildren().clear();

            double centerX = pane.getWidth() / 2;
            double centerY = pane.getHeight() / 2;

            for (Entity entity: quadTree.getEntitiesInRegion(
                    camera.getX() - centerX,
                    camera.getY() - centerY,
                    camera.getX() + centerX,
                    camera.getY() + centerY)) {
                drawEntity(centerX, centerY, entity, players);
            }
            ArrayList<Player> entites = quadTree.getAllPlayers();
            for(Entity entity: entites){
                drawEntity(centerX, centerY, entity, players);
            }
        });
    }

    /**
     * Draw an entity on the pane
     * @param centerX The x axis center of the entity
     * @param centerY The y axis center of the entity
     * @param entity The entity
     */
    private void drawEntity(double centerX, double centerY, Entity entity, ArrayList<Player> players) {
        double entityX = (entity.getX() - camera.getX() + centerX);
        double entityY = (entity.getY() - camera.getY() + centerY);
        double entityRadius = entity.getRadius();

        Circle circle = new Circle(entityX, entityY, entityRadius);

        if (entity instanceof MovableObject) {
            if(entity instanceof AI){
                if( ((AI)entity).isSpecialPelletIsInvisible()){
                    circle.setFill(Color.TRANSPARENT);
                } else{
                    circle.setFill(Color.RED);
                }

            } else {
                if ( ((MovableObject)entity).isSpecialPelletIsInvisible()){
                    circle.setFill(Color.LIGHTBLUE);
                } else{
                    circle.setFill(Color.BLUE);
                }

            }
        } else if(entity instanceof SpecialPellet) {
            circle.setFill(Color.YELLOW);
            circle.setStroke(Color.YELLOW);
            circle.setStrokeWidth(2);
            Glow glow = new Glow();
            glow.setLevel(2);
            circle.setEffect(glow);
        } else {
            circle.setFill(((Pellet) entity).getColor());
        }

        pane.getChildren().add(circle);
    }

    public void setPane(Pane pane){
        this.pane = pane;
    }
}
