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

package rodes;

import java.util.ArrayList;
import java.util.List;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.KnowledgeSingleton;
import evochecker.auxiliary.Utility;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.RegionGene;
import evochecker.genetic.jmetal.GeneticProblemPSY;
import evochecker.genetic.jmetal.metaheuristics.NSGAIIRegion_Settings;
import evochecker.genetic.jmetal.metaheuristics.RandomSearchRegion_Settings;
import evochecker.parser.ParserEngine;
import evochecker.parser.ParserEnginePrismPSY;
import evochecker.prism.Property;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;


/**
 * Main RODES class
 * @author sgerasimou
 *
 */
public class RODES implements Runnable{

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
	
	/** Knowledge singleton*/
	private KnowledgeSingleton knowledge = KnowledgeSingleton.getInstance();

	
	/**
	 * Main 
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		try {			
			//instantiate evochecker
			RODES rodes = new RODES();
			//initialise problem
			rodes.initializeProblem();
			//initialise algorithm
			rodes.initialiseAlgorithm();
			//execute 
			rodes.execute();
			//close down
			rodes.closeDown();
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
		knowledge.addMessage("Initializing problem");
		
		//1 Get model and properties filenames
		modelFilename 		= Utility.getProperty(Constants.MODEL_FILE_KEYWORD);
		propertiesFilename	= Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD);
		
		//2) parse model template
		parserEngine 		= new ParserEnginePrismPSY(modelFilename, propertiesFilename);
		
		//3) create chromosome
		genes				= GenotypeFactory.createChromosome(parserEngine.getEvolvableList());
		
		//4) create (gene,evolvable element) pairs
		parserEngine.createMapping();
		
		//5) create properties list
		propertyList = new ArrayList<Property>();
		
		//Google
//		propertyList.add(new Property(true));
//		propertyList.add(new Property(true));
//		propertyList.add(new Property(false));
//		int numOfConstraints  = 1;

//		Cluster
//		propertyList.add(new Property(true));
//		propertyList.add(new Property(true));
//		propertyList.add(new Property(false));
//		int numOfConstraints  = 1;

//		Buffer
		propertyList.add(new Property(true));
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
		knowledge.addMessage("Initializing algorithm");

		String algorithmStr = Utility.getProperty(Constants.ALGORITHM_KEYWORD).toUpperCase();
		if (algorithmStr != null){
			if (algorithmStr.equals(Constants.ALGORITHM.NSGAII.toString())){
				NSGAIIRegion_Settings nsgaiiSettings = new NSGAIIRegion_Settings(problem.getName(), problem);
				algorithm = nsgaiiSettings.configure();
 			}
			else if (algorithmStr.equals(Constants.ALGORITHM.RANDOM.toString())){
				RandomSearchRegion_Settings rsSettings = new RandomSearchRegion_Settings(problem.getName(), problem);
				algorithm = rsSettings.configure();
			}
			else 
				throw new Exception("Algorithm not recognised");
		}
	}
	
	
	/**
	 * Make finalisations of algorithm
	 */
	private void closeDown(){
		knowledge.addMessage("Closing down");
		knowledge.put(Constants.DONE_KEYWORD, true);
	}
	
	
	/**
	 * Execute
	 * @throws Exception
	 */
	private void execute() throws Exception{
		knowledge.addMessage("Starting execution");

		// Execute the Algorithm
		SolutionSet population = algorithm.execute();

		knowledge.addMessage("Finished execution");
		
		//Print results to console
		System.out.println("-------------------------------------------------");
		System.out.println("SOLUTIONS: \t" + population.size());
		knowledge.addMessage("Saving solutions");
		
		List<Double> regionsRadii = new ArrayList<Double>();
		for (AbstractGene gene : genes){
			if (gene instanceof RegionGene)
				regionsRadii.add(((RegionGene)gene).getRegionRadius());
		}
		String tolerance 	= Utility.getProperty(Constants.TOLERANCE_KEYWORD).replace(".", "");
		String epsilon		= Utility.getProperty(Constants.EPSILON_KEYWORD).replace(".", "");
		int run				= RODESExperimentRuns.getRun();
		
		String directory = "data/" + Utility.getProperty(Constants.PROBLEM_KEYWORD); 
		Utility.createDir(directory);
		String outputFileEnd = tolerance +"_"+ epsilon +"_"+ run;
		//Store results
		population.printObjectivesToFile(directory + "/FUN_" + outputFileEnd);
		population.printVariablesToFile( directory + "/VAR_" +  outputFileEnd);
				
		Utility.printVariableRegionsToFile( directory + "/VAR_REGION_" +  outputFileEnd, population, false, regionsRadii);
		Utility.printObjectiveRegionsToFile(directory + "/FUN_REGION_" + outputFileEnd, population, false, propertyList);
		
	}


	@Override
	public void run() {
		main(null);
	}	
}
