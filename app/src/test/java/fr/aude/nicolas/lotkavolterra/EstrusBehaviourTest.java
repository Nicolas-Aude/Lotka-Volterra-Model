package fr.aude.nicolas.lotkavolterra.model.behaviour;

import fr.aude.nicolas.lotkavolterra.model.entity.Entity;
import fr.aude.nicolas.lotkavolterra.model.entity.Prey;
import fr.aude.nicolas.lotkavolterra.model.simulation.Simulation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class EstrusBehaviourTest {
    private static class FakeSimulation implements Simulation {
        @Override public double getDeltaTime() { return 0.5; }
        @Override public void notifyPrinters() {}
        @Override public void notifySimulationEnd() {}
        @Override public void start() {}
        @Override public int getFirstPopSize() { return 0; }
        @Override public int getSecondPopSize() { return 0; }
        @Override public double getCurrentTime() { return 0; }
    }

    private static Behaviour stayStill() {
        return new Behaviour() {
            @Override public Entity notify(Entity entity) { return entity; }
        };
    }

    private Prey prey(double x, double y) {
        return new Prey(new ArrayList<>(List.of(stayStill())),
                new FakeSimulation(), x, y, 1, new Random(2));
    }

    private void putInHeat(Prey prey) {
        for (int i = 0; i < 61; i++) prey.lifeCycle();
        assertTrue(prey.isInHeat());
    }

    @Test public void notifyReturnsNullWhenEntityIsNotInHeat() {
        Prey subject = prey(0, 0);
        List<Entity> population = new ArrayList<>(List.of(subject));
        EstrusBehaviour<Prey> behaviour = new EstrusBehaviour<>(population, parent -> new Prey((Prey) parent));
        assertNull(behaviour.notify(subject));
    }

    @Test public void notifyReturnsNearestMateInHeat() {
        Prey subject = prey(0, 0);
        Prey nearest = prey(4, 0);
        Prey farther = prey(9, 0);
        putInHeat(subject);
        putInHeat(nearest);
        putInHeat(farther);
        List<Entity> population = new ArrayList<>(List.of(subject, farther, nearest));
        EstrusBehaviour<Prey> behaviour = new EstrusBehaviour<>(population, parent -> new Prey((Prey) parent));

        assertSame(nearest, behaviour.notify(subject));
    }

    @Test public void notifyIgnoresEntitiesNotInHeat() {
        Prey subject = prey(0, 0);
        Prey notReady = prey(1, 0);
        putInHeat(subject);
        List<Entity> population = new ArrayList<>(List.of(subject, notReady));
        EstrusBehaviour<Prey> behaviour = new EstrusBehaviour<>(population, parent -> new Prey((Prey) parent));

        assertNull(behaviour.notify(subject));
    }

    @Test public void notifyCreatesChildWhenMatesOverlap() {
        Prey subject = prey(0, 0);
        Prey mate = prey(0, 0);
        putInHeat(subject);
        putInHeat(mate);
        List<Entity> population = new ArrayList<>(List.of(subject, mate));
        AtomicInteger factoryCalls = new AtomicInteger();
        EstrusBehaviour<Prey> behaviour = new EstrusBehaviour<>(population, parent -> {
            factoryCalls.incrementAndGet();
            return new Prey((Prey) parent);
        });

        Entity child = behaviour.notify(subject);

        assertNotNull(child);
        assertEquals(1, factoryCalls.get());
        assertEquals(3, population.size());
        assertFalse(subject.isInHeat());
        assertFalse(mate.isInHeat());
    }

    @Test public void notifyReturnsNullWhenThereIsNoOtherEntityInHeat() {
        Prey subject = prey(0, 0);
        putInHeat(subject);
        List<Entity> population = new ArrayList<>(List.of(subject));
        EstrusBehaviour<Prey> behaviour = new EstrusBehaviour<>(population, parent -> new Prey((Prey) parent));

        assertNull(behaviour.notify(subject));
    }
}
