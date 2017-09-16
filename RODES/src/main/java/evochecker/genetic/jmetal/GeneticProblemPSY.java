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

package evochecker.genetic.jmetal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import evochecker.auxiliary.ParserGSON;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.RegionGene;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.genetic.jmetal.encoding.solution.RegionSolution;
import evochecker.parser.InstantiatorInterface;
import evochecker.parser.InstantiatorInterfacePrismPSY;
import evochecker.parser.ParserEngine;
import evochecker.parser.ParserEnginePrismPSY;
import evochecker.prism.Property;
import jmetal.core.Solution;
import jmetal.util.JMException;

/**
 * Class representing a genetic problem to be solved through 
 * using search-based techniques
 * @author sgerasimou
 *
 */
public class GeneticProblemPSY extends GeneticModelProblem{
	
	/**
	 * Class constructor: create a new Genetic Problem instance
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblemPSY(List<AbstractGene> genes, InstantiatorInterface instantiator, 
							List<Property> objectivesList, List<Property> constraintsList, String problemName){
		super(genes, instantiator, objectivesList, constraintsList, problemName);
		try {
			if (!(instantiator instanceof InstantiatorInterfacePrismPSY)){
					throw new EvoCheckerException("InstantiatorInterfacePrismPSY not provided");
			}	
			this.instantiator = (InstantiatorInterfacePrismPSY) instantiator;
		} 
		catch (EvoCheckerException e) {
			e.printStackTrace();
			System.exit(-1);
		}	
	}

	
	/**
	 * Populate the values for the gene as specified by the solution parameter
	 * @param solution
	 * @throws JMException
	 * @throws EvoCheckerException 
	 */
	protected void populateGenesWithRealSolution(Solution solution) throws JMException, EvoCheckerException {
		ArrayReal realPart = (ArrayReal) solution.getDecisionVariables()[0];
		int currentIndex = 0;

		for (int i = 0; i < genes.size(); i++) {
			AbstractGene g = genes.get(i);
			if (g instanceof DiscreteDistributionGene) {
				int outcomes = ((DiscreteDistributionGene) g).getNumberOfOutcomes();
				double[] outcomesValues = new double[outcomes];
				int index = 0;

				for (int j = currentIndex; j < currentIndex + outcomes; j++) {
					outcomesValues[index] = realPart.getValue(j);
					index++;
				}
				currentIndex = currentIndex + outcomes;				
				g.setAllele(outcomesValues);
			}

			if (g instanceof RegionGene){
				double value = realPart.getValue(currentIndex);
				currentIndex++;
				g.setAllele(value);
			}
		}
	}
	
	
	/** 
	 * Evaluate 
	 * @param solution
	 * @param out
	 * @param in
	 * @throws JMException
	 * @throws EvoCheckerException 
	 */
	public void parallelEvaluate(BufferedReader in, PrintWriter out, Solution solution) throws JMException, EvoCheckerException {
		//Populate genes
		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);

		//Get params
		String model 				= instantiator.getValidModelInstance(this.genes);
		String propertyFile 			= instantiator.getPrismPropertyFileName(); 
		String paramsWithRanges		= ((InstantiatorInterfacePrismPSY) instantiator).getParamsWithRanges();
		String accuracy				= ((InstantiatorInterfacePrismPSY) instantiator).getAccuracy();
		String decompositionType	=((InstantiatorInterfacePrismPSY) instantiator).getDecompositionType();
//		Utility.exportToFile("model.txt", model);

		
		try {
			String outputStr 			= model +"\n@"+ propertyFile +"\n@"+ decompositionType +"\n@"+ 
										  paramsWithRanges +"\n@"+ accuracy + "\nEND"; 
			List<String> resultsList 	= invokePrism(in, out, outputStr);

			for (int i = 0; i < this.numberOfObjectives_; i++) {
				Property p = this.objectivesList.get(i);
				double min, max;
				if (p.isMaximization()) {
					max = new BigDecimal(- Double.parseDouble(resultsList.get(i*2))).setScale(4, RoundingMode.HALF_DOWN).doubleValue();
					min = new BigDecimal(- Double.parseDouble(resultsList.get(i*2+1))).setScale(4, RoundingMode.HALF_DOWN).doubleValue();
				}
				else{
					min = new BigDecimal(  Double.parseDouble(resultsList.get(i*2))).setScale(4, RoundingMode.HALF_DOWN).doubleValue();
					max = new BigDecimal(  Double.parseDouble(resultsList.get(i*2+1))).setScale(4, RoundingMode.HALF_DOWN).doubleValue();
				}
				((RegionSolution)solution).setObjectiveBounds(i, min, max);
//				solution.setObjective(i, result);
//				System.out.print("FITNESS: ["+ min +","+ max +"]");
			}

			this.evaluateConstraints(solution, resultsList);
			
		} catch (IOException | EvoCheckerException | NullPointerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
//		System.out.println();
	}


	/**
	 * Prism invocation method
	 * @param model
	 * @param propertyFile
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws EvoCheckerException 
	 */
	@Override
	protected List<String> invokePrism(BufferedReader in, PrintWriter out, String outputStr) throws IOException, EvoCheckerException {
		//send to server
		out.println(outputStr);
		out.flush();
		//read from server
		String response = in.readLine();
//		System.out.println(response);
		if (response==null || response.isEmpty())
			throw new EvoCheckerException("Error: invalid response from server:\t" + response);
		List<String> resultList = ParserGSON.parseGSON(response);
		return resultList;
	}
	
	
	 /** 
	  * Evaluates the constraint overhead of a solution 
	  * @param solution The solution
	 * @throws JMException 
	  */  
//	public void evaluateConstraints(Solution solution, List<String> resultsList) throws JMException {
//		//Google
////		double value = new BigDecimal(Double.parseDouble(resultsList.get(numberOfObjectives_*2+1)))
////										.setScale(4, RoundingMode.HALF_DOWN).doubleValue();
////		if (value > 5){
////			solution.setOverallConstraintViolation((5-value )*100);
////			solution.setNumberOfViolatedConstraint(1);			
////		}
////		else{			
////			solution.setOverallConstraintViolation(0);
////			solution.setNumberOfViolatedConstraint(0);
////		}
//		
//		//Cluster
////		double value = new BigDecimal(Double.parseDouble(resultsList.get(numberOfObjectives_*2+1)))
////										.setScale(4, RoundingMode.HALF_DOWN).doubleValue();
////		if (value > 10){
////			solution.setOverallConstraintViolation((10-value )*100);
////			solution.setNumberOfViolatedConstraint(1);			
////		}
////		else{			
////			solution.setOverallConstraintViolation(0);
////			solution.setNumberOfViolatedConstraint(0);
////		}
//	}
	
	
	public GeneticProblemPSY (GeneticProblemPSY aProblem) throws EvoCheckerException{
		
		super((GeneticModelProblem)aProblem);
		
		this.instantiator 			= new ParserEnginePrismPSY((ParserEnginePrismPSY)aProblem.instantiator);
		this.genes 					= ((ParserEngine)instantiator).getGeneList(); 
	}
}
