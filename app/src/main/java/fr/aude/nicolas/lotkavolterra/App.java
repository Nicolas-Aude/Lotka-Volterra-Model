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
package fr.aude.nicolas.lotkavolterra;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;
public class App {
    public static void main(String[] args) {
        int pop1Size = 100;
        int pop2Size = 2;
        List<Entity> predators = new ArrayList<>();
        List<Entity> prey = new ArrayList<>();
        HuntingBehaviour huntingBehaviour =
            new HuntingBehaviour(prey);
        HintBehaviour<Prey> preyHintBehaviour =
            new HintBehaviour<>(prey, e -> new Prey((Prey) e));
        HintBehaviour<Predator> predatorHintBehaviour =
            new HintBehaviour<>(predators, e -> new Predator((Predator) e));
        for (int i = 0 ; i < pop1Size ; i++) {
            prey.add(new Prey(null,
                              preyHintBehaviour,
                              0,
                              i,
                              1));
        }
        for (int i = 0 ; i < pop2Size ; i++) {
            predators.add(new Predator(huntingBehaviour,
                                        predatorHintBehaviour,
                                        1,
                                        i,
                                        2));
        }
        Simulation simulation = new Simulation(prey, predators);
    }
}
