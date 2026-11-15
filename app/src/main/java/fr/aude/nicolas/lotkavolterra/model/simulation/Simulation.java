package fr.aude.nicolas.lotkavolterra.model.simulation;
public interface Simulation {
    public double getDeltaTime();
    public void notifyPrinters();
    public void notifySimulationEnd();
    public void start();
    public int getFirstPopSize();
    public int getSecondPopSize();
    public double getCurrentTime();
}
