package ss225bx;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/*
 * File:	MultithreadedService.java
 * Course: 	21HT - Operating Systems - 1DV512
 * Author: 	Saleh Shalabi ss225bx
 * Date: 	16/01/2022
 */



// You can implement additional fields and methods in code below, but
// you are not allowed to rename or remove any of it!

// Additionally, please remember that you are not allowed to use any third-party libraries

public class MultithreadedService {

    public class Task implements Runnable {

        int id;
        long burstTimeMs;
        long totSleepTime;
        long sleepTimeMs;
        long startTime;
        long st;
        long finishTime;
        boolean finishid = false;
        boolean interrupted = false;

        Task(int id, long burstTimeMs, long sleepTimeMs) {

            this.id = id;
            this.burstTimeMs = burstTimeMs;
            this.totSleepTime = 0;
            this.sleepTimeMs = sleepTimeMs;
        }
        
        public void run() {
            // System.out.println(id);
            // sleeps repeatedly untill it sleept as long as the brustTime (As I understood the task)
            startTime = System.currentTimeMillis() - st; // count the start time of the task
            while (totSleepTime < burstTimeMs) {
                try {
                    Thread.sleep(sleepTimeMs);
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                    interrupted = true; // assign as interupted task
                }
                totSleepTime += sleepTimeMs;
            }
            finishid = true;
            finishTime = System.currentTimeMillis() - st; // finish time of the task
        
        }

    }



    // Random number generator that must be used for the simulation
	Random rng;
    ArrayList<Task> totTasks = new ArrayList<>();

    // ... add further fields, methods, and even classes, if necessary
    

	public MultithreadedService (long rngSeed) {
        this.rng = new Random(rngSeed);
    }


	public void reset() {
        totTasks.clear();
    }
    

    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public void runNewSimulation(final long totalSimulationTimeMs,
        final int numThreads, final int numTasks,
        final long minBurstTimeMs, final long maxBurstTimeMs, final long sleepTimeMs) {
        reset();
        // I choose to create the tasks before the simulation starts 
        // because creating them in the simulation will take some time before it actually starts.
        for (int i = 0 ; i < numTasks + 1 ;i++) {
            long burstTimeMs = this.rng.nextLong(maxBurstTimeMs - minBurstTimeMs + 100) + minBurstTimeMs;
            Task task = new Task(i,burstTimeMs,sleepTimeMs);
            totTasks.add(task);
        }
        // all threads is ready before simulation start
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);

        long st = System.currentTimeMillis();
        int i = 0;
        while ((System.currentTimeMillis() - st) < totalSimulationTimeMs) {
            // assign the task to threads at the first loop then only loop until simulation time is done
            while (i < numTasks + 1) {
                for (Task t : totTasks) {
                    t.st = st; 
                    pool.execute(t); // giving the start time to each task to caclulate the startTime when its excuted
                    i++;
                }  
            }           
        }
        pool.shutdownNow(); 
        }

    


    public void printResults() {
        int i = 0;
        System.out.println("Completed tasks:");
        for (Task t : totTasks) { 
            if (t.finishid) {
                System.out.println("Task ID: " + t.id + ", burstTime: " + t.totSleepTime + ", StartTime: " + t.startTime + ", FinishTime: " + t.finishTime);
            }
        }
        
        System.out.println("Interrupted tasks:");
        for (Task t : totTasks) {
            if (t.interrupted) {
                if (i == 10) {
                    System.out.println();
                    i = 0;
                }
                System.out.print(" | " + t.id);
            }
            i++;
        }
        
        System.out.println();
        System.out.println("Waiting tasks:");
        for (Task t: totTasks) {
            if (!(t.finishid) && !(t.interrupted)) {   
                if (i == 10) {
                    System.out.println();
                    i = 0;
                }
            System.out.print(" | " + t.id);
            }
            i++;
        }
        System.out.println();

	}




    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public static void main(String args[]) {
		
		final long rngSeed = 19990330;  
				
        // Do not modify the code below — instead, complete the implementation
        // of other methods!
        MultithreadedService service = new MultithreadedService(rngSeed);
        
        final int numSimulations = 3;
        final long totalSimulationTimeMs = 15*1000L; // 15 seconds
        
        final int numThreads = 4;
        final int numTasks = 30;
        final long minBurstTimeMs = 1*1000L; // 1 second  
        final long maxBurstTimeMs = 10*1000L; // 10 seconds
        final long sleepTimeMs = 100L; // 100 ms

        for (int i = 0; i < numSimulations + 1; i++) {
            System.out.println("Running simulation #" + i);

            service.runNewSimulation(totalSimulationTimeMs,
                numThreads, numTasks,
                minBurstTimeMs, maxBurstTimeMs, sleepTimeMs);

            System.out.println("Simulation results:"
					+ "\n" + "----------------------");	
            service.printResults();

            System.out.println("\n");
        }

        System.out.println("----------------------");
        System.out.println("Exiting...");
        
        // If your program has not completed after the message printed above,
        // it means that some threads are not properly stopped! -> this issue will affect the grade
    }
}
