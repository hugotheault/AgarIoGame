package sae.launch.agario.models;

public class PlayerLeaf extends MovableObject implements PlayerComponant {

    private final static double speedSlowMultiplier = 0.5;

    // Constructor
    public PlayerLeaf(int ID, double x, double y, double mass) {
        super(ID, x, y, mass);
    }

    @Override
    public void updatePosition(double deltaX, double deltaY) {
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
    }

    @Override
    public double getMass() {
        return super.getMass();
    }

    public int getID() {
        return super.getID();
    }

    @Override
    public double getRay() {
        return super.getRadius();
    }

    @Override
    public double getSpeed() {
        throw new IllegalCallerException(" Player ne peux pas appeler cette méthode");
    }

    @Override
    public double getSpeed(double xCursor, double yCursor, double paneCenterX,double paneCenterY) {
        if(isInSlowCircle(xCursor,yCursor,paneCenterX,paneCenterY)){
            double slowRateX = Math.abs((paneCenterX - xCursor)/this.getSlowRangeRay());
            double slowRateY = Math.abs((paneCenterY - yCursor)/this.getSlowRangeRay());
            double speed = (this.getBaseMouvementSpeed() - (speedSlowMultiplier * Math.log(1 + this.getMass()))) * Math.max(slowRateX,slowRateY);
            return Math.max(speed,0) ;
        } else{
            double speed = (this.getBaseMouvementSpeed() - (speedSlowMultiplier * Math.log(1 + this.getMass())));
            return Math.max(speed,0) ;
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
