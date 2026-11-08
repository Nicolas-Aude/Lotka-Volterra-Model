
package fr.aude.nicolas.lotkavolterra.model.entity;

import fr.aude.nicolas.lotkavolterra.model.behaviour.Behaviour;
import fr.aude.nicolas.lotkavolterra.model.simulation.Simulation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class PredatorTest {

    private static class FakeSimulation implements Simulation {

        private final double deltaTime;

        FakeSimulation(double deltaTime) {
            this.deltaTime = deltaTime;
        }

        @Override
        public double getDeltaTime() {
            return deltaTime;
        }

        @Override
        public void notifyPrinters() {}

        @Override
        public void notifySimulationEnd() {}

        @Override
        public void start() {}

        @Override
        public int getFirstPopSize() {
            return 0;
        }

        @Override
        public int getSecondPopSize() {
            return 0;
        }

        @Override
        public double getCurrentTime() {
            return 0;
        }
    }

    private FakeSimulation simulation() {
        return new FakeSimulation(0.5);
    }

    private Predator createPredator(
            List<Behaviour> behaviours,
            double x,
            double y,
            double speed
    ) {
        return new Predator(
                behaviours,
                simulation(),
                x,
                y,
                speed,
                new Random(42)
        );
    }

    private Behaviour selfBehaviour() {
        return new Behaviour() {
            @Override
            public Entity notify(Entity e) {
                return e;
            }
        };
    }


    @Test
    public void constructorShouldInitializeCoordinates() {
        Predator predator = createPredator(
                new ArrayList<>(), 10, 20, 5
        );

        assertEquals(10, predator.getX(), 0.0001);
        assertEquals(20, predator.getY(), 0.0001);
        assertTrue(predator.isAlive());
    }

    @Test
    public void constructorWithRandomShouldAcceptProvidedRandom() {
        Random random = new Random(42);

        Predator predator = new Predator(
                new ArrayList<>(),
                simulation(),
                10, 20, 5,
                random
        );

        assertEquals(10, predator.getX(), 0.0001);
        assertEquals(20, predator.getY(), 0.0001);
    }

    @Test
    public void copyConstructorShouldCopyCoordinates() {
        Predator original = createPredator(
                new ArrayList<>(), 10, 20, 5
        );

        Predator copy = new Predator(original);

        assertEquals(original.getX(), copy.getX(), 0.0001);
        assertEquals(original.getY(), copy.getY(), 0.0001);
        assertEquals(original.isAlive(), copy.isAlive());
    }


    @Test
    public void predatorShouldInitiallyBeAlive() {
        Predator predator = createPredator(
                new ArrayList<>(), 0, 0, 1
        );

        assertTrue(predator.isAlive());
    }

    @Test
    public void killShouldSetAliveToFalse() {
        Predator predator = createPredator(
                new ArrayList<>(), 0, 0, 1
        );

        predator.kill();

        assertFalse(predator.isAlive());
    }

    @Test
    public void predatorShouldInitiallyNotBeHungry() {
        Predator predator = createPredator(
                new ArrayList<>(), 0, 0, 1
        );

        assertFalse(predator.isHungry());
    }

    @Test
    public void predatorShouldBecomeHungryBelowHungerLimit() {
        Predator predator = createPredator(
                List.of(selfBehaviour()), 0, 0, 1
        );

        for (int i = 0; i < 40; i++) {
            predator.lifeCycle();
        }

        assertFalse(predator.isHungry());

        predator.lifeCycle();

        assertTrue(predator.isHungry());
    }

    @Test
    public void predatorShouldEnterHeatAfterHeatLimit() {
        Predator predator = createPredator(
                List.of(selfBehaviour()), 0, 0, 1
        );

        for (int i = 0; i < 60; i++) {
            predator.lifeCycle();
        }

        assertFalse(predator.isInHeat());

        predator.lifeCycle();

        assertTrue(predator.isInHeat());
    }

    @Test
    public void predatorShouldInitiallyNotBeStarving() {
        Predator predator = createPredator(
                new ArrayList<>(), 0, 0, 1
        );

        assertFalse(predator.isStarving());
    }

    @Test
    public void predatorShouldStarveWhenHungerReachesZero() {
        Predator predator = createPredator(
                List.of(selfBehaviour()), 0, 0, 1
        );

        for (int i = 0; i < 100; i++) {
            predator.lifeCycle();
        }

        assertTrue(predator.isStarving());
    }

    @Test
    public void giveXDistanceShouldCalculateDistanceFromCoordinate() {
        Predator predator = createPredator(
                new ArrayList<>(), 10, 20, 1
        );

        assertEquals(5, predator.giveXDistance(5), 0.0001);
    }

    @Test
    public void giveYDistanceShouldCalculateDistanceFromCoordinate() {
        Predator predator = createPredator(
                new ArrayList<>(), 10, 20, 1
        );

        assertEquals(10, predator.giveYDistance(10), 0.0001);
    }

    @Test
    public void giveXDistanceShouldCalculateDistanceFromEntity() {
        Predator predator = createPredator(
                new ArrayList<>(), 10, 20, 1
        );

        Prey prey = new Prey(
                new ArrayList<>(), simulation(),
                5, 10, 1, new Random(42)
        );

        assertEquals(5, predator.giveXDistance(prey), 0.0001);
    }

    @Test
    public void giveYDistanceShouldCalculateDistanceFromEntity() {
        Predator predator = createPredator(
                new ArrayList<>(), 10, 20, 1
        );

        Prey prey = new Prey(
                new ArrayList<>(), simulation(),
                5, 10, 1, new Random(42)
        );

        assertEquals(10, predator.giveYDistance(prey), 0.0001);
    }

    @Test
    public void followShouldMoveTowardsTarget() {
        Predator predator = createPredator(
                new ArrayList<>(), 0, 0, 2
        );

        Prey prey = new Prey(
                new ArrayList<>(), simulation(),
                10, 0, 1, new Random(42)
        );

        predator.follow(prey, 1);

        assertEquals(2, predator.getX(), 0.0001);
        assertEquals(0, predator.getY(), 0.0001);
    }

    @Test
    public void followShouldStopAtTarget() {
        Predator predator = createPredator(
                new ArrayList<>(), 0, 0, 20
        );

        Prey prey = new Prey(
                new ArrayList<>(), simulation(),
                3, 4, 1, new Random(42)
        );

        predator.follow(prey, 1);

        assertEquals(3, predator.getX(), 0.0001);
        assertEquals(4, predator.getY(), 0.0001);
    }

    @Test
    public void followShouldNotMoveWhenTargetIsAtSamePosition() {
        Predator predator = createPredator(
                new ArrayList<>(), 5, 5, 2
        );

        Prey prey = new Prey(
                new ArrayList<>(), simulation(),
                5, 5, 1, new Random(42)
        );

        predator.follow(prey, 1);

        assertEquals(5, predator.getX(), 0.0001);
        assertEquals(5, predator.getY(), 0.0001);
    }

    @Test
    public void moveAroundShouldChangeCoordinates() {
        Predator predator = createPredator(
                new ArrayList<>(), 10, 10, 2
        );

        double initialX = predator.getX();
        double initialY = predator.getY();

        predator.moveAround();

        assertNotEquals(initialX, predator.getX(), 0.0001);
        assertNotEquals(initialY, predator.getY(), 0.0001);

        assertTrue(Double.isFinite(predator.getX()));
        assertTrue(Double.isFinite(predator.getY()));
    }

    @Test
    public void lifeCycleShouldDoNothingWhenPredatorIsDead() {
        Predator predator = createPredator(
                List.of(selfBehaviour()), 5, 5, 1
        );

        predator.kill();

        double initialX = predator.getX();
        double initialY = predator.getY();

        predator.lifeCycle();

        assertEquals(initialX, predator.getX(), 0.0001);
        assertEquals(initialY, predator.getY(), 0.0001);
    }

    @Test
    public void lifeCycleShouldFollowTargetWhenBehaviourFindsOne() {
        Predator predator = createPredator(
                new ArrayList<>(), 0, 0, 2
        );

        Prey prey = new Prey(
                new ArrayList<>(), simulation(),
                10, 0, 1, new Random(42)
        );

        Behaviour behaviour = new Behaviour() {
            @Override
            public Entity notify(Entity e) {
                return prey;
            }
        };

        predator = createPredator(
                List.of(behaviour), 0, 0, 2
        );

        predator.lifeCycle();

        assertTrue(predator.getX() > 0);
    }

    @Test
    public void lifeCycleShouldStopSearchingAfterFindingTarget() {
        int[] firstCalls = {0};
        int[] secondCalls = {0};

        Prey prey = new Prey(
                new ArrayList<>(), simulation(),
                10, 0, 1, new Random(42)
        );

        Behaviour first = new Behaviour() {
            @Override
            public Entity notify(Entity e) {
                firstCalls[0]++;
                return prey;
            }
        };

        Behaviour second = new Behaviour() {
            @Override
            public Entity notify(Entity e) {
                secondCalls[0]++;
                return null;
            }
        };

        Predator predator = createPredator(
                List.of(first, second), 0, 0, 2
        );

        predator.lifeCycle();

        assertEquals(1, firstCalls[0]);
        assertEquals(0, secondCalls[0]);
    }

    @Test
    public void feedShouldIncreaseHunger() {
        Predator predator = createPredator(
                List.of(selfBehaviour()), 0, 0, 1
        );

        for (int i = 0; i < 41; i++) {
            predator.lifeCycle();
        }

        assertTrue(predator.isHungry());

        predator.feed();

        assertFalse(predator.isHungry());
    }

    @Test
    public void feedShouldStopStarvation() {
        Predator predator = createPredator(
                List.of(selfBehaviour()), 0, 0, 1
        );

        for (int i = 0; i < 100; i++) {
            predator.lifeCycle();
        }

        assertTrue(predator.isStarving());

        predator.feed();

        assertFalse(predator.isStarving());
    }

    @Test
    public void justGiveBirthShouldResetHeat() {
        Predator predator = createPredator(
                List.of(selfBehaviour()), 0, 0, 1
        );

        for (int i = 0; i < 61; i++) {
            predator.lifeCycle();
        }

        assertTrue(predator.isInHeat());

        predator.justGiveBirth();

        assertFalse(predator.isInHeat());
    }

    @Test
    public void escapeShouldMoveAwayFromEntity() {
        Predator predator = createPredator(
                new ArrayList<>(), 10, 10, 2
        );

        Prey prey = new Prey(
                new ArrayList<>(), simulation(),
                12, 10, 1, new Random(42)
        );

        predator.escape(prey);

        assertEquals(9, predator.getX(), 0.0001);
        assertEquals(10, predator.getY(), 0.0001);
    }
}
