package sae.launch.agario.models;

public interface EntityFactory {
    Entity factory(int id, double x, double y, double masse);
}
