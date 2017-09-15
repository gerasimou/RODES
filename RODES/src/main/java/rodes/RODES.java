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
import java.util.Map;
import java.util.Properties;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.KnowledgeSingleton;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.RegionGene;
import evochecker.genetic.jmetal.GeneticProblemPSY;
import evochecker.genetic.jmetal.metaheuristics.NSGAIIRegion_Settings;
import evochecker.genetic.jmetal.metaheuristics.RandomSearchRegion_Settings;
import evochecker.parser.ParserEngine;
import evochecker.parser.ParserEnginePrismPSY;
import evochecker.prism.Constraint;
import evochecker.prism.Objective;
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
//	private List<Property> propertyList;
	private List<Property> objectivesList;
	private List<Property> constraintsList;
	
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
			//check configuration script
			checkConfiguration();

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
			
			long end = System.currentTimeMillis();

			rodes.exportTime( (end - start)/1000.0);
			System.err.println("Time:\t" + (end - start)/1000.0);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
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
		//propertyList = new ArrayList<Property>();
		objectivesList 	= new ArrayList<Property>();
		constraintsList	= new ArrayList<Property>();
		
		//Google
//		propertyList.add(new Property(true));
//		propertyList.add(new Property(true));
//		propertyList.add(new Property(false));
//		int numOfConstraints  = 1;

//		Cluster
		//propertyList.add(new Property(true));
		//propertyList.add(new Property(true));
		//propertyList.add(new Property(false));
		//int numOfConstraints  = 1;
		objectivesList.add(new Objective(true));
		objectivesList.add(new Objective(true));
		constraintsList.add(new Constraint(false, 10));

//		Buffer
//		propertyList.add(new Property(true));
//		propertyList.add(new Property(false));
//		int numOfConstraints  = 0;

		
		//6) instantiate the problem
		//problem = new GeneticProblemPSY(genes, propertyList, parserEngine, numOfConstraints, "GeneticProblem");
		problem = new GeneticProblemPSY(genes, parserEngine, objectivesList, constraintsList, "GeneticProblem");
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
		
		knowledge.put(Constants.OBJECTIVES_KEYWORD, objectivesList);
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
		
		//get output dir
		String outputDir 		= knowledge.get(Constants.OUTPUT_DIR_KEYWORD).toString();
		String outputFileSuffix = knowledge.get(Constants.OUTPUT_FILE_SUFFIX).toString();
		
		//Strore configuration
		Utility.exportToFile(outputDir +"config_"+ outputFileSuffix, getConfiguration(), false);
		
		//Store midpoint results
		population.printObjectivesToFile(outputDir + "FUN_" + outputFileSuffix);
		population.printVariablesToFile( outputDir + "VAR_" +  outputFileSuffix);
		
		//Store region results
		Utility.printObjectiveRegionsToFile(outputDir + "FUN_REGION_" +  outputFileSuffix, population, false, objectivesList);
		Utility.printVariableRegionsToFile(outputDir + "VAR_REGION2_" + outputFileSuffix, population, true);
		Utility.printVariableRegionsToFileOld( outputDir + "VAR_REGION_" +  outputFileSuffix, population, false, regionsRadii);		
	}
	
	
	private void exportTime(double time) {
		//get output dir
		String outputDir	= knowledge.get(Constants.OUTPUT_DIR_KEYWORD).toString();
		
		String outputStr	= knowledge.get(Constants.OUTPUT_FILE_SUFFIX).toString() +":\t"+ time; 
		
		//Strore configuration
		Utility.exportToFile(outputDir +"times", outputStr, true);

	}
	
	
	/**
	 * Check whether the experiment has been configured correctly 
	 * @throws EvoCheckerException 
	 */
	private static void checkConfiguration() throws EvoCheckerException {
		StringBuilder errors = new StringBuilder();
		final String NAN = "NAN";
		
		//check algorithm
		if (Utility.getProperty(Constants.ALGORITHM_KEYWORD, NAN).equals(NAN)) 
			errors.append(Constants.ALGORITHM_KEYWORD + "not found in configuration script!\n");
		else {
			try {
				Constants.ALGORITHM.valueOf(Utility.getProperty(Constants.ALGORITHM_KEYWORD, NAN));
			} catch (IllegalArgumentException e) {
				errors.append(e.getMessage());
			}
		}
		
		//check population size
		if (Utility.getProperty(Constants.POPULATION_SIZE_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.POPULATION_SIZE_KEYWORD + " not found in configuration script!\n");

		//check evaluations
		if (Utility.getProperty(Constants.MAX_EVALUATIONS_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.MAX_EVALUATIONS_KEYWORD + " not found in configuration script!\n");

		//check processors
		if (Utility.getProperty(Constants.PROCESSORS_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.PROCESSORS_KEYWORD + " not found in configuration script!\n");

		//check model file
		if (Utility.getProperty(Constants.MODEL_FILE_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.MODEL_FILE_KEYWORD + " not found in configuration script!\n");

		//check properties file
		if (Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.PROPERTIES_FILE_KEYWORD + " not found in configuration script!\n");

		//check tolerance
		if (Utility.getProperty(Constants.TOLERANCE_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.TOLERANCE_KEYWORD + " not found in configuration script!\n");

		//check epsilon
		if (Utility.getProperty(Constants.EPSILON_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.EPSILON_KEYWORD + " not found in configuration script!\n");

		//check problem name
		if (Utility.getProperty(Constants.PROBLEM_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.PROBLEM_KEYWORD + " not found in configuration script!\n");

		//check port
		if (Utility.getProperty(Constants.INITIAL_PORT_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.INITIAL_PORT_KEYWORD + " not found in configuration script!\n");

		//check jvm
		if (Utility.getProperty(Constants.JVM_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.JVM_KEYWORD + " not found in configuration script!\n");

		//check dominance relation
		if (Utility.getProperty(Constants.DOMINANCE_KEYWORD, NAN).equals(NAN))
			errors.append(Constants.DOMINANCE_KEYWORD + " not found in configuration script!\n");
		else {
			try {
				Constants.DOMINANCE.valueOf(Utility.getProperty(Constants.DOMINANCE_KEYWORD).trim());
			} catch (IllegalArgumentException e) {
				errors.append(e.getMessage());
			}
		}

		if (errors.length()!=0)
			throw new EvoCheckerException(errors.toString());
		else
			System.out.println(getConfiguration());
	}
	
	
	private static String getConfiguration() {
		StringBuilder str = new StringBuilder();
		
		str.append("Configuration script\n");
		str.append("==========================================\n");
		
		Properties props = Utility.getAllProperties();
		for (Map.Entry<Object, Object> entry : props.entrySet()) {
			str.append(entry.getKey() +" = "+ entry.getValue() +"\n");
		}
		str.append("==========================================\n\n");
		
		return str.toString();
	}


	@Override
	public void run() {
		main(null);
	}	
}
