package fr.aude.nicolas.lotkavolterra;
import java.util.ArrayList;
import java.util.List;
public class Prey extends Entity {
    private double x,y;
    private double speed;
    private boolean isAlive = true;
    private int hunger = 100;
    private int hit = 0;
    private Behaviour eatingBehaviour;
    private Behaviour hintBehaviour;
    public Prey(Behaviour eatingBehaviour,
                    Behaviour hintBehaviour,
                    double x,
                    double y,
                    double speed) {
                this.eatingBehaviour = eatingBehaviour;
                this.hintBehaviour = hintBehaviour;
                this.x = x ; this.y = y ; this.speed = speed ;
    }
    public Prey(Prey other) {
            this(
                other.eatingBehaviour,
                other.hintBehaviour,
                other.x,
                other.y,
                other.speed
            );
    }
    public boolean isHunting() {return false;}
    public boolean isInHint() {return false;}
    public boolean isHungry() {return false;}
    public boolean isAlive() {return isAlive;}
    public double getX() {return this.x;}
    public double getY() {return this.y;}
    public double giveXDistance(double xStart) {return this.x - xStart;}
    public double giveXDistance(Entity e) {return this.x - e.getX();}
    public double giveYDistance(double yStart) {return this.y - yStart;}
    public double giveYDistance(Entity e) {return this.y - e.getY();}
    public void follow(Entity target, double deltaTime) {
        double dx = target.giveXDistance(this.x);
        double dy = target.giveYDistance(this.y);
        double distance = Math.sqrt(dx*dx+dy*dy);
        if (distance == 0) {return;}
        double movement = speed*deltaTime;
        movement = Math.min(distance, movement);
        this.x += (dx/distance)*movement;
        this.y += (dy/distance)*movement;
    }
    public void moveAround() {}
    public void courtship() {}
    public void hunt() {}
    public void lifeCycle() {
    }
    public void kill() {
        isAlive = false;
        System.out.println("Has been killed !!!! \n");
    }
    public void feed() {hunger += 20;}
    public void justGiveBirth() {hit = 0;}
}
