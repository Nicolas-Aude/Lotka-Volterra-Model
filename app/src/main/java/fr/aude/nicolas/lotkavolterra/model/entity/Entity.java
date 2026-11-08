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
