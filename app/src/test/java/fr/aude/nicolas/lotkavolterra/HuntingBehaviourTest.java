package fr.aude.nicolas.lotkavolterra.model.behaviour;

import fr.aude.nicolas.lotkavolterra.model.entity.Entity;
import fr.aude.nicolas.lotkavolterra.model.entity.Predator;
import fr.aude.nicolas.lotkavolterra.model.entity.Prey;
import fr.aude.nicolas.lotkavolterra.model.simulation.Simulation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class HuntingBehaviourTest {
    private static class FakeSimulation implements Simulation {
        @Override public double getDeltaTime() { return 0.5; }
        @Override public void notifyPrinters() {}
        @Override public void notifySimulationEnd() {}
        @Override public void start() {}
        @Override public int getFirstPopSize() { return 0; }
        @Override public int getSecondPopSize() { return 0; }
        @Override public double getCurrentTime() { return 0; }
    }

    private Prey prey(double x, double y) {
        return new Prey(new ArrayList<>(), new FakeSimulation(), x, y, 2, new Random(1));
    }

    private Predator predator(double x, double y) {
        Behaviour stayStill = new Behaviour() {
            @Override public Entity notify(Entity entity) { return entity; }
        };
        return new Predator(new ArrayList<>(List.of(stayStill)),
                new FakeSimulation(), x, y, 2, new Random(1));
    }

    @Test public void notifyReturnsNullWhenPredatorIsNotHungry() {
        List<Entity> prey = new ArrayList<>();
        prey.add(prey(5, 0));
        Predator hunter = predator(0, 0);
        HuntingBehaviour behaviour = new HuntingBehaviour(prey, new ArrayList<>(List.of(hunter)));
        assertNull(behaviour.notify(hunter));
        assertEquals(1, prey.size());
    }

    @Test public void notifyReturnsClosestPreyAndMakesItEscape() {
        Entity near = prey(3, 0);
        Entity far = prey(8, 0);
        List<Entity> preys = new ArrayList<>(List.of(far, near));
        Predator hunter = predator(0, 0);
        for (int i = 0; i < 41; i++) {
            hunter.lifeCycle();
        }
        HuntingBehaviour behaviour = new HuntingBehaviour(preys, new ArrayList<>(List.of(hunter)));
        assertSame(near, behaviour.notify(hunter));
        assertTrue(near.isInDanger());
        assertEquals(2, preys.size());
    }

    @Test public void notifyKillsPreyAndFeedsPredatorWhenTheyOverlap() {
        Entity victim = prey(0, 0);
        List<Entity> preys = new ArrayList<>(List.of(victim));
        Predator hunter = predator(0, 0);
        for (int i = 0; i < 41; i++) hunter.lifeCycle();
        HuntingBehaviour behaviour = new HuntingBehaviour(preys, new ArrayList<>(List.of(hunter)));

        assertSame(hunter, behaviour.notify(hunter));
        assertFalse(victim.isAlive());
        assertTrue(preys.isEmpty());
        assertFalse(hunter.isHungry());
    }

    @Test public void notifyKillsAndRemovesStarvingPredator() {
        Predator hunter = predator(0, 0);
        List<Entity> predators = new ArrayList<>(List.of(hunter));
        List<Entity> preys = new ArrayList<>();
        Behaviour inert = new Behaviour() {
            @Override public Entity notify(Entity entity) { return entity; }
        };
        hunter = new Predator(new ArrayList<>(List.of(inert)), new FakeSimulation(),
                0, 0, 1, new Random(1));
        predators.clear();
        predators.add(hunter);
        for (int i = 0; i < 100; i++) hunter.lifeCycle();

        HuntingBehaviour behaviour = new HuntingBehaviour(preys, predators);
        assertNull(behaviour.notify(hunter));
        assertFalse(hunter.isAlive());
        assertTrue(predators.isEmpty());
    }

    @Test public void notifyReturnsNullWhenNoPreyIsAvailable() {
        Predator hunter = predator(0, 0);
        for (int i = 0; i < 41; i++) hunter.lifeCycle();
        HuntingBehaviour behaviour = new HuntingBehaviour(new ArrayList<>(),
                new ArrayList<>(List.of(hunter)));
        assertNull(behaviour.notify(hunter));
    }
}
