package sae.launch.agario.models;

import sae.launch.agario.QuadTree;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AIRandom implements AIStrategy{

    private int tt = 0;
    private HashMap<String,Double> g;
    @Override
    public HashMap<String,Double> execStrategy(Double x, Double y, QuadTree quadtree) {
        if(tt == 0) {
            tt = 1;
            Random rand = new Random();
            HashMap<String, Double> coordinates = new HashMap<>();
            coordinates.put("x", rand.nextDouble(100));
            coordinates.put("y", rand.nextDouble(100));
            g = coordinates;
            Timer timer = new Timer();
            TimerTask t = new TimerTask(){

                @Override
                public void run() {
                    tt = 0;
                }
            };
            timer.schedule(t,2000);

            return coordinates;
        }
        return g;

    }

}
