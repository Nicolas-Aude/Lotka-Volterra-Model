package fr.aude.nicolas.lotkavolterra;
import java.util.ArrayList;
import java.util.List;
public abstract class Entity {
    public abstract boolean isHunting();
    public abstract boolean isInHint();
    public abstract boolean isHungry();
    public abstract boolean isAlive();
    public abstract double getX();
    public abstract double getY();
    public abstract double giveXDistance(double xStart);
    public abstract double giveXDistance(Entity e);
    public abstract double giveYDistance(double yStart);
    public abstract double giveYDistance(Entity e);
    public abstract void follow(Entity target, double deltaTime);
    public abstract void courtship();
    public abstract void hunt();
    public abstract void moveAround();
    public abstract void lifeCycle();
    public abstract void feed();
    public abstract void kill();
    public abstract void justGiveBirth();
}
