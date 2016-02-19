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
import java.util.Arrays;
import java.util.List;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;
import evochecker.auxiliary.Utility;
//import org.apache.commons.lang.NotImplementedException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;
import evochecker.genetic.jmetal.encoding.ArrayInt;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.genetic.jmetal.encoding.ArrayRealIntSolutionType;
import evochecker.parser.InstantiatorInterface;
import evochecker.prism.Property;

/**
 * Class representing a genetic problem to be solved through 
 * using search-based techniques
 * @author sgerasimou
 *
 */
public abstract class GeneticModelProblem extends Problem {

	private static final long serialVersionUID = -2679872853510614319L;

	/** List of genes*/
	protected List<AbstractGene> genes;
	
	/** List of properties*/
	protected List<Property> properties;

	/** Reference to the instantiator handler*/
	protected InstantiatorInterface instantiator;
	
	/** Number of integer variables*/
	private int intVariables;
	
	/** Number of real variables*/
	private int realVariables;

	
	/**
	 * Class constructor: create a new Genetic Problem instance
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticModelProblem(List<AbstractGene> genes, List<Property> properties,
						  InstantiatorInterface instantiator, int numOfConstraints, String problemName) {
		this.genes 					= genes;
		this.instantiator 			= instantiator;
		this.numberOfConstraints_ 	= numOfConstraints;
		this.numberOfObjectives_ 	= properties.size()-numberOfConstraints_;
		this.properties 			= properties;
		this.problemName_			= problemName;
		this.initializeLimits();
	}
	
	
	/**
	 * Intialise limits of variables
	 */
	private void initializeLimits() {
		//1) Calculate how many variables exist in the probabilistic model template
		computeNumberOfVariables();
		// System.out.println("Found variables: " + this.numberOfVariables_);
		//2) Initialise arrays to hold their bounds
		upperLimit_ 	= new double[numberOfVariables_];
		lowerLimit_ 	= new double[numberOfVariables_];
		//3) Calculate the number of real variables
		realVariables 	= this.computeRealVariables(0);
		//4) Calculate the number of integer variables
		intVariables 	= this.computeIntVariables(realVariables);
		//5) Initialise the solution type
		solutionType_ 	= new ArrayRealIntSolutionType(this, realVariables, intVariables, this.lowerLimit_, this.upperLimit_);
	}

	
	/** 
	 * Calculate the number of variables
	 */
	private void computeNumberOfVariables() {
		this.numberOfVariables_ = 0;
		for (AbstractGene g : genes) {
			// Discrete distribution generates a number of genes 
			// equal to the number of their outcomes
			if (g instanceof DiscreteDistributionGene) {
				int outcomes = ((DiscreteDistributionGene) g).getNumberOfOutcomes();
				this.numberOfVariables_ += outcomes;
			} 
			else {
				this.numberOfVariables_++;
			}
		}
	}
	
	
	/**
	 * Calculate the number of real variables (i.e., Double + Distribution) 
	 * @param baseIndex
	 * @return
	 */
	private int computeRealVariables(int baseIndex) {
		int realVariables = baseIndex;
		for (AbstractGene g : genes) {
			if (g instanceof DiscreteDistributionGene) {
				int outcomes = ((DiscreteDistributionGene) g).getNumberOfOutcomes();
				int total = realVariables + outcomes;
				for (int j = realVariables; j < total; j++) {
					lowerLimit_[j] = g.getMinValue().doubleValue();
					upperLimit_[j] = g.getMaxValue().doubleValue();
					realVariables++;
				}
			}

			if (g instanceof DoubleGene) {
				lowerLimit_[realVariables] = g.getMinValue().doubleValue();
				upperLimit_[realVariables] = g.getMaxValue().doubleValue();
				realVariables++;
			}
		}
		return realVariables - baseIndex;
	}

	
	/**
	 * Calculate the number of integer variables (i.e., Integer + Module)
	 * @param baseIndex
	 * @return
	 */
	private int computeIntVariables(int baseIndex) {
		int intVariables = baseIndex;
		for (AbstractGene g : genes) {
			if (g instanceof IntegerGene || g instanceof AlternativeModuleGene) {
				lowerLimit_[intVariables] = g.getMinValue().doubleValue();
				upperLimit_[intVariables] = g.getMaxValue().doubleValue();
				// System.out.println("MIN VALUE: "+
				// g.getMinValue().doubleValue());
				// System.out.println("MAX VALUE: "+
				// g.getMaxValue().doubleValue());
				intVariables++;
			}
		}
		return intVariables - baseIndex;
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
				double cumulative = 0;
				double[] outcomesValues = new double[outcomes];
				int index = 0;

				for (int j = currentIndex; j < currentIndex + outcomes; j++) {
					cumulative += realPart.getValue(j);
					outcomesValues[index] = realPart.getValue(j);
					index++;
				}
				currentIndex = currentIndex + outcomes;
				
				//TODO real values normalised here: is this good for a CTMC?
				for (int j = 0; j < outcomes; j++) {
					outcomesValues[j] = outcomesValues[j] / cumulative;
				}
				g.setAllele(outcomesValues);
			}

			if (g instanceof DoubleGene) {
				double value = realPart.getValue(currentIndex);
				currentIndex++;
				g.setAllele(value);
			}
		}
	}


	/**
	 * Populate the integer values for the gene as specified by the solution parameter
	 * @param solution
	 * @throws JMException
	 */
	protected void populateGenesWithIntSolution(Solution solution) throws JMException {
		int currentIndex = 0;
		for (int i = 0; i < genes.size(); i++) {
			AbstractGene g = genes.get(i);

			if (g instanceof IntegerGene
					|| g instanceof AlternativeModuleGene) {
				ArrayInt intPart = (ArrayInt) solution.getDecisionVariables()[1];
				g.setAllele(intPart.getValue(currentIndex));
				currentIndex++;
			}

		}
	}


	/**
	 * Evaluate function from JMetal
	 * throws exception because we use the parallel evaluation
	 */
	@Override
	public void evaluate(Solution arg0) throws JMException {
		try {
			System.err.println("evaluate");
			throw new IllegalAccessException("Evaluate() function is not used; invoke parallelEvaluate() instead"); 
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}		
	}

	  
	/** 
	 * Get number of integer variables
	 * @return
	 */
	public int getNumOfIntVariables(){
		return (this.intVariables);
	}


	/**
	 * Get number of real variables
	 * @return
	 */
	 public int getNumOfRealVariables(){
		 return (this.realVariables);
	 }

	 
	/** 
	 * Evaluate 
	 * @param solution
	 * @param out
	 * @param in
	 * @throws JMException
	 */
	public abstract void parallelEvaluate(BufferedReader in, PrintWriter out, Solution solution) throws JMException;

	 
	/**
	 * Prism invocation method
	 * @param model
	 * @param propertyFile
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 */
	 protected abstract List<String> invokePrism(BufferedReader in, PrintWriter out, String output) throws IOException;


	 /** 
	  * Evaluates the constraint overhead of a solution 
	  * @param solution The solution
	 * @throws JMException 
	  */  
	public abstract void evaluateConstraints(Solution solution, List<String> fitnessList) throws JMException; 	  
	
	
	
//	{
//		//Populate genes
//		this.populateGenesWithRealSolution(solution);
//		this.populateGenesWithIntSolution(solution);
//
//		// Invoke prism....
//		String model = instantiator.getValidModelInstance(this.genes);
//		String propertyFile = instantiator.getPrismPropertyFileName();
////		Utility.exportToFile("model.txt", model);
//
//		
//		List<String> resultsList;
//		try {
//			resultsList = this.invokePrism(in, out, model, propertyFile);
//
//			for (int i = 0; i < this.numberOfObjectives_; i++) {
//				Property p = this.properties.get(i);
//				double result;
//				if (p.isMaximization()) {
//					result = new BigDecimal(- Double.parseDouble(resultsList.get(i))).setScale(3, RoundingMode.HALF_DOWN).doubleValue();
//				}
//				else{
//					result = new BigDecimal(Double.parseDouble(resultsList.get(i))).setScale(3, RoundingMode.HALF_UP).doubleValue();
//				}
//				solution.setObjective(i, result);
//				System.out.print("FITNESS: "+ result +"\t");
////				} 
////				else {
////					solution.setObjective(i, result);
////					System.out.print("FITNESS: " + result +"\t");
////				}
//			}
//			
//			if (numberOfConstraints_>0){
//				this.evaluateConstraints(solution, resultsList);
//			}
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println();
//	}


////		System.out.println("Sending to PRISM: "+propertyFile);
////		System.out.println("Sending to PRISM: "+model);
//		out.print(model + "@" + propertyFile + "\nEND\n");
//		out.flush();
//
//		String line;
//		StringBuilder modelBuilder = new StringBuilder();
//		do {
////			System.out.println("Waiting PRISM");
//			line = in.readLine();
//			if (line.endsWith("END"))
//				break;
//			modelBuilder.append(line);
//			modelBuilder.append("\n");
//		} while (true);
//
//		String res[] = modelBuilder.toString().trim().split("@");
////		System.out.println("Received from PRISM: "+ modelBuilder.toString());
//		return Arrays.asList(res);
//	}

}
