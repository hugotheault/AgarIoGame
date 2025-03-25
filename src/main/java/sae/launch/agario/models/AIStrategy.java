package sae.launch.agario.models;


import java.util.ArrayList;
import java.util.HashMap;

public interface AIStrategy {
    public HashMap<String,Double> execStrategy(Double x, Double y);

}
