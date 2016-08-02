//  MultithreadedEvaluator.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2013 Antonio J. Nebro
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package evochecker.genetic.jmetal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.jmetal.metaheuristics.IParallelEvaluator;
import jmetal.core.Problem;
import jmetal.core.Solution;

/**
 * Class representing a parallel evaluator
 * @author sgerasimou
 *
 */
public class MultiProcessEvaluator implements IParallelEvaluator {
	
	/** number of parallel executions (processes)*/
	private int numberOfProcesses;
	
	/** handler to the problem being solved*/
	private Problem problem;
	
	
	private Problem[] problems;

	/** List of solutions*/
	private List<Solution> solutionsList;
		
	/** Solution results list*/
	private CopyOnWriteArrayList<Solution> results;

	/** Socket array keeping the prism instances*/
	private Socket[] socket = null;
	
	/** Array of threads*/
	private Thread[] threads;
	
	/** Array of runnables*/
	private RunnableExecutor[] runnables;

	/** Starting port*/
	static int portId = 8880;

	/** Output array*/
	private PrintWriter[] out;
	
	/** Input array*/
	private BufferedReader[] in;
	
	
	/**
	 * Constructor
	 * @param processes
	 */
	public MultiProcessEvaluator(int processes) {
		if (processes <= 0) {
			String processesNum = Utility.getProperty("PROCESSORS");
			if (processesNum!=null)
				numberOfProcesses = Integer.parseInt(processesNum);
			else if (processesNum == null || processesNum.equals("-1"))
				numberOfProcesses = Runtime.getRuntime().availableProcessors();
		} else {
			numberOfProcesses = processes;
		}
		this.startExecutors();
		this.init();
//		this.reset();
	}

	
	/** 
	 * Initialise executors
	 */
	private void startExecutors() {
		try {
			System.out.println("Initialization");
			socket 	= new Socket[numberOfProcesses];
			in 		= new BufferedReader[numberOfProcesses];
			out 	= new PrintWriter[numberOfProcesses];
			
			int initPort = Integer.parseInt(Utility.getProperty("INIT_PORT_NUM"));
						
			String params[] = new String[4];
			params[0] = Utility.getProperty("JVM");
			params[1] = "-jar";
			params[2] = Utility.getProperty("EXECUTOR_PATH", "repo/PRISM-PSY-fat.jar");
			for (int i = 0; i < numberOfProcesses; i++) {
				
				boolean isAlive = false;
				int portNum = initPort+i;// portId++;
				System.out.println("Starting server at port " + portNum);
				params[3] = String.valueOf(portNum);
				do {
					Process p = Runtime.getRuntime().exec(params);
					Thread.sleep(1000);
					isAlive = p.isAlive();
				} 
				while (!isAlive);

				System.out.println("Connecting");
//				socket[i] = new Socket("127.0.0.1", portNum);
//				in[i] = new BufferedReader(new InputStreamReader(socket[i].getInputStream()));
//				out[i] = new PrintWriter(socket[i].getOutputStream());
				 makeConnection(portNum, i, params);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	/** 
	 * Try to make a connection until finding an open port
	 * @param portNum
	 * @param i
	 * @param params
	 */
	private void makeConnection(int portNum, int i, String[] params){
		try{
			socket[i] 	= new Socket("127.0.0.1", portNum);
			in[i] 		= new BufferedReader(new InputStreamReader(socket[i].getInputStream()));
			out[i] 		= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket[i].getOutputStream())),true);
		} catch (IOException e) {
			try {
				Thread.sleep(2000);
				Process p = Runtime.getRuntime().exec(params);
			} catch (InterruptedException | IOException e1) {
				e1.printStackTrace();
			}			
			makeConnection(portNum, i, params);
		}
	}

	
	/** 
	 * Initialise evaluator 
	 */
	public void startEvaluator(Problem problem) {
		System.out.println("Cores: " + numberOfProcesses);
		this.problem = problem;

		try {
		
			problems = new Problem[numberOfProcesses];
			if (problem instanceof GeneticProblemPSY){
				for (int i=0; i<numberOfProcesses; i++){
							problems[i] = new GeneticProblemPSY((GeneticProblemPSY) problem);
				}
			}
			else if (problem instanceof GeneticProblem){
				for (int i=0; i<numberOfProcesses; i++){
					problems[i] = new GeneticProblem((GeneticProblem) problem);
				}
			}
		}
		catch (EvoCheckerException e) {
			e.printStackTrace();
		}
			
	}

	
	/** 
	 * Add the solution to the list of solutions to be evaluated
	 */
	public void addSolutionForEvaluation(Solution solution) {
//		 System.out.println("Adding a solution to be evaluated");
		solutionsList.add(solution);
	}

	
	/**
	 * Start parallel execution
	 */
	private void startThreads() {
		for (Thread t : this.threads) {
			t.start();
		}

//		System.out.println("Thread started....");

		for (Thread t : this.threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Assign solutions to parallel processes
	 */
	private void assignSolutions() {
		for (int i = 0; i < this.solutionsList.size(); i++) {
//			System.out.println("Assigning tasks");
			this.runnables[i % this.runnables.length].addSolutionForEvaluation(this.solutionsList
					.get(i));
		}
	}

	
	private void init(){
		threads 		= new Thread[numberOfProcesses];
		runnables 		= new RunnableExecutor[numberOfProcesses];
		solutionsList 	= new ArrayList<Solution>();
	}
	
	/**
	 * When done, reset the evaluators
	 */
	private void reset() {	
		for (int i = 0; i < numberOfProcesses; i++) {
			runnables[i]	= new RunnableExecutor(out[i], in[i], problems[i]);
			threads[i] 		= new Thread(runnables[i]);
		}
	}

	
	/**
	 * Run parallel evaluation
	 */
	public List<Solution> parallelEvaluation() {
//		System.out.println("Parallel evaluation");
		results = new CopyOnWriteArrayList<Solution>();
		this.reset();
		this.assignSolutions();
		this.startThreads();
		solutionsList.clear();
//		System.out.println("End of parallel evaluation....");
		return this.results;
	}

	
	/**
	 * Once finished, stop the evaluators
	 */
	public void stopEvaluators() {
		for (int i = 0; i < this.numberOfProcesses; i++) {
			try {
				this.in[i].close();
				this.out[i].close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

		
	
	/**
	 * Inner class
	 * @author sgerasimou
	 *
	 */
	private class RunnableExecutor implements Runnable {
		/** List of solutions to be evaluated*/
 		private List<Solution> solution = new ArrayList<Solution>();
 		
 		/** Output*/
		private PrintWriter out;
		
		/** Input*/
		private BufferedReader in;

		Problem runnableProblem;
		
		/**
		 * Class constructor: create a new runnable executor
		 * @param out
		 * @param in
		 */
		public RunnableExecutor(PrintWriter out, BufferedReader in, Problem problem) {
			this.in 			 = in;
			this.out 			 = out;
			this.runnableProblem = problem;
			this.solution 		 = new ArrayList<Solution>();
		}

		
		/** Add a solution for evaluation*/
		public void addSolutionForEvaluation(Solution solution) {
			this.solution.add(solution);
		}

		
		/**
		 * Run
		 */
		@Override
		public void run() {
			
			for (Solution task : this.solution) {
				try {
//					 System.out.println("Running thread...." + this.hashCode());
					if (runnableProblem instanceof GeneticModelProblem){
						((GeneticModelProblem) runnableProblem).parallelEvaluate(in, out, task);
					}
//					if (problem instanceof GeneticModelProblem){
//						((GeneticModelProblem) problem).parallelEvaluate(in, out, task);
//					}
					else throw new IllegalArgumentException("Problem not recognised");
				} catch (Exception e) {
					e.printStackTrace();
				}
//				 System.out.println("Adding result");
				results.add(task);
			}
		}
	}
}
