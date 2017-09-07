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

import java.io.File;
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
import jmetal.util.JMException;


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
			
			//initialise data structures and variables for saving data
			rodes.initialiseOutputData();
			
			//execute and save results
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
	 * Initialise data structure and variables for saving execution results
	 */
	private void initialiseOutputData() {
		//create output dir
		String outputDir = "data" + File.separator 
							+ Utility.getProperty(Constants.PROBLEM_KEYWORD)   + File.separator 
							+ Utility.getProperty(Constants.ALGORITHM_KEYWORD) + File.separator;
		Utility.createDir(outputDir);
		knowledge.put(Constants.OUTPUT_DIR_KEYWORD, outputDir);
		
		//Generate output files suffix
		String tolerance 	= Utility.getProperty(Constants.TOLERANCE_KEYWORD).replace(".", "");
		String epsilon		= Utility.getProperty(Constants.EPSILON_KEYWORD).replace(".", "");
		int run				= RODESExperimentRuns.getRun();
		String outputFileSuffix = tolerance +"_"+ epsilon +"_"+ run;
		knowledge.put(Constants.OUTPUT_FILE_SUFFIX, outputFileSuffix);
		
		knowledge.put(Constants.PROPERTIES_KEYWORD, propertyList);
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

		//Execute the Algorithm
		SolutionSet population = algorithm.execute();

		//Save results
		exportResults(population);
		
		knowledge.addMessage("Finished execution");
	}
	
	
	
	private void exportResults(SolutionSet population) throws JMException {
		//export final population
		//Print results to console
		System.out.println("-------------------------------------------------");
		System.out.println("SOLUTIONS: \t" + population.size());
		knowledge.addMessage("Saving solutions");
		
		List<Double> regionsRadii = new ArrayList<Double>();
		for (AbstractGene gene : genes){
			if (gene instanceof RegionGene)
				regionsRadii.add(((RegionGene)gene).getRegionRadius());
		}
		
		//create output dir
		String outputDir 		= knowledge.get(Constants.OUTPUT_DIR_KEYWORD).toString();
		String outputFileSuffix = knowledge.get(Constants.OUTPUT_FILE_SUFFIX).toString();
		
		//Store midpoint results
		population.printObjectivesToFile(outputDir + "FUN_" + outputFileSuffix);
		population.printVariablesToFile( outputDir + "VAR_" +  outputFileSuffix);
		
		//Store region results
		Utility.printObjectiveRegionsToFile(outputDir + "FUN_REGION_" +  outputFileSuffix, population, false, propertyList);
		Utility.printVariableRegionsToFile( outputDir + "VAR_REGION_" +  outputFileSuffix, population, false, regionsRadii);
		Utility.printVariableRegionsToFile2(outputDir + "VAR_REGION2_" + outputFileSuffix, population, true);
		
//		//store generation results
//		List<SolutionSet> generationsRepo = knowledge.getGenerationsRepository();
//		int gen = 1;
//		for (SolutionSet generation : generationsRepo) {
//			String outputFile = outputFileSuffix +"_"+ gen++;
//			Utility.printVariableRegionsToFile( outputDir + File.separatorChar + "VAR_REGION_" +  outputFile, generation, false, regionsRadii);
//			Utility.printObjectiveRegionsToFile(outputDir + File.separatorChar + "FUN_REGION_" +  outputFile, generation, false, propertyList);
//			Utility.printVariableRegionsToFile2(outputDir + File.separatorChar + "VAR_REGION2_" + outputFile, generation, true);
//		}
//		
	}


	@Override
	public void run() {
		main(null);
	}	
}
