package sae.launch.agario.models;

import java.util.Timer;
import java.util.TimerTask;

public enum Effect {


    INVISIBILITY,
    SPEED_BOOST,
    SLOW,
    DIVISION;

    private final double SpecialPelletSpeedBoostValue = 2;
    private final double SpecialPelletSpeedSlowValue = 0.5;
    private final long SpecialPelletDurationEffect = 3000;

    /**
     * Updates the characteristics of a player or an AI depending on the pellet
     * @param obj   The object you want to apply effect on
     */
    public void ApplyEffect(MovableObject obj){
        switch(this){
            case SPEED_BOOST -> {
                obj.setSpecialPelletSpeedBoost(SpecialPelletSpeedBoostValue);
                ResetSpeedModification(obj);
            }
            case SLOW -> {
                obj.setSpecialPelletSpeedBoost(SpecialPelletSpeedSlowValue);
                ResetSpeedModification(obj);
            }
            case INVISIBILITY -> {
                obj.setSpecialPelletIsInvisible(true);
                ResetInvisibleEffect(obj);
            }
        }

    }

    /**
     * Updates the object's speed characteristic back to normal
     * @param obj   the object
     */
    public void ResetSpeedModification(MovableObject obj){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                obj.setSpecialPelletSpeedBoost(1);
            }
        };
        timer.schedule(task,SpecialPelletDurationEffect);

    }

    /**
     * Updates the object's invisible characteristic back to normal
     * @param obj   the object
     */
    public void ResetInvisibleEffect(MovableObject obj){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                obj.setSpecialPelletIsInvisible(false);
            }
        };
        timer.schedule(task,SpecialPelletDurationEffect);

    }




}
