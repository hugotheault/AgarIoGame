package sae.launch.agario.models.serverFiles;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import sae.launch.agario.controllers.OnlineInGameController;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientDataReader extends Thread {

    private OnlineInGameController o;

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
                byte[] buffer = new byte[1024];
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
                            double x = Double.parseDouble(attributs[0]);
                            double y = Double.parseDouble(attributs[1]);
                            double radius = Double.parseDouble(attributs[2]);
                            circles.add(new Circle(x, y, radius));
                    }
                    Platform.runLater(() -> {
                        p.getChildren().clear();
                        for (Circle circle : circles) {
                            p.getChildren().add(circle);
                        }
                    });
                    pt.write("ok");
                    pt.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
