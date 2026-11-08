package fr.aude.nicolas.lotkavolterra.model.entity;

import fr.aude.nicolas.lotkavolterra.model.behaviour.Behaviour;
import fr.aude.nicolas.lotkavolterra.model.simulation.Simulation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class PreyTest {
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

    private Prey newPrey(double x, double y, double speed, Behaviour... behaviours) {
        return new Prey(new ArrayList<>(List.of(behaviours)),
                new FakeSimulation(), x, y, speed, new Random(42));
    }

    @Test public void constructorInitializesPositionAndAliveState() {
        Prey prey = newPrey(3, 4, 2);
        assertEquals(3, prey.getX(), 0.0);
        assertEquals(4, prey.getY(), 0.0);
        assertTrue(prey.isAlive());
        assertFalse(prey.isInDanger());
        assertFalse(prey.isHungry());
        assertFalse(prey.isStarving());
    }

    @Test public void distanceMethodsReturnSignedDifferences() {
        Prey prey = newPrey(8, 5, 1);
        Prey other = newPrey(3, 2, 1);
        assertEquals(5, prey.giveXDistance(3), 0.0);
        assertEquals(5, prey.giveXDistance(other), 0.0);
        assertEquals(3, prey.giveYDistance(2), 0.0);
        assertEquals(3, prey.giveYDistance(other), 0.0);
    }

    @Test public void followMovesTowardTargetBySpeedTimesDelta() {
        Prey prey = newPrey(0, 0, 2);
        Prey target = newPrey(10, 0, 1);
        prey.follow(target, 1);
        assertEquals(2, prey.getX(), 1e-9);
        assertEquals(0, prey.getY(), 1e-9);
    }

    @Test public void followDoesNotMoveWhenAlreadyAtTarget() {
        Prey prey = newPrey(2, 3, 2);
        Prey target = newPrey(2, 3, 1);
        prey.follow(target, 1);
        assertEquals(2, prey.getX(), 0.0);
        assertEquals(3, prey.getY(), 0.0);
    }

    @Test public void followDoesNotOvershootTarget() {
        Prey prey = newPrey(0, 0, 20);
        prey.follow(newPrey(3, 4, 1), 1);
        assertEquals(3, prey.getX(), 1e-9);
        assertEquals(4, prey.getY(), 1e-9);
    }

    @Test public void moveAroundChangesPositionAwayFromOrigin() {
        Prey prey = newPrey(10, 10, 2);
        prey.moveAround();
        assertNotEquals(10, prey.getX(), 0.0);
        assertNotEquals(10, prey.getY(), 0.0);
    }

    @Test public void lifecycleIncrementsHeatAndDoesNotChangeHungerState() {
        Prey prey = newPrey(10, 10, 2, stayStill());
        for (int i = 0; i < 60; i++) {
            prey.lifeCycle();
        }
        assertFalse(prey.isInHeat());
        prey.lifeCycle();
        assertTrue(prey.isInHeat());
        assertFalse(prey.isHungry());
    }

    @Test public void deadPreyDoesNotMoveOrAdvanceLifecycle() {
        Prey prey = newPrey(10, 10, 2, stayStill());
        prey.kill();
        prey.lifeCycle();
        assertFalse(prey.isAlive());
        assertEquals(10, prey.getX(), 0.0);
        assertEquals(10, prey.getY(), 0.0);
        assertFalse(prey.isInHeat());
    }

    @Test public void escapeMovesAwayAndSetsDanger() {
        Prey prey = newPrey(10, 10, 2);
        Prey threat = newPrey(12, 10, 1);
        prey.escape(threat);
        assertEquals(9, prey.getX(), 1e-9);
        assertEquals(10, prey.getY(), 1e-9);
        assertTrue(prey.isInDanger());
    }

    @Test public void escapeIsIgnoredWhileAlreadyInDanger() {
        Prey prey = newPrey(10, 10, 2);
        Prey threat = newPrey(12, 10, 1);
        prey.escape(threat);
        double xAfterFirstEscape = prey.getX();
        double yAfterFirstEscape = prey.getY();
        prey.escape(threat);
        assertEquals(xAfterFirstEscape, prey.getX(), 0.0);
        assertEquals(yAfterFirstEscape, prey.getY(), 0.0);
    }

    @Test public void lifecycleClearsDangerAndSkipsOtherUpdatesForThatTick() {
        Prey prey = newPrey(10, 10, 2, stayStill());
        prey.escape(newPrey(12, 10, 1));
        double x = prey.getX();
        double y = prey.getY();
        prey.lifeCycle();
        assertFalse(prey.isInDanger());
        assertEquals(x, prey.getX(), 0.0);
        assertEquals(y, prey.getY(), 0.0);
        assertFalse(prey.isInHeat());
    }

    @Test public void justGiveBirthResetsHeat() {
        Prey prey = newPrey(10, 10, 2, stayStill());
        for (int i = 0; i < 61; i++) prey.lifeCycle();
        assertTrue(prey.isInHeat());
        prey.justGiveBirth();
        assertFalse(prey.isInHeat());
    }

    @Test public void copyConstructorCopiesPositionAndStartsAlive() {
        Prey original = newPrey(7, 9, 2, stayStill());
        Prey copy = new Prey(original);
        assertEquals(7, copy.getX(), 0.0);
        assertEquals(9, copy.getY(), 0.0);
        assertTrue(copy.isAlive());
    }
}
