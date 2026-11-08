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
package fr.aude.nicolas.lotkavolterra.model.simulation;
import fr.aude.nicolas.lotkavolterra.model.entity.*;
import fr.aude.nicolas.lotkavolterra.model.behaviour.*;
import fr.aude.nicolas.lotkavolterra.output.printer.*;
import java.util.List;
public class FrequencedSimulation implements Runnable, Simulation {
    private int fps;
    private static final double gigaUnit = 1000000000;
    private static final double megaUnit = 1000000;
    private List<Entity> prey;
    private List<Entity> predators;
    private Thread thread;
    private List<SimulationPrinter> printers;
    private int round = 0;
    private boolean isRunning = true;
    public FrequencedSimulation(
            List<Entity> prey,
            List<Entity> predators,
            List<SimulationPrinter> printers
        ) {
        this.thread = new Thread(this);
        this.prey = prey;
        this.predators = predators;
        this.fps = 60;
        this.printers = printers;
    }
    public FrequencedSimulation(
            List<Entity> prey,
            List<Entity> predators,
            List<SimulationPrinter> printers,
            int fps
        ) {
        this.thread = new Thread(this);
        this.prey = prey;
        this.predators = predators;
        this.printers = printers;
        this.fps = fps;
    }
    public FrequencedSimulation(
            List<Entity> prey,
            List<Entity> predators
        ) {
        this.thread = new Thread(this);
        this.prey = prey;
        this.predators = predators;
        this.fps = 60;
        this.printers = null;
    }
    public FrequencedSimulation(
            List<Entity> prey,
            List<Entity> predators,
            int fps                                                
        ) {
        this.thread = new Thread(this);
        this.prey = prey;
        this.predators = predators;
        this.fps = fps;
        this.printers = null;
    }
    @Override
    public void run() {
        double drawInterval = gigaUnit/fps;
        double nextDrawTime = System.nanoTime() + drawInterval;
        notifyPrinters();
        while (isRunning) {
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/megaUnit;
                if(remainingTime < 0) {
                    remainingTime = 0;
                }
                for (Entity entity : List.copyOf(predators)) {
                    entity.lifeCycle();
                }
                for (Entity entity : List.copyOf(prey)) {
                    entity.lifeCycle();
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
                round++;
                notifyPrinters();
                if (
                        prey.size() == 0 
                    && predators.size() == 0
                ) {
                    isRunning = false;
                }
                System.out.println("Prey : " + prey.size() + " Predators : " + predators.size() +"\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifySimulationEnd();
    }
    @Override
    public double getDeltaTime(){return (double) 1.0/fps;}
    @Override
    public void notifyPrinters() {
        if (printers == null) return;
        for (SimulationPrinter printer : printers) {
            printer.update();
        }
    }
    @Override
    public void notifySimulationEnd() {
        if (printers == null) return;
        for (SimulationPrinter printer : printers) {
            printer.print();
        }
    }
    @Override
    public int getFirstPopSize() {return prey.size();}
    @Override
    public int getSecondPopSize() {return predators.size();}
    @Override
    public double getCurrentTime() {return (double) round/fps;}
    @Override
    public void start() {
        thread.start();
    }
}
