package fr.aude.nicolas.lotkavolterra.model.entity;
import fr.aude.nicolas.lotkavolterra.model.simulation.*;
import fr.aude.nicolas.lotkavolterra.model.behaviour.*;
import java.util.List;
public abstract class Entity {
    private List<Behaviour> behaviours;
    private Simulation simulation;
    protected List<Behaviour> getBehaviours() {return this.behaviours;}
    protected Simulation getSimulation() {return simulation;}
    public Entity(
            List<Behaviour> behaviours,
            Simulation simulation
                ) {
        this.behaviours = behaviours;
        this.simulation = simulation;
    }
    public abstract boolean isInHeat();
    public abstract boolean isHungry();
    public abstract boolean isAlive();
    public abstract boolean isInDanger();
    public abstract double getX();
    public abstract double getY();
    public abstract double giveXDistance(double xStart);
    public abstract double giveXDistance(Entity e);
    public abstract double giveYDistance(double yStart);
    public abstract double giveYDistance(Entity e);
    public abstract void follow(Entity target, double deltaTime);
    public abstract void moveAround();
    public abstract void lifeCycle();
    public abstract void feed();
    public abstract void kill();
    public abstract boolean isStarving();
    public abstract void justGiveBirth();
    public abstract void escape(Entity e);
}
