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
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.RegionGene;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.parser.InstantiatorInterface;
import evochecker.parser.InstantiatorInterfacePrismPSY;
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
	public GeneticProblemPSY(List<AbstractGene> genes, List<Property> properties,
						  InstantiatorInterface instantiator, int numOfConstraints, String problemName){
		super(genes, properties, instantiator, numOfConstraints, problemName);
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
	 */
	protected void populateGenesWithRealSolution(Solution solution) throws JMException {
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
	 */
	public void parallelEvaluate(BufferedReader in, PrintWriter out, Solution solution) throws JMException {
		//Populate genes
		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);

		//Get params
		String model 				= instantiator.getValidModelInstance(this.genes);
		String propertyFile 		= instantiator.getPrismPropertyFileName();
		String paramsWithRanges		= ((InstantiatorInterfacePrismPSY) instantiator).getParamsWithRanges();
		String accuracy				= ((InstantiatorInterfacePrismPSY) instantiator).getAccuracy();
		String decompositionType	=((InstantiatorInterfacePrismPSY) instantiator).getDecompositionType();
//		Utility.exportToFile("model.txt", model);

		
		try {
			String outputStr 			= model +"\n@"+ propertyFile +"\n@"+ decompositionType +"\n@"+ 
										  paramsWithRanges +"\n@"+ accuracy + "\nEND"; 
			List<String> resultsList 	= invokePrism(in, out, outputStr);

			for (int i = 0; i < this.numberOfObjectives_; i++) {
				Property p = this.properties.get(i);
				double result;
				if (p.isMaximization()) {
					result = new BigDecimal(- Double.parseDouble(resultsList.get(i))).setScale(3, RoundingMode.HALF_DOWN).doubleValue();
				}
				else{
					result = new BigDecimal(Double.parseDouble(resultsList.get(i))).setScale(3, RoundingMode.HALF_UP).doubleValue();
				}
				solution.setObjective(i, result);
				System.out.print("FITNESS: "+ result +"\t");
			}
			
			if (numberOfConstraints_>0){
				this.evaluateConstraints(solution, resultsList);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}


	/**
	 * Prism invocation method
	 * @param model
	 * @param propertyFile
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 */
	@Override
	protected List<String> invokePrism(BufferedReader in, PrintWriter out, String outputStr) throws IOException {
		//send to server
		out.println(outputStr);
		out.flush();
		//read from server
		String response = in.readLine();
		System.out.println(response);
		List<String> resultList = ParserGSON.parseGSON(response);
		return resultList;
	}
	
	
	 /** 
	  * Evaluates the constraint overhead of a solution 
	  * @param solution The solution
	 * @throws JMException 
	  */  
	public void evaluateConstraints(Solution solution, List<String> fitnessList) throws JMException {
		solution.setOverallConstraintViolation(0);
		solution.setNumberOfViolatedConstraint(0);
	}
}
