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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
//import org.apache.commons.lang.NotImplementedException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;
import evochecker.genetic.genes.RegionGene;
import evochecker.genetic.jmetal.encoding.ArrayInt;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.genetic.jmetal.encoding.ArrayRealIntSolutionType;
import evochecker.parser.InstantiatorInterface;
import evochecker.parser.ParserEngine;
import evochecker.prism.Property;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;

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
			else if (g instanceof RegionGene){
				this.numberOfVariables_ += 2;
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

			if (g instanceof RegionGene) {
				lowerLimit_[realVariables] = g.getMinValue().doubleValue();
				upperLimit_[realVariables] = g.getMaxValue().doubleValue();				
				realVariables++;
				lowerLimit_[realVariables] = ((RegionGene)g).getRegionMin();
				upperLimit_[realVariables] = ((RegionGene)g).getRegionMax();								
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
	 * @throws EvoCheckerException 
	 */
	protected void populateGenesWithRealSolution(Solution solution) throws JMException, EvoCheckerException {
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
	 * @throws EvoCheckerException 
	 */
	public abstract void parallelEvaluate(BufferedReader in, PrintWriter out, Solution solution) throws JMException, EvoCheckerException;

	 
	/**
	 * Prism invocation method
	 * @param model
	 * @param propertyFile
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 */
	 protected abstract List<String> invokePrism(BufferedReader in, PrintWriter out, String output) throws IOException, EvoCheckerException;


	 /** 
	  * Evaluates the constraint overhead of a solution 
	  * @param solution The solution
	 * @throws JMException 
	  */  
	public abstract void evaluateConstraints(Solution solution, List<String> fitnessList) throws JMException; 	  

	
	public GeneticModelProblem(GeneticModelProblem aProblem) throws EvoCheckerException{
		
		if (aProblem.instantiator instanceof ParserEngine)
			this.instantiator 			= new ParserEngine((ParserEngine)aProblem.instantiator);
		else
			throw new EvoCheckerException("Invalid Instantiator inteface!");

		this.genes 					= ((ParserEngine)instantiator).getGeneList(); 
										
		
		this.numberOfConstraints_ 	= aProblem.numberOfConstraints_;
		this.numberOfObjectives_ 	= aProblem.numberOfObjectives_; 
		
		this.properties 			= new ArrayList<Property>();
		for (Property prop : aProblem.properties){
			this.properties.add(new Property(prop.isMaximization()));
		}
		
		this.problemName_			= aProblem.problemName_;
		this.initializeLimits();

	}
}
