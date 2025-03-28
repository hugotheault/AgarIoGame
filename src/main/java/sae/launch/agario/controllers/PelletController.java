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

    public PelletController(QuadTree quadTree, int maxPellets, double pelletSize) {
        this.quadTree = quadTree;
        this.maxPellets = maxPellets;
        this.pelletSize = pelletSize;
        this.generator = IDGenerator.getGenerator();
    }

    public void generatePellets(boolean choiceSpecialPellet) {
        if (quadTree.getPelletsNumber() < maxPellets) {
            Random random = new Random();
            int pelletsToAdd = maxPellets - quadTree.getPelletsNumber();
            while (pelletsToAdd > 0) {
                Pellet pellet;
                if(choiceSpecialPellet) {
                    if(random.nextInt(100)> 1){
                        pellet = new Pellet(generator.NextID(), random.nextDouble(quadTree.getLength()), random.nextDouble(quadTree.getHeight()), pelletSize);
                    }else{
                        pellet = new SpecialPellet(generator.NextID(), random.nextDouble(quadTree.getLength()), random.nextDouble(quadTree.getHeight()), pelletSize);
                    }
                }else{
                    pellet = new Pellet(generator.NextID(), random.nextDouble(quadTree.getLength()), random.nextDouble(quadTree.getHeight()), pelletSize);
                }
                quadTree.insert(pellet);
                pelletsToAdd--;
            }
        }
    }

    public void removePellet(Pellet pellet) {
        quadTree.remove(pellet);
    }

}
