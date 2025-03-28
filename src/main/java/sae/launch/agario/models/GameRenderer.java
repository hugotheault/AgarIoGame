package sae.launch.agario.models;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import sae.launch.agario.QuadTree;
import javafx.scene.effect.Glow;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

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

    /**
     * The method called every time the game is updated
     */
    public void updateVisuals(QuadTree quadTree, ArrayList<PlayerComposite> players) {
        camera.updatePosition(quadTree);
        render(quadTree, players);
    }

    /**
     * Render all the Entities on the pane
     */
    private void render(QuadTree quadTree, ArrayList<PlayerComposite> players) {
        Platform.runLater(() -> {
            pane.getChildren().clear();

            double centerX = pane.getWidth() / 2;
            double centerY = pane.getHeight() / 2;

            for (Entity entity: quadTree.getEntitiesInRegion(
                    camera.getX() - centerX,
                    camera.getY() - centerY,
                    camera.getX() + centerX,
                    camera.getY() + centerY)) {
                drawEntity(centerX, centerY, entity);
            }
            ArrayList<PlayerComposite> entites = quadTree.getAllPlayers();
            for(Entity entity: entites){
                if(entity instanceof PlayerComposite){
                    for(PlayerLeaf player: ((PlayerComposite) entity).getPlayers()){
                        drawEntity(centerX, centerY, player);
                    }
                }
            }
        });
    }

    /**
     * Draw an entity on the pane
     * @param centerX The x axis center of the entity
     * @param centerY The y axis center of the entity
     * @param entity The entity
     */
    private void drawEntity(double centerX, double centerY, Entity entity) {
        double entityX = (entity.getX() - camera.getX() + centerX);
        double entityY = (entity.getY() - camera.getY() + centerY);
        double entityRadius = entity.getRadius();

        if(entity instanceof PlayerComposite){
            return;
        }

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
                    Image motif = new Image(getClass().getResource("/unicaen.png").toExternalForm());

                    circle.setFill(new ImagePattern(motif));
                    circle.setOpacity(0.4);

                } else if (entity instanceof PlayerLeaf) {
                    try {
                        Image motif = new Image(getClass().getResource("/unicaen.png").toExternalForm());
                        circle.setFill(new ImagePattern(motif));

                    } catch (NullPointerException e) {
                        System.out.println("Image non trouvée !");
                    }
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

}
