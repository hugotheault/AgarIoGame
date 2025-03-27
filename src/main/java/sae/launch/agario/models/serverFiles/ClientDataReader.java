package sae.launch.agario.models.serverFiles;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import sae.launch.agario.controllers.OnlineInGameController;
import sae.launch.agario.models.Camera;
import sae.launch.agario.models.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ClientDataReader extends Thread {

    private OnlineInGameController o;
    private Camera camera;

    public ClientDataReader(OnlineInGameController o){
        this.o=o;
        this.camera=new Camera();
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
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                InputStream inputStream = o.getClientSocker().getInputStream();
                byte[] buffer = new byte[200000];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    String messageRecu = new String(buffer, 0, bytesRead);

                    if (messageRecu.contains("end")) {
                        break;  // Nous avons reçu le message complet, avec "end"
                    }
                }

                byte[] finalData = byteArrayOutputStream.toByteArray();
                String messageFinal = new String(finalData);
                String[] parts = messageFinal.split("end");
                messageFinal = parts[0];


                //String messageRecu = new String(buffer, 0, bytesRead);
                Pane p = o.getPane();
                ArrayList<Circle> circles = new ArrayList<>();
                Player player = null;

                String[] elements = messageFinal.split("#");
                String[] playerAttributes = elements[0].split("/");
                int id = 0;
                double x = 0, y = 0, radius = 0;
                double mass = 0;
                for (String attribute : playerAttributes) {
                    if (attribute.contains("cox:")) x = Double.parseDouble(attribute.substring(4, attribute.length()));
                    if (attribute.contains("coy:")) y = Double.parseDouble(attribute.substring(4, attribute.length()));
                    if (attribute.contains("id:")) id = Integer.parseInt(attribute.substring(3, attribute.length()));
                    if (attribute.contains("mass:"))
                        mass = Double.parseDouble(attribute.substring(5, attribute.length()));
                    if (attribute.contains("radius:"))
                        radius = Double.parseDouble(attribute.substring(7, attribute.length()));
                }
                player = new Player(id, x, y, mass);
                camera.updatePosition(player);
                for (String element : elements) {
                    String[] attributs = element.split("/");
                    x = 0;
                    y = 0;
                    mass = 0;
                    Color color;
                    Double red = null;
                    Double green = null;
                    Double blue = null;
                    for (String attribut : attributs) {
                        if (attribut.contains("cox:")) x = Double.parseDouble(attribut.substring(4, attribut.length()));
                        if (attribut.contains("coy:")) y = Double.parseDouble(attribut.substring(4, attribut.length()));
                        if (attribut.contains("id:")) id = Integer.parseInt(attribut.substring(3, attribut.length()));
                        if (attribut.contains("red:"))
                            red = Double.parseDouble(attribut.substring(4, attribut.length()));
                        if (attribut.contains("green:"))
                            green = Double.parseDouble(attribut.substring(6, attribut.length()));
                        if (attribut.contains("blue:"))
                            blue = Double.parseDouble(attribut.substring(5, attribut.length()));
                        if (attribut.contains("mass:"))
                            mass = Double.parseDouble(attribut.substring(5, attribut.length()));
                        if (attribut.contains("radius:"))
                            radius = Double.parseDouble(attribut.substring(7, attribut.length()));
                    }

                    Circle circle = new Circle((x - camera.getX() + o.getPane().getWidth() / 2), (y - camera.getY() + o.getPane().getHeight() / 2), radius);
                    if (red != null && green != null && blue != null) {
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
                    System.out.println(o.getPlayerXPercent());
                    double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
                    if (magnitude != 0) {
                        directionX /= magnitude;
                        directionY /= magnitude;
                    }

                    double deltaX = Math.round(directionX * player.getSpeed(o.getCoX(), o.getCoY(), o.getPane().getWidth()/2, o.getPane().getHeight()/2) * 1000.0)/1000.0;
                    double deltaY = Math.round(directionY * player.getSpeed(o.getCoX(), o.getCoY(),o.getPane().getWidth()/2, o.getPane().getHeight()/2) * 1000.0)/1000.0;
                    System.out.println("deltaX: " + deltaX);
                    System.out.println("deltaY: " + deltaY);

                    String playerDirection = "deplacement:" + deltaX + "/" + deltaY;
                    pt.write(playerDirection);
                    pt.flush();

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
