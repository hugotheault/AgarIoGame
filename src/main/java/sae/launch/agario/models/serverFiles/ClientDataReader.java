package sae.launch.agario.models.serverFiles;

import javafx.application.Platform;
import javafx.scene.Camera;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import sae.launch.agario.controllers.OnlineInGameController;
import sae.launch.agario.models.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientDataReader extends Thread {

    private OnlineInGameController o;
    private Camera camera;

    public ClientDataReader(OnlineInGameController o){
        this.o=o;
    }
    @Override
    public void run() {
        PrintWriter pt = null;
        try {
            pt = new PrintWriter(o.getClientSocker().getOutputStream(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pt.write("ok");
        pt.flush();
        while(true){
            try {
                byte[] buffer = new byte[16000];
                //TODO : augmenter la taille du buffer si on veut envoyer beaucoup d'éléments
                int bytesRead;
                bytesRead = o.getClientSocker().getInputStream().read(buffer);
                if (bytesRead != -1) {
                    String messageRecu = new String(buffer, 0, bytesRead);
                    System.out.println(messageRecu);
                    Pane p = o.getPane();
                    ArrayList<Circle> circles = new ArrayList<>();


                    String[] elements = messageRecu.split("#");
                    for(String element: elements){
                        String[] attributs = element.split("/");
                            int id = Integer.parseInt(attributs[0]);
                            double x = Double.parseDouble(attributs[1]);
                            double y = Double.parseDouble(attributs[2]);
                            double radius = Double.parseDouble(attributs[3]);
                            circles.add(new Circle(x, y, radius));

                    }
                    
                    Platform.runLater(() -> {
                        p.getChildren().clear();
                        for (Circle circle : circles) {
                            p.getChildren().add(circle);
                        }
                    });

                    double directionX = o.getPlayerXPercent() - 0.5;
                    double directionY = o.getPlayerYPercent() - 0.5;
                    double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
                    if (magnitude != 0) {
                        directionX /= magnitude;
                        directionY /= magnitude;
                    }

                    String[] playerAttributes = elements[0].split("/");
                    Double mass = Math.pow(Double.parseDouble(playerAttributes[3]), 2);
                    System.out.println("Joueur créé avec parametres : " + Integer.parseInt(playerAttributes[0]) + " " + " " + Double.parseDouble(playerAttributes[1]) + " " + Double.parseDouble(playerAttributes[2]) + " " + mass);
                    Player player = new Player(Integer.parseInt(playerAttributes[0]), Double.parseDouble(playerAttributes[1]), Double.parseDouble(playerAttributes[2]), mass);
                    System.out.println("speed : " + player.getSpeed());
                    System.out.println("dir x : " +directionX);
                    double deltaX = Math.round(directionX * player.getSpeed(o.getCoX(), o.getCoY()) * 1000.0)/1000.0;
                    double deltaY = Math.round(directionY * player.getSpeed(o.getCoX(), o.getCoY()) * 1000.0)/1000.0;

                    String playerDirection = "deplacement:" + deltaX + "/" + deltaY;
                    pt.write(playerDirection);
                    pt.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
