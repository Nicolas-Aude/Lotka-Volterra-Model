/*
 * Copyright (c) 2026 AUDE Nicolas
 * All rights reserved.
 *
 * This source code and its associated documentation are the
 * intellectual property of AUDE Nicolas.
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
package fr.aude.nicolas.lotkavolterra;

import java.util.ArrayList;
import java.util.List;

import fr.aude.nicolas.lotkavolterra.model.behaviour.Behaviour;
import fr.aude.nicolas.lotkavolterra.model.behaviour.HuntingBehaviour;
import fr.aude.nicolas.lotkavolterra.model.behaviour.EstrusBehaviour;
import fr.aude.nicolas.lotkavolterra.model.entity.Entity;
import fr.aude.nicolas.lotkavolterra.model.entity.Prey;
import fr.aude.nicolas.lotkavolterra.model.entity.Predator;
import fr.aude.nicolas.lotkavolterra.model.simulation.Simulation;
import fr.aude.nicolas.lotkavolterra.model.simulation.FrequencedSimulation;
import fr.aude.nicolas.lotkavolterra.output.printer.SimulationPrinter;
import fr.aude.nicolas.lotkavolterra.output.printer.PopulationSimulationPrinter;

public class App {

    public static void main(String[] args) {

        int pop1Size = 50;
        int pop2Size = 4;

        List<Entity> predators = new ArrayList<>();
        List<Entity> prey = new ArrayList<>();

        List<SimulationPrinter> printers = new ArrayList<>();

        Simulation simulation =
            new FrequencedSimulation(prey, predators, printers);

        printers.add(new PopulationSimulationPrinter(simulation));

        HuntingBehaviour huntingBehaviour =
            new HuntingBehaviour(prey, predators);

        EstrusBehaviour<Prey> preyEstrusBehaviour =
            new EstrusBehaviour<>(prey, e -> new Prey((Prey) e));

        EstrusBehaviour<Predator> predatorEstrusBehaviour =
            new EstrusBehaviour<>(predators, e -> new Predator((Predator) e));

        List<Behaviour> preyBehaviours = new ArrayList<>();
        preyBehaviours.add(preyEstrusBehaviour);

        List<Behaviour> predatorBehaviours = new ArrayList<>();
        predatorBehaviours.add(huntingBehaviour);
        predatorBehaviours.add(predatorEstrusBehaviour);

        for (int i = 0; i < pop1Size; i++) {
            prey.add(new Prey(
                preyBehaviours,
                simulation,
                0,
                (double) i / pop1Size,
                1
            ));
        }

        for (int i = 0; i < pop2Size; i++) {
            predators.add(new Predator(
                predatorBehaviours,
                simulation,
                1,
                (double) i / pop2Size,
                2 
            ));
        }

        simulation.start();
    }
}
