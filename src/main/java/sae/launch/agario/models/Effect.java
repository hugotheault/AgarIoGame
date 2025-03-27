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
