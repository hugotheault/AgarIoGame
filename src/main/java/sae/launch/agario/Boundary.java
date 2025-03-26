package sae.launch.agario;

import sae.launch.agario.models.Entity;

public class Boundary {
    double x, y, width, height;

    public Boundary(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(Entity entity) {
        double ex = entity.getX();
        double ey = entity.getY();
        return (ex >= x && ex <= x + width && ey >= y && ey <= y + height);
    }

    public boolean intersects(Boundary other) {
        return !(other.x > x + width || other.x + other.width < x ||
                other.y > y + height || other.y + other.height < y);
    }
}