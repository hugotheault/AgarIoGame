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
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import sae.launch.agario.QuadTree;
import sae.launch.agario.models.*;
import sae.launch.agario.models.serverFiles.ClientDataReader;
import sae.launch.agario.models.serverFiles.Server;

public class OnlineInGameController implements Initializable {

    private int ID;

    private boolean isHost;
    private Server server;
    private PrintWriter clientPrintWriter;
    private Socket clientSocker;





    private @FXML Pane pane;
    private ThreadWorld threadWorld;
    private QuadTree quadTree;
    private PelletController pelletController;
    ClientDataReader ecouteur;
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
        //reads the properties file
        FileInputStream input = null;
        Properties properties  = new Properties();
        try {
            input = new FileInputStream("application.properties");
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mapSize = Double.parseDouble(properties.getProperty("mapSize"));
        initialSize = Double.parseDouble(properties.getProperty("initialSize"));
        maxPelletNb = Integer.parseInt(properties.getProperty("maxPelletNb"));
        sizeToDivide = Double.parseDouble(properties.getProperty("sizeToDivide"));
        pelletSize = Double.parseDouble(properties.getProperty("pelletSize"));
        sizeScaleToEat = Double.parseDouble(properties.getProperty("sizeScaleToEat"));

        this.isHost = true;
        server = new Server(this);
        quadTree = new QuadTree(mapSize, mapSize, 6, 0, 0);
        this.gameRenderer = new GameRenderer();

        this.pelletController = new PelletController(quadTree, maxPelletNb, pelletSize);
        pelletController.generatePellets();

        this.players = new ArrayList<>();

        this.threadWorld = new ThreadWorld (gameRenderer, new Runnable() {
            @Override
            public void run() {
                updateGame();
            }
        });
    }
    public OnlineInGameController(String ip, int port) throws IOException {
        //reads the properties file
        FileInputStream input = null;
        Properties properties  = new Properties();
        try {
            input = new FileInputStream("application.properties");
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mapSize = Double.parseDouble(properties.getProperty("mapSize"));
        initialSize = Double.parseDouble(properties.getProperty("initialSize"));
        maxPelletNb = Integer.parseInt(properties.getProperty("maxPelletNb"));
        sizeToDivide = Double.parseDouble(properties.getProperty("sizeToDivide"));
        pelletSize = Double.parseDouble(properties.getProperty("pelletSize"));
        sizeScaleToEat = Double.parseDouble(properties.getProperty("sizeScaleToEat"));

        this.isHost = false;
        clientSocker = new Socket(ip, port);
        clientPrintWriter = new PrintWriter(clientSocker.getOutputStream(), true);
        clientPrintWriter.write("Je me connecte");

        while(this.ID == 0) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            try {
                bytesRead = this.clientSocker.getInputStream().read(buffer);
                if (bytesRead != -1) {
                    String messageRecu = new String(buffer, 0, bytesRead);
                    if (messageRecu.contains("id:")) {
                        int idIndex = messageRecu.indexOf("id:") + 3;

                        String idString = messageRecu.substring(idIndex).trim();
                        try {
                            int id = Integer.parseInt(idString);
                            this.ID = id;
                            System.out.println("ID reçu : " + this.ID);
                        } catch (NumberFormatException e) {

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }


        ecouteur = new ClientDataReader(this);



    }


    @Override
    public void initialize(URL u, ResourceBundle r) {
        System.out.println("Initialize");
        pane.setOnMouseMoved(event ->{
            setPlayerXPercent(event.getX() / pane.getWidth());
            setPlayerYPercent(event.getY() / pane.getHeight());
            setCoX(event.getX());
            setCoY(event.getY());

        });
        if(isHost){
            gameRenderer.setPane(pane);
            threadWorld.start();
        }
        else{
            ecouteur.start();
        }
    }

    private void updateGame() {
        pelletController.generatePellets();
        gameRenderer.updateVisuals(quadTree, players, this.ID);

        writeQuadTree();

        updatePlayers();
    }

    private void writeQuadTree() {

        //TODO verifier si la méthode est utile
        for(Player player: players){
            quadTree.remove(player);
            quadTree.insert(player);
        }

        for(int i = 0; i < players.size(); i++){
            Player p = players.get(i);
            quadTree.remove(p);
            ArrayList<Entity> entities = quadTree.getEntitiesInRegion(p.getX()-500, p.getY()-500, p.getX()+500, p.getY()+500);
            StringBuilder s = new StringBuilder();
            s.append(p);
            for(Entity e: entities){
                s.append(e.toStringRounded());
            }

            String lastMessage = server.getClientConnexionList().get(i).getLastMessage();
            if(!lastMessage.equals("")){
                s.deleteCharAt(s.length() - 1);
                s.append("/end");

                server.getPrintWriterList().get(i).write(s.toString());
                server.getPrintWriterList().get(i).flush();
            }

            if(lastMessage.contains("deplacement:")){
                String message = lastMessage.substring(12, lastMessage.length()-1);
                String[] deplacements = message.split("/");

                double deplacementX = Double.parseDouble(deplacements[0]);
                double deplacementY = Double.parseDouble(deplacements[1]);

                p.setX(p.getX()+deplacementX);
                p.setY(p.getY()+deplacementY);
            }

            quadTree.insert(p);
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
    public QuadTree getQuadTree(){return quadTree;}

    public Socket getClientSocker(){return clientSocker;}

    public Pane getPane(){return this.pane;}

    public double getPlayerXPercent() {
        return playerXPercent;
    }

    public double getPlayerYPercent() {
        return playerYPercent;
    }

    public double getCoX() {
        return coX;
    }

    public double getCoY() {
        return coY;
    }

}
