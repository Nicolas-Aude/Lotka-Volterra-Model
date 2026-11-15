package fr.aude.nicolas.lotkavolterra.model.behaviour;
import fr.aude.nicolas.lotkavolterra.model.simulation.*;
import fr.aude.nicolas.lotkavolterra.model.entity.*;
import java.util.List;
public class HuntingBehaviour extends Behaviour {
    private List<Entity> prey;
    private List<Entity> predators;
    public Entity notify(Entity e) {
        if (!e.isHungry()) return null;
        Entity match = null;
        double minDistance = Double.POSITIVE_INFINITY;
        if (e.isStarving()) {
            predators.remove(e);
            e.kill();
            return null;
        }
        for (Entity target : prey) {
            if (target == e) {continue;}
            double dx = target.giveXDistance(e);
            double dy = target.giveYDistance(e);
            double distance = dx*dx+dy*dy;
            if (distance < minDistance) {
                minDistance = distance;
                match = target;
            }
        }
       if (minDistance == 0) {
            e.feed();
            prey.remove(match);
            match.kill();
            return e;
        }
        if (match != null) {
            match.escape(e);
        }
        return match;
    }
    public HuntingBehaviour(List<Entity> prey, List<Entity> predators) {
        this.prey = prey;
        this.predators = predators;
    }
}
