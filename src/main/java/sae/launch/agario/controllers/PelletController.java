package sae.launch.agario.controllers;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;
import sae.launch.agario.QuadTree;
import sae.launch.agario.models.IDGenerator;
import sae.launch.agario.models.Pellet;
import sae.launch.agario.models.SpecialPellet;

import java.util.Random;

public class PelletController {

    private QuadTree quadTree;	
    private IDGenerator generator;
    private int maxPellets;
    private double pelletSize;

    /**
     * Defines the characteristics of the pellets in the game 
     * @param quadTree	The game's Quadtree @see Quadtree
     * @param maxPellets	The game's max amount of pellets displayed at the same time
     * @param pelletSize	Size of the pellets
     */
    public PelletController(QuadTree quadTree, int maxPellets, double pelletSize) {
        this.quadTree = quadTree;
        this.maxPellets = maxPellets;
        this.pelletSize = pelletSize;
        this.generator = IDGenerator.getGenerator();
    }

    /**
     * Inserts the pellets into the quadtree
     */
    public void generatePellets() {
        if (quadTree.getPelletsNumber() < maxPellets) {
            Random random = new Random();
            int pelletsToAdd = maxPellets - quadTree.getPelletsNumber();
            while (pelletsToAdd > 0) {
                Pellet pellet;
                if( random.nextInt(100)> 1) {
                    pellet = new Pellet(generator.NextID(), random.nextDouble(quadTree.getLength()), random.nextDouble(quadTree.getHeight()), pelletSize);
                }else{
                    pellet = new SpecialPellet(generator.NextID(), random.nextDouble(quadTree.getLength()), random.nextDouble(quadTree.getHeight()), pelletSize);
                }
                quadTree.insert(pellet);
                pelletsToAdd--;
            }
        }
    }

    /**
     * Remove the designated Pellet from the Quadtree
     * @param pellet
     */
    public void removePellet(Pellet pellet) {
        quadTree.remove(pellet);
    }

}
