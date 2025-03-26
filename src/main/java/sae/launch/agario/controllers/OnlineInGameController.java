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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import sae.launch.agario.QuadTree;
import sae.launch.agario.models.*;
import sae.launch.agario.models.serverFiles.Server;

public class OnlineInGameController implements Initializable {

    private int ID;

    private Server server;
    private PrintWriter clientPrintWriter;
    private Socket clientSocker;





    private @FXML Pane pane;
    private ThreadWorld threadWorld;
    private QuadTree quadTree;
    private PelletController pelletController;

    private ArrayList<Player> players;

    private GameRenderer gameRenderer;


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

    public OnlineInGameController() {
        server = new Server(this);

        this.pelletController = new PelletController(quadTree, maxPelletNb, pelletSize);
        pelletController.generatePellets();

        this.players = new ArrayList<>();

        this.threadWorld = new ThreadWorld (gameRenderer, new Runnable() {
            @Override
            public void run() {
                updateGame();
            }
        });
        threadWorld.start();
    }
    public OnlineInGameController(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        clientPrintWriter = new PrintWriter(socket.getOutputStream(), true);
        clientPrintWriter.write("Je me connecte");

        while(this.ID == 0) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            try {
                bytesRead = this.clientSocker.getInputStream().read(buffer);
                if (bytesRead != -1) {
                    String messageRecu = new String(buffer, 0, bytesRead);
                    System.out.println(messageRecu);
                    this.ID= Integer.parseInt(messageRecu.substring(3));
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        Runnable task = new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        bytesRead = clientSocker.getInputStream().read(buffer);
                        if (bytesRead != -1) {
                            String messageRecu = new String(buffer, 0, bytesRead);
                            System.out.println(messageRecu);
                            
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        Thread ecouteThread = new Thread(task);
        ecouteThread.start();


    }


    @Override
    public void initialize(URL u, ResourceBundle r) {

        //reads the properties file
        FileInputStream input = null;
        Properties properties  = new Properties();
        try {
            input = new FileInputStream("application.properties");
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        quadTree = new QuadTree(mapSize, mapSize, 6, 0, 0);

        this.gameRenderer = new GameRenderer(pane);

        pane.setOnMouseMoved(event ->{
            setPlayerXPercent(event.getX() / pane.getWidth());
            setPlayerYPercent(event.getY() / pane.getHeight());
            setCoX(event.getX());
            setCoY(event.getY());
        });

    }

    private void updateGame() {
        updatePlayers();

        pelletController.generatePellets();
        gameRenderer.updateVisuals(quadTree, players, this.ID);

        writeQuadTree();
    }

    private void writeQuadTree() {
        for(PrintWriter p: server.getPrintWriterList()){
            p.print(quadTree);
        }
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
     *Update the position of all the players, and whether they can eat or get eaten
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
            double deltaX = directionX * player.getSpeed(coX, coY);
            double deltaY = directionY * player.getSpeed(coX, coY);
            player.setX(player.getX() + deltaX);
            player.setY(player.getY() + deltaY);
        }



        //todo update la position des joueurs IA

        for(Player joueur: quadTree.getAllPlayers()){
            for(Entity cible: quadTree.getEntitiesAroundPlayer((Player) joueur)){
                if(cible.equals(joueur)) continue;
                if(joueur.canEat(cible)){
                    joueur.setMass(joueur.getMass()+cible.getMass());
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
    public ArrayList<Player> getPlayers(){return this.players;}
    public QuadTree getQuadTree(){return this.getQuadTree();}

}
