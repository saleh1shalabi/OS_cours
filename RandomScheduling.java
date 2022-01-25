

import java.util.ArrayList;
import java.util.Random;

/*
 * File:	RandomScheduling.java
 * Course: 	20HT - Operating Systems - 1DV512
 * Author: 	Saleh Shalabi, ss225bx
 * Date: 	Dec 2021
 */

public class RandomScheduling {
	
	public static class ScheduledProcess {
		int processId;
		int burstTime;
		int arrivalMoment;
		
		// The total time the process has waited since its arrival
		int totalWaitingTime;
		
		// The total CPU time the process has used so far
		// (when equal to burstTime -> the process is complete!)
		int allocatedCpuTime;

		public ScheduledProcess(int processId, int burstTime, int arrivalMoment) {
			this.processId = processId;
			this.burstTime = burstTime;
			this.arrivalMoment = arrivalMoment;
			this.allocatedCpuTime = 0;
			this.totalWaitingTime = 0;
		}
	}
		
	// Random number generator that must be used for the simulation
	Random rng;

	int tick = 0;
	ArrayList<ScheduledProcess> completeProcesses = new ArrayList<>();
	ArrayList<ScheduledProcess> processes = new ArrayList<>();
	ScheduledProcess runningProcess = null;
	int runTimes = 0;

	// ... add further fields and methods, if necessary

	public void chooseProcess() {
		runningProcess = processes.get(rng.nextInt(processes.size()));
	}
		
	public RandomScheduling(long rngSeed) {
		this.rng = new Random(rngSeed);
	}
	
	public void reset() {
		tick = 0;
		processes.clear();
		completeProcesses.clear();
		runningProcess = null;
		runTimes = 0;
	}
	
	public void runNewSimulation(final boolean isPreemptive, final int timeQuantum,
	    final int numProcesses,
		final int minBurstTime, final int maxBurstTime,
		final int maxArrivalsPerTick, final double probArrival) {
		reset();
		
		while (completeProcesses.size() != numProcesses) {
			int createdThisTick = 0;

			// only if total sum of processes are not 10 yet then we can look if it will arrive any new ones
			if (processes.size() + completeProcesses.size() < numProcesses) { 
				while (rng.nextDouble() <= probArrival) {
					// int burstTime = this.rng.nextInt(maxBurstTime - minBurstTime + 1) + minBurstTime;
					int burstTime = rng.nextInt(minBurstTime, maxBurstTime + 1);
					ScheduledProcess scheduledProcess = new ScheduledProcess(processes.size() + completeProcesses.size(), burstTime, tick);
					processes.add(scheduledProcess);
					createdThisTick++;	
					if (createdThisTick == maxArrivalsPerTick | processes.size() + completeProcesses.size() == numProcesses) {
						break;
					}
				}
			}
			if (processes.size() == 0) { // non of the code bellow will be rund if no processes have been created. 
			} else {
				if (isPreemptive) {
					if (runTimes == timeQuantum) {
						runningProcess = null;
						runTimes = 0;
					} if (runningProcess == null) {
						chooseProcess();
						runTimes = 0;
					} if (runningProcess.allocatedCpuTime < runningProcess.burstTime) {
						runningProcess.allocatedCpuTime++;
						runTimes++;
						for (ScheduledProcess s : processes) {
							if (!(s.equals(runningProcess))) {
								s.totalWaitingTime++;
							}
						}
					} if (runningProcess.allocatedCpuTime == runningProcess.burstTime) {
						processes.remove(runningProcess);
						completeProcesses.add(runningProcess);
						runningProcess = null;
						runTimes = 0;
					}
				} else {
					if (runningProcess == null) {
						chooseProcess();
					} if (runningProcess.allocatedCpuTime < runningProcess.burstTime) {
						runningProcess.allocatedCpuTime++;
						for (ScheduledProcess s : processes) {
							if (!(s.equals(runningProcess))) {
								s.totalWaitingTime++;
							}
						}
					} if (runningProcess.allocatedCpuTime == runningProcess.burstTime) {
						processes.remove(runningProcess);
						completeProcesses.add(runningProcess);
						runningProcess = null;
					}
				}
			}
			tick++;	
		}
	}

	public void printResults() {
		double averge = 0;
		for (ScheduledProcess s : completeProcesses) {
			System.out.println("ProcessId: " + s.processId + " BurstTime: " + s.burstTime + " Arraival Moment: " + s.arrivalMoment + " Total Waiting Time: " + s.totalWaitingTime);
			averge += s.totalWaitingTime;
		}
		System.out.println("Averge Waiting Time: " + (averge/10));
		System.out.println("Total time: " + tick);
	}
		
	
	public static void main(String args[]) {
		final long rngSeed = 19990330;  

		RandomScheduling scheduler = new RandomScheduling(rngSeed);
		
		final int numSimulations = 5;
		
		final int numProcesses = 10;
		final int minBurstTime = 2;
		final int maxBurstTime = 10;
		final int maxArrivalsPerTick = 2;
		final double probArrival = 0.75;
		
		final int timeQuantum = 2;

		boolean[] preemptionOptions = {false, true};

		for (boolean isPreemptive: preemptionOptions) {

			for (int i = 0; i < numSimulations; i++) {
				System.out.println("Running " + ((isPreemptive) ? "preemptive" : "non-preemptive")
					+ " simulation #" + i);

				scheduler.runNewSimulation(
					isPreemptive, timeQuantum,
					numProcesses,
					minBurstTime, maxBurstTime,
					maxArrivalsPerTick, probArrival);

				System.out.println("Simulation results:"
					+ "\n" + "----------------------");	
				scheduler.printResults();

				System.out.println("\n");
			}
		}		
	}
	
}