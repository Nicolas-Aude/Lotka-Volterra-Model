package fr.aude.nicolas.lotkavolterra.output.printer;
import fr.aude.nicolas.lotkavolterra.model.simulation.*;
import fr.aude.nicolas.lotkavolterra.model.entity.*;
import fr.aude.nicolas.lotkavolterra.model.behaviour.*;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PopulationSimulationPrinter implements SimulationPrinter {

    private Simulation simulation;
    private List<Integer> firstPopSize;
    private List<Integer> secondPopSize;
    private List<Double> time;

    public PopulationSimulationPrinter(Simulation simulation) {
        this.simulation = simulation;
        this.firstPopSize = new ArrayList<>();
        this.secondPopSize = new ArrayList<>();
        this.time = new ArrayList<>();
    }

    @Override
    public void update() {
        firstPopSize.add(simulation.getFirstPopSize());
        secondPopSize.add(simulation.getSecondPopSize());
        time.add(simulation.getCurrentTime());
    }

    @Override
    public void print() {
        String filename = "simulation.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Time,FirstPopulation,SecondPopulation");
            writer.newLine();
            for (int i = 0; i < time.size(); i++) {
                writer.write(
                    time.get(i) + ";" +
                    firstPopSize.get(i) + ";" +
                    secondPopSize.get(i)
                );
                writer.newLine();
            }
            System.out.println("Données exportées dans " + filename);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'export CSV : " + e.getMessage());
        }
    }
}
