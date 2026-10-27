package fr.aude.nicolas.lotkavolterra;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Predator extends Entity {
    private double x,y;
    private double speed;
    private Behaviour eatingBehaviour;
    private Behaviour hintBehaviour;
    private boolean isAlive = true;
    private int hunger = 80;
    private int hit = 0;
    private Random rand;
    private double deltaTime = 1;
    public Predator(Behaviour eatingBehaviour,
                    Behaviour hintBehaviour, 
                    double x, 
                    double y, 
                    double speed) {
        this.eatingBehaviour = eatingBehaviour;
        this.hintBehaviour = hintBehaviour;
        this.x = x ; this.y = y ; this.speed = speed ;
        this.rand = new Random(System.nanoTime());
    }
    public Predator(Predator other) {
                    this(
                        other.eatingBehaviour,
                        other.hintBehaviour,
                        other.x,
                        other.y,
                        other.speed
                    );
                        }
    public boolean isHunting() {return hunger < 80;}
    public boolean isInHint() {return hit > 30;}
    public boolean isHungry() {return hunger < 80;}
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
    public void moveAround() {
        double dx = this.x + rand.nextDouble(this.speed);
        double dy = this.y + rand.nextDouble(this.speed);
        double distance = Math.sqrt(dx*dx+dy*dy);
        double movement = speed*deltaTime;
        movement = Math.min(distance, movement);
        this.x += (dx/distance)*movement;
        this.y += (dy/distance)*movement;
    }
    public void courtship() {}
    public void hunt() {}
    public void lifeCycle() {
        Entity target = null;
        if (isInHint()) {
            target = hintBehaviour.notify(this);
        } 
        if (isHunting()) {
            target = eatingBehaviour.notify(this);
        }
        if (target == null) {
            moveAround();
        } else {
            follow(target,deltaTime);
        }
        hunger--;
        hit++;
    }
    public void kill() {isAlive=false;}
    public void feed() {hunger +=20;}
    public void justGiveBirth() {hit = 0;}
}
