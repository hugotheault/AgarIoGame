package sae.launch.agario.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

import sae.launch.agario.QuadTree;
import sae.launch.agario.models.*;

public class SoloInGameController implements Initializable {
    private @FXML Pane pane;
    private ThreadWorld threadWorld;
    private QuadTree quadTree;
    private PelletController pelletController;

    private ArrayList<Player> players;

    private ArrayList<AI> ais;

    private GameRenderer gameRenderer;

    private final int nbAis = 5;
    private double playerXPercent;
    private double playerYPercent;
    private double coX;
    private double coY;


    private double mapSize;
    private double initialSize;
    private double sizeScaleToEat; //Ex: 1.33 -> You need 33% more mass to eat someone else
    private int maxPelletNb;
    private double sizeToDivide;
    private double pelletSize;

    private double mouseXCursor;
    private double mouseYCursor;

    @Override
    public void initialize(URL u, ResourceBundle r){
        this.mapSize = 2000;
        this.initialSize = 50;
        this.sizeScaleToEat = 1.15;
        this.maxPelletNb = 500;
        this.sizeToDivide = 50;
        this.pelletSize = 10;
        Random rand = new Random();

        quadTree = new QuadTree(mapSize, mapSize, 6, 0, 0);

        int idBase = IDGenerator.getGenerator().NextID();
        quadTree.insert(new Player(idBase, 100, 100, initialSize));

        this.pelletController = new PelletController(quadTree, maxPelletNb, pelletSize);
        pelletController.generatePellets();

        this.players = new ArrayList<>();
        players.add(new Player(idBase, 50, 50, initialSize));

        this.ais = new ArrayList<>();
        //ais.add( new AI(IDGenerator.getGenerator().NextID(),1 + (rand.nextDouble() % 100) ,1 + (rand.nextDouble() % 100),initialSize,quadTree,new AIRandom()));
        /*for( int i = 0; i < nbAis; i++){
            ais.add( new AI(IDGenerator.getGenerator().NextID(),1 + (rand.nextDouble() % 100) ,1 + (rand.nextDouble() % 100),initialSize));
        }*/
        int idAi = IDGenerator.getGenerator().NextID();
        ais.add( new AI(idAi,1 + 40 ,40,initialSize,quadTree,new AIRandom()));
        this.gameRenderer = new GameRenderer(pane);
        quadTree.insert(new AI(idAi,1 + 40 ,40,initialSize,quadTree,new AIRandom()));

        pane.setOnMouseMoved(event ->{
            setPlayerXPercent(event.getX() / pane.getWidth());
            setPlayerYPercent(event.getY() / pane.getHeight());
            setMouseXCursor(event.getX());
            setMouseYCursor(event.getY());
        });

        this.threadWorld = new ThreadWorld (gameRenderer, new Runnable() {
            @Override
            public void run() {
                updateGame();
            }
        });
        threadWorld.start();

    }

    private void updateGame() {
        updatePlayers();

        pelletController.generatePellets();
        updateAI();
        gameRenderer.updateVisuals(quadTree, players);

    }

    @FXML
    protected void onQuitButton() {
        Platform.exit();
    }

    @FXML
    protected void onMenuButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sae/launch/agario/AppView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        System.out.println(((Node) event.getSource()).getScene().getWindow().getHeight());
        stage.setScene(new Scene(root));

        stage.show();
    }


    /**
     *Update the position of all the players, and wheter they can eat or get eaten
     */
    private void updatePlayers() {

        for(Player player: quadTree.getPlayers()){
            //Update position du joueur principal
            double directionX = playerXPercent - 0.5;
            double directionY = playerYPercent - 0.5;
            double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
            if (magnitude != 0) {
                directionX /= magnitude;
                directionY /= magnitude;
            }
            double deltaX = directionX * player.getSpeed(mouseXCursor, mouseYCursor,pane.getWidth() / 2,pane.getHeight() / 2);
            double deltaY = directionY * player.getSpeed(mouseXCursor, mouseYCursor,pane.getWidth() / 2,pane.getHeight() / 2);
            player.setX(player.getX() + deltaX);
            player.setY(player.getY() + deltaY);
        }



        //todo update la position des joueurs IA

        for(Player joueur: quadTree.getAllPlayers()){
            for(Entity cible: quadTree.getEntitiesAroundMovableObject(joueur)){
                if(cible.equals(joueur)) continue;
                if(joueur.canEat(cible)){
                    joueur.Absorb(cible);
                    quadTree.remove(cible);
                }
            }
        }
    }

    private void updateAI(){
        for(AI ai : quadTree.getAI()){
            System.out.println(ai.getX() + " " + ai.getY());

            ai.setTree(quadTree);
            HashMap<String,Double> strategyObjective = ai.getCoordStrategy();

            /*double distanceX = ai.getX() -strategyObjective.get("x");
            double distanceY = ai.getY() - strategyObjective.get("y");*/
            double distanceX = 300 - ai.getX();
            double distanceY = 300 - ai.getY() ;
            System.out.println("distanceX" + distanceX);
            System.out.println("distanceY" + distanceY);
            if(distanceX > ai.getSpeed() && distanceX > 0){
                ai.setX(ai.getX() + ai.getSpeed());
            } else if ( distanceX >= 0 && distanceX < ai.getSpeed()){
                ai.setX(ai.getX() + distanceX);
            } else if ( distanceX > -ai.getSpeed() && distanceX < 0){
                ai.setX(ai.getX() - ai.getSpeed());
            }else{
                ai.setX(ai.getX() -distanceX);
            }

            if(distanceY > ai.getSpeed() && distanceY > 0){
                ai.setY(ai.getY() + ai.getSpeed());
            } else if ( distanceY >= 0 && distanceY < ai.getSpeed()){
                ai.setY(ai.getY() + distanceY);
            } else if ( distanceY > -ai.getSpeed() && distanceY < 0){
                ai.setY(ai.getY() - ai.getSpeed());
            }else{
                ai.setY(ai.getY()-distanceY);
            }
        }

        for(AI ai: quadTree.getAllAi()){
            for(Entity cible: quadTree.getEntitiesAroundMovableObject( ai)){
                if(cible.equals(ai)) continue;
                if(ai.canEat(cible)){
                    ai.Absorb(cible);
                    quadTree.remove(cible);
                }
            }
        }
    }


    /*
    Getters and Setters
     */
    public double getMapSize() {
        return mapSize;
    }

    public void setMapSize(double mapSize) {
        this.mapSize = mapSize;
    }

    public double getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(double initialSize) {
        this.initialSize = initialSize;
    }

    public double getSizeScaleToEat() {
        return sizeScaleToEat;
    }

    public void setSizeScaleToEat(double sizeScaleToEat) {
        this.sizeScaleToEat = sizeScaleToEat;
    }

    public double getPelletNb() {
        return maxPelletNb;
    }

    public void setPelletNb(int pelletNb) {
        this.maxPelletNb = pelletNb;
    }

    public double getSizeToDivide() {
        return sizeToDivide;
    }

    public void setSizeToDivide(double sizeToDivide) {
        this.sizeToDivide = sizeToDivide;
    }

    public void setPlayerXPercent(double playerXPercent) {
        this.playerXPercent = playerXPercent;
    }

    public void setPlayerYPercent(double playerYPercent) {
        this.playerYPercent = playerYPercent;
    }

    public void setCoX(double x) {
        this.coX = x;
    }
    public void setCoY(double y) {
        this.coY = y;
    }

    public void setMouseXCursor(double mouseXCursor) {
        this.mouseXCursor = mouseXCursor;
    }

    public void setMouseYCursor(double mouseYCursor) {
        this.mouseYCursor = mouseYCursor;
    }

}
