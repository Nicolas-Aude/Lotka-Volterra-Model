package fr.aude.nicolas.lotkavolterra;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;
public class HintBehaviour<T extends Entity> extends Behaviour {
    private Function<Entity, T> factory;
    private List<Entity> entities;
    public Entity notify(Entity e) {
        Entity match = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Entity target : entities) {
            if (target == e) {continue;}
            double dx = target.giveXDistance(e); 
            double dy = target.giveYDistance(e);
            double distance = dx*dx+dy*dy;
            if (distance < minDistance && target.isInHint()) {
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
    public HintBehaviour(
            List<Entity> entities,
            Function<Entity, T> factory
            ) {
        this.entities = entities;
        this.factory = factory;
    }
}
