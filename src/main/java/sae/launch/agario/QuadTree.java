package sae.launch.agario;

import java.util.ArrayList;

public class QuadTree {
    private QuadTree NWTree;
    private QuadTree NETree;
    private QuadTree SWTree;
    private QuadTree SETree;
    private int depth;
    private double length;
    private double width;



    //TODO Change integer type to Entity
    private Integer entities;

    public QuadTree(double length, double width, int depth){
        this.depth = depth;
        this.length = length;
        this.width = width;
        if(depth > 0){
            this.NWTree = new QuadTree(length/2, width/2, depth-1);
            this.NETree = new QuadTree(length/2, width/2, depth-1);
            this.SWTree = new QuadTree(length/2, width/2, depth-1);
            this.SETree = new QuadTree(length/2, width/2, depth-1);
        } else{

        }
    }

    public double getMinLength(){
        if(depth == 0) return this.length;
        else return NETree.getMinLength();
    }
    public double getMinWidth() {
        if (depth == 0) return this.width;
        else return NETree.getMinWidth();
    }

    public boolean insert(){
        return true;
    }





}
