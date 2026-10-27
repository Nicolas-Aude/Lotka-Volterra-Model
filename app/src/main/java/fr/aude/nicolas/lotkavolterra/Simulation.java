package fr.aude.nicolas.lotkavolterra;
import java.util.List;
public class Simulation implements Runnable {
    private int fps = 60;
    private static final double gigaUnit = 1000000000;
    private static final double megaUnit = 1000000;
    private List<Entity> prey;
    private List<Entity> predators;
    private Thread thread;
    public Simulation(
            List<Entity> prey,
            List<Entity> predators
        ) {
        this.thread = new Thread(this);
        this.prey = prey;
        this.predators = predators;
        thread.start();
    }
    @Override
    public void run() {
        double drawInterval = gigaUnit/fps;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (true) {
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/megaUnit;
                if(remainingTime < 0) {
                    remainingTime = 0;
                }
                for (Entity entity : List.copyOf(predators)) {
                    entity.lifeCycle();
                }
                for (Entity entity : List.copyOf(prey)) {
                    entity.lifeCycle();
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
