package sae.launch.agario.models;

public class Player extends MovableObject {
    public Player(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public double getSpeed(double xCursor, double yCursor, double paneCenterX,double paneCenterY) {
        if(isInSlowCircle(xCursor,yCursor,paneCenterX,paneCenterY)){
            double slowRateX = Math.abs((paneCenterX - xCursor)/this.getSlowRangeRay());
            double slowRateY = Math.abs((paneCenterY - yCursor)/this.getSlowRangeRay());
            System.out.println(this.getBaseMouvementSpeed() Math.log(this.getMass())/this.getMass());
            return this.getBaseMouvementSpeed() * Math.max(slowRateX,slowRateY) ;/// this.getMass();
        } else{
            return this.getBaseMouvementSpeed();
        }
    }

    public boolean isInSlowCircle(double xCursor, double yCursor, double paneCenterX,double paneCenterY){
        boolean isInSlowCircleRight = xCursor >= paneCenterX && xCursor <= paneCenterX +this.getSlowRangeRay();
        boolean isInSlowCircleLeft = xCursor <= paneCenterX && xCursor >= paneCenterX -this.getSlowRangeRay();
        boolean isInSlowCircleUp = yCursor >= paneCenterY && yCursor <= paneCenterY +this.getSlowRangeRay();
        boolean isInSlowCircleDown = yCursor <= paneCenterY && yCursor >= paneCenterY -this.getSlowRangeRay();
        if((isInSlowCircleRight && isInSlowCircleUp ) || (isInSlowCircleRight && isInSlowCircleDown )
                || (isInSlowCircleLeft && isInSlowCircleUp ) || (isInSlowCircleLeft && isInSlowCircleDown )){
            return true;
        } else {
            return false;
        }
    }

}
