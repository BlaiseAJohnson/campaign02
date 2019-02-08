package edu.isu.cs.cs3308;

/**
 * Drives an instance of the Simulation class.
 *
 * @author Blaise Johnson
 */
public class Driver {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(18, 10);

        simulation.runSimulation();
    }
}
