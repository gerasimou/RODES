//==============================================================================
//	
//	Copyright (c) 2015-
//	Authors:
//	* Simos Gerasimou (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of EvoChecker.
//	
//==============================================================================

package evochecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import evochecker.auxiliary.Utility;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.jmetal.GeneticProblem;
import evochecker.genetic.jmetal.GeneticProblemPSY;
import evochecker.genetic.jmetal.metaheuristics.MOCell_Settings;
import evochecker.genetic.jmetal.metaheuristics.NSGAII_Settings;
import evochecker.genetic.jmetal.metaheuristics.RandomSearch_Settings;
import evochecker.genetic.jmetal.metaheuristics.SPEA2_Settings;
import evochecker.genetic.jmetal.single.AlgorithmSteps;
import evochecker.genetic.jmetal.single.GeneticProblemSingle;
import evochecker.genetic.jmetal.single.RandomSearchSingle_Settings;
import evochecker.genetic.jmetal.single.SingleGA_Settings;
import evochecker.parser.ParserEngine;
import evochecker.parser.ParserEnginePrismPSY;
import evochecker.prism.Property;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;


/**
 * Main EvoChecker class
 * @author sgerasimou
 *
 */
public class EvoChecker {

	/** properties list*/
	private List<Property> propertyList;
	
	/** problem trying to solve*/
	private Problem problem;
	
	/** problem genes*/
	private List<AbstractGene> genes = new ArrayList<AbstractGene>();
	
	/** parser engine handler*/
	private ParserEngine parserEngine;
	
	/** model filename*/
	private String 		modelFilename;		
	
	/** property filename*/
	private String 		propertiesFilename;
	
	/** algorithm to be executed*/
	private Algorithm algorithm;
	
	
	
	/**
	 * Main 
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		try {			
			//instantiate evochecker
			EvoChecker evoChecker = new EvoChecker();
			//initialise problem
			evoChecker.initializeProblem();
			//initialise algorithm
			evoChecker.initialiseAlgorithm();
			//execute 
			evoChecker.execute();
			//close down
			evoChecker.closeDown();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		
		System.err.println("Time:\t" + (end - start)/1000);
	}

	
	/**
	 * Initialise the problem and the properties associated with the problem
	 * Note that in the next iteration of this code, 
	 * the initialisation should be done by reading the properties file
	 * @throws Exception
	 */
	private void initializeProblem() throws Exception {
		//1 Get model and properties filenames
		modelFilename 		= Utility.getProperty("MODEL_TEMPLATE_FILE");
		propertiesFilename	= Utility.getProperty("PROPERTIES_FILE");
		
		//2) parse model template
		parserEngine 		= new ParserEnginePrismPSY(modelFilename, propertiesFilename);
		
		//3) create chromosome
		genes				= GenotypeFactory.createChromosome(parserEngine.getEvolvableList());
		
		//4) create (gene,evolvable element) pairs
		parserEngine.createMapping();
		
		//5) create properties list
		propertyList = new ArrayList<Property>();

		//FX
//		propertyList.add(new Property(true));
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(false));
//		propertyList.add(new Property(true));
//		int numOfConstraints = 1;
		
		//Google
		propertyList.add(new Property(true));
		propertyList.add(new Property(false));
		propertyList.add(new Property(false));
		int numOfConstraints  = 0;

		//6) instantiate the problem
		problem = new GeneticProblemPSY(genes, propertyList, parserEngine, numOfConstraints, "GeneticProblem");
	}
	

	/**
	 * initialise algorithm
	 * @throws Exception
	 */
	private void initialiseAlgorithm() throws Exception{
		String algorithmStr = Utility.getProperty("ALGORITHM").toUpperCase();
		if (algorithmStr != null){
			if (algorithmStr.equals("NSGAII")){
				NSGAII_Settings nsgaiiSettings = new NSGAII_Settings(problem.getName(), problem);
				algorithm = nsgaiiSettings.configure();
			}
			else if (algorithmStr.equals("RANDOM")){
				RandomSearch_Settings rsSettings = new RandomSearch_Settings(problem.getName(), problem);
				algorithm = rsSettings.configure();
			}
			else if (algorithmStr.equals("SPEA2")){
				SPEA2_Settings spea2Settings = new SPEA2_Settings(problem.getName(), problem);
				algorithm = spea2Settings.configure();
			}
			else if (algorithmStr.equals("MOCELL")){
				MOCell_Settings mocellSettings = new MOCell_Settings(problem.getName(), problem);
				algorithm = mocellSettings.configure();
			}
			else if (algorithmStr.equals("SGA")){
				int numOfConstraints = 1;
				problem = new GeneticProblemSingle(genes, propertyList, parserEngine, numOfConstraints);
				SingleGA_Settings sga_settings = new SingleGA_Settings(problem.getName(), problem);
				algorithm = sga_settings.configure();
				((AlgorithmSteps)algorithm).initialise(); //only for single-objective algorithms
			}
			else if (algorithmStr.equals("RANDOM_SINGLE")){
				int numOfConstraints = 1;
				problem = new GeneticProblemSingle(genes, propertyList, parserEngine, numOfConstraints);
				RandomSearchSingle_Settings rss_settings = new RandomSearchSingle_Settings(problem.getName(), problem);
				algorithm = rss_settings.configure();
				((AlgorithmSteps)algorithm).initialise(); //only for single-objective algorithms
			}
			else 
				throw new Exception("Algorithm not recognised");
		}
	}
	
	
	/**
	 * Make finalisations of algorithm
	 */
	private void closeDown(){
		//only for single-objective algorithms
		if (algorithm instanceof AlgorithmSteps)
			((AlgorithmSteps)algorithm).finalise(); 
	}
	
	
	/**
	 * Execute
	 * @throws Exception
	 */
	private void execute() throws Exception{
		
		// Execute the Algorithm
		SolutionSet population = algorithm.execute();

		
		//Print results to console
		System.out.println("-------------------------------------------------");
		System.out.println("SOLUTION: \t" + population.size());
		for (int i=0; i<population.size(); i++){
			Solution solution = population.get(i);
			for (int objective=0; objective<solution.getNumberOfObjectives(); objective++){
				System.out.printf("%.3f\t", solution.getObjective(objective));
			}
			double constraintValue = solution.getOverallConstraintViolation();
			if (constraintValue<0){
				System.out.println(constraintValue +"\t"+ Arrays.toString(solution.getDecisionVariables()));
			}
		}
		
		//Store results
		String algorithmStr = Utility.getProperty("ALGORITHM").toUpperCase();
		String seeding = Utility.getProperty("SEEDING").toUpperCase();
		Utility.exportToFile("data/FUN_"+algorithmStr +"_"+ seeding, population.get(0).toString(), true);
		Utility.printVariablesToFile("data/VAR_"+algorithmStr +"_"+ seeding, population.get(0), true);
//		population.printObjectivesToFile("data/FUN_"+algorithmStr +"_"+ seeding);
//		population.printVariablesToFile("data/VAR_"+algorithmStr  +"_"+ seeding);
	}	
}
