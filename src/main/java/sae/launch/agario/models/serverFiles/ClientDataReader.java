package sae.launch.agario.models.serverFiles;

import javafx.application.Platform;
import javafx.scene.Camera;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
                byte[] buffer = new byte[100000];
                //TODO : augmenter la taille du buffer si on veut envoyer beaucoup d'éléments
                int bytesRead;
                bytesRead = o.getClientSocker().getInputStream().read(buffer);

                if (bytesRead != -1) {
                    String messageRecu = new String(buffer, 0, bytesRead);
                    System.out.println(messageRecu);
                    Pane p = o.getPane();
                    ArrayList<Circle> circles = new ArrayList<>();
                    Player player = null;

                    String[] elements = messageRecu.split("#");
                    String[] playerAttributes = elements[0].split("/");
                    for(String attribute: playerAttributes){
                        int id=0;
                        double x=0, y=0;
                        double radius=0;
                        if(attribute.contains("cox:")) x= Double.parseDouble(attribute.substring(4, attribute.length()));
                        if(attribute.contains("coy:")) y=Double.parseDouble(attribute.substring(4, attribute.length()));
                        if(attribute.contains("id:")) id=Integer.parseInt(attribute.substring(3, attribute.length()));
                        if(attribute.contains("radius:")) radius=Double.parseDouble(attribute.substring(7, attribute.length()));
                        player = new Player(id, x, y, Math.pow(radius,2));
                    }

                    for(String element: elements){
                        String[] attributs = element.split("/");
                            int id ;
                            double x = 0;
                            double y = 0;
                            double radius = 0;
                            Color color;
                            Double red = null;
                            Double green = null;
                            Double blue = null;
                            for(String attribut: attributs){
                                if(attribut.contains("cox:")) x= Double.parseDouble(attribut.substring(4, attribut.length()));
                                if(attribut.contains("coy:")) y=Double.parseDouble(attribut.substring(4, attribut.length()));
                                if(attribut.contains("id:")) id=Integer.parseInt(attribut.substring(3, attribut.length()));
                                if(attribut.contains("red:")) red=Double.parseDouble(attribut.substring(4, attribut.length()));
                                if(attribut.contains("green:")) green=Double.parseDouble(attribut.substring(6, attribut.length()));
                                if(attribut.contains("blue:")) blue=Double.parseDouble(attribut.substring(5, attribut.length()));
                                if(attribut.contains("radius:")) radius=Double.parseDouble(attribut.substring(7, attribut.length()));
                            }
                            Circle circle = new Circle(x, y, radius);
                            if(red!=null && green != null && blue != null) {
                                circle.setFill(Color.color(red, green, blue));
                            }
                        circles.add(circle);

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

                    System.out.println("speed : " + player.getSpeed());
                    System.out.println("dir x : " +directionX);
                    double deltaX = Math.round(directionX * player.getSpeed(o.getCoX(), o.getCoY(), o.getPane().getWidth()/2, o.getPane().getHeight()/2) * 1000.0)/1000.0;
                    double deltaY = Math.round(directionY * player.getSpeed(o.getCoX(), o.getCoY(),o.getPane().getWidth()/2, o.getPane().getHeight()/2) * 1000.0)/1000.0;
                    System.out.println("deltaX: " + deltaX);
                    System.out.println("deltaY: " + deltaY);

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
