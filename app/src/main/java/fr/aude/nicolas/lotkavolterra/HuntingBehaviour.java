package fr.aude.nicolas.lotkavolterra;
import java.util.ArrayList;
import java.util.List;
public class HuntingBehaviour extends Behaviour {
    private List<Entity> prey;
    public Entity notify(Entity e) {
        Entity match = null;
        double minDistance = Double.POSITIVE_INFINITY;
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
        }
        return match;
    }
    public HuntingBehaviour(List<Entity> prey) {
            this.prey = prey;
    }
}
