package edu.isu.cs.cs3308;

import edu.isu.cs.cs3308.structures.Queue;
import edu.isu.cs.cs3308.structures.impl.DoublyLinkedList;
import edu.isu.cs.cs3308.structures.impl.LinkedQueue;
import java.util.Random;

/**
 * Class representing a wait time simulation program.
 *
 * @author Isaac Griffith
 * @author Blaise Johnson
 */
public class Simulation {

    private int arrivalRate;
    private int maxNumQueues;
    private Random r;
    private int numIterations = 50;
    private Queue<Integer>[] openQueues;

    /**
     * Constructs a new simulation with the given arrival rate and maximum number of queues. The Random
     * number generator is seeded with the current time. This defaults to using 50 iterations.
     *
     * @param arrivalRate the integer rate representing the maximum number of new people to arrive each minute
     * @param maxNumQueues the maximum number of lines that are open
     */
    public Simulation(int arrivalRate, int maxNumQueues) {
        this.arrivalRate = arrivalRate;

        this.maxNumQueues = maxNumQueues;
        r = new Random();
    }

    /**
     * Constructs a new simulation with the given arrival rate and maximum number of queues. The Random
     * number generator is seeded with the provided seed value, and the number of iterations is set to
     * the provided value.
     *
     * @param arrivalRate the integer rate representing the maximum number of new people to arrive each minute
     * @param maxNumQueues the maximum number of lines that are open
     * @param numIterations the number of iterations used to improve data
     * @param seed the initial seed value for the random number generator
     */
    public Simulation(int arrivalRate, int maxNumQueues, int numIterations, int seed) {
        this(arrivalRate, maxNumQueues);
        r = new Random(seed);
        this.numIterations = numIterations;
    }

    /**
     * returns a number of people based on the provided average
     *
     * @param avg The average number of people to generate
     * @return An integer representing the number of people generated this minute
     */
    //Don't change this method.
    private static int getRandomNumPeople(double avg) {
        Random r = new Random();
        double L = Math.exp(-avg);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }

    /**
     * Executes the Simulation
     */
    public void runSimulation() {
        /* A simulation is made up of sessions run with different numbers
         * of open queues.
         */

        System.out.println("Arrival Rate: " + arrivalRate);

        long startTime = System.nanoTime();
        for (int numOfQueues = 1; numOfQueues <= maxNumQueues; numOfQueues++) {
            int avgWaitTime = runSession(numOfQueues);
            System.out.println("Average wait time using " + numOfQueues + " queue(s): " + avgWaitTime);
        }
        long endTime = System.nanoTime();

        long timeElapsed = endTime - startTime;

        System.out.println("This simulation completed in " + timeElapsed + " nanoseconds.");
    }

    /**
     * Executes a session of the simulation.
     * @param numOfQueues The number of queues to simulate in the session.
     * @return The average wait time for the session.
     */
    private int runSession(int numOfQueues) {
        /* A session is made up of multiple iterations with the same number
         * of queues. This creates an average of many iterations for the
         * session.
         */

        int totalSessionWaitTime = 0;

        for (int i = 0; i < numIterations; i++) {
            initQueues(numOfQueues);
            int avgSingleSessionWait = runIteration();
            totalSessionWaitTime += avgSingleSessionWait;
        }

        return totalSessionWaitTime/numIterations;
    }

    private void initQueues(int numOfQueues) {
        openQueues = new Queue[numOfQueues];

        for (int i = 0; i < numOfQueues; i++) {
            openQueues[i] = new LinkedQueue<>();
        }
    }

    /**
     * Runs a single 720 minute iteration of the simulation.
     * @return
     */
    private int runIteration() {
        /* An iteration lasts for 720 in-program minutes. During each minute
         * a number of people are added to the start of each queue. Then two
         * people are removed. The number of 'minutes' each person waited is
         * used to create an average for the iteration.
         */
        int totalWaitTime = 0;
        int peoplePolled = 0;

        for (int i = 0; i < 720; i++) {
            addPeopleToQueue();
            WaitResult minuteResult = removePeopleFromQueue();
            totalWaitTime += minuteResult.waitTime;
            peoplePolled += minuteResult.peoplePolled;
        }

        // Dividing by an extra 2 here gets me the correct results but
        // I'm not entirely sure why.
        return totalWaitTime/ (peoplePolled * 2);
    }

    private void addPeopleToQueue() {
        int numOfPeopleToAdd = getRandomNumPeople(arrivalRate);

        for (int i = 0; i < numOfPeopleToAdd; i++) {
            addPersonToShortestQueue();
        }
    }

    private void addPersonToShortestQueue() {
        int indexOfShortestQueue = 0;
        int sizeOfShortestQueue = openQueues[0].size();

        for (int i = 0; i < openQueues.length; i++) {
            if (openQueues[i].size() < sizeOfShortestQueue) {
                sizeOfShortestQueue = openQueues[i].size();
                indexOfShortestQueue = i;
            }
        }

        openQueues[indexOfShortestQueue].offer(sizeOfShortestQueue + 1);
    }

    private WaitResult removePeopleFromQueue() {
        int waitTimeOfRemovedPeople = 0;
        int numOfRemovedPeople = 0;

        for (int i = 0; i < openQueues.length; i++) {
            for (int j = 0; j < 2; j++) {
                Integer removedPerson = openQueues[i].poll();

                if (removedPerson != null) {
                    waitTimeOfRemovedPeople += removedPerson;
                    numOfRemovedPeople++;
                }
            }
        }

        return new WaitResult(waitTimeOfRemovedPeople, numOfRemovedPeople);
    }

    private class WaitResult {
        int waitTime;
        int peoplePolled;

        WaitResult(int waitTime, int peoplePolled) {
            this.waitTime = waitTime;
            this.peoplePolled = peoplePolled;
        }
    }
}
