package sae.launch.agario.models;


import sae.launch.agario.QuadTree;

import java.util.ArrayList;
import java.util.HashMap;

public interface AIStrategy {
    public HashMap<String,Double> execStrategy(Double x, Double y, QuadTree quadtree);

}
