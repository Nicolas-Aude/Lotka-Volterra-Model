package fr.aude.nicolas.lotkavolterra.model.behaviour;
import fr.aude.nicolas.lotkavolterra.model.simulation.*;
import fr.aude.nicolas.lotkavolterra.model.entity.*;
import java.util.function.Function;
import java.util.List;
public class EstrusBehaviour<T extends Entity> extends Behaviour {
    private Function<Entity, T> factory;
    private List<Entity> entities;
    public Entity notify(Entity e) {
        if (!e.isInHeat()) return null;
        Entity match = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Entity target : entities) {
            if (target == e) {continue;}
            double dx = target.giveXDistance(e); 
            double dy = target.giveYDistance(e);
            double distance = dx*dx+dy*dy;
            if (distance < minDistance && target.isInHeat()) {
                minDistance = distance;
                match = target;
            }
        }
        if (minDistance == 0) {
            T child = factory.apply(e);
            entities.add(child);
            e.justGiveBirth();
            match.justGiveBirth();
            return child;
        }
        return match;
    }
    public EstrusBehaviour(
            List<Entity> entities,
            Function<Entity, T> factory
            ) {
        this.entities = entities;
        this.factory = factory;
    }
}
