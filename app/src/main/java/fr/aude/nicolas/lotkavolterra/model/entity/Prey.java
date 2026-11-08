/*
 * Copyright (c) 2026 AUDE Nicolas
 * All rights reserved.
 *
 * This source code and its associated documentation are the
 * intellectual property of AUDE Nicolas .
 *
 * Unauthorized copying, reproduction, modification, distribution,
 * publication, or commercial use of this source code, in whole
 * or in part, is strictly prohibited without prior written
 * permission from the rights holder, except as permitted by law.
 *
 * This software is provided as proprietary software.
 * No license or rights are granted to third parties unless
 * explicitly authorized in writing by the rights holder.
 */
package fr.aude.nicolas.lotkavolterra.model.entity;
import fr.aude.nicolas.lotkavolterra.model.simulation.*;
import fr.aude.nicolas.lotkavolterra.model.behaviour.*;
import java.util.List;
import java.util.Random;
public class Prey extends Entity {
    private double x,y;
    private double speed;
    private boolean isAlive = true;
    private int hunger = 100;
    private int heat = 0;
    private int heat_limit = 60;
    private int hunger_limit = 0;
    private int hunger_update = 20;
    private boolean isInDanger = false;
    private Random rand;
    public Prey(List<Behaviour> behaviours,
                Simulation simulation,
                double x,
                double y,
                double speed,
                Random rand) {
                super(behaviours,simulation);
                this.x = x ; this.y = y ; this.speed = speed ;
                this.rand = rand;
    }
    public Prey(List<Behaviour> behaviours,
                Simulation simulation,
                double x,
                double y,
                double speed) {
                super(behaviours,simulation);
                this.x = x ; this.y = y ; this.speed = speed ;
                this.rand = new Random(System.nanoTime());
                this.heat = rand.nextInt(10);
    }
    public Prey(Prey other) {
            this(
                other.getBehaviours(),
                other.getSimulation(),
                other.x,
                other.y,
                other.speed
            );
    }
    public boolean isInHeat() {return heat>heat_limit;}
    public boolean isHungry() {return false;}
    public boolean isAlive() {return isAlive;}
    public boolean isInDanger() {return isInDanger;}
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
        double movement = speed*getSimulation().getDeltaTime();
        movement = Math.min(distance, movement);
        this.x += (dx/distance)*movement;
        this.y += (dy/distance)*movement;
    }
    public void lifeCycle() {
        if (!isAlive()) return;
        if (isInDanger()) {
            isInDanger = false;
            return;
        }
        Entity target = null;
        for (Behaviour behaviour : getBehaviours()) {
            target = behaviour.notify(this);
            if (target != null) {break;}
        }
        if (target == null) {
            moveAround();
        } else {
            follow(target,getSimulation().getDeltaTime());
        }
        heat++;
    }
    public void kill() {
        isAlive = false;
    }
    public void feed() {hunger += hunger_update;}
    public void justGiveBirth() {heat = 0;}
    public void escape(Entity e) {
        if (isInDanger) return;
        double dx = e.giveXDistance(this.x);
        double dy = e.giveYDistance(this.y);
        double distance = Math.sqrt(dx*dx+dy*dy);
        double movement = speed*getSimulation().getDeltaTime();
        movement = Math.min(distance, movement);
        this.x -= (dx/distance)*movement;
        this.y -= (dy/distance)*movement;
        isInDanger = true;
    }
    public boolean isStarving() {return hunger == 0;}
}
