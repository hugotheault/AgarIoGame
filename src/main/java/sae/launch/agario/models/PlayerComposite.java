package sae.launch.agario.models;

import java.util.ArrayList;
import java.util.List;

public class PlayerComposite extends PlayerComponant{

    ArrayList<PlayerComponant> players;

    public PlayerComposite(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
    }

    @Override
    public void updatePosition() {

    }

    public void add(PlayerComponant p){
        players.add(p);
    }

    public void remove(PlayerComponant p){
        players.remove(p);
    }

    public PlayerComponant getPlayer(int i){
        return players.get(i);
    }

    public ArrayList<PlayerComponant> getAllPlayer(){
        return players;
    }

    @Override
    public double split() {
        List<PlayerComponant> newPlayers = new ArrayList<>();
        for (PlayerComponant player : players) {
            player.split();
            newPlayers.add(new PlayerLeaf(player.getID(), player.getX()+10, player.getY()+10, player.getMass(), false));
        }
        players.addAll(newPlayers);
        return 0;
    }

    @Override
    public double getSpeed(double xCursor, double yCursor, double paneCenterX, double paneCenterY) {
        return 0;
    }
}
