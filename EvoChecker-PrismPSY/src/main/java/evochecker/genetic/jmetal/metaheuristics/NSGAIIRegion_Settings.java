//  NSGAII_Settings.java 
//
//  Authors:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
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

package evochecker.genetic.jmetal.metaheuristics;

import java.util.HashMap;
import java.util.Properties;

import evochecker.auxiliary.Utility;
import evochecker.genetic.jmetal.GeneticModelProblem;
import evochecker.genetic.jmetal.MultiProcessEvaluator;
import evochecker.genetic.jmetal.operators.CrossoverFactory;
import evochecker.genetic.jmetal.operators.MutationFactory;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.experiments.Settings;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;

/**
 * Settings class of algorithm NSGA-II (real encoding)
 */
public class NSGAIIRegion_Settings extends Settings {
	public int populationSize_;
	public int maxEvaluations_;
	public double realCrossoverProbability_;
	public double intCrossoverProbability_;
	public double realMutationProbability_;
	public double intMutationProbability_;
	public double distributionIndex_;

	/**
	 * Constructor
	 */
	public NSGAIIRegion_Settings(String problemName, Problem problem) {
		super(problemName);
		problem_ 					= problem;
		// Default experiments.settings
		populationSize_ 			= Integer.parseInt(Utility.getProperty("POPULATION_SIZE", "100"));
		maxEvaluations_ 			= Integer.parseInt(Utility.getProperty("MAX_EVALUATIONS", "100"));

		realCrossoverProbability_ 	= 0.9;
		intCrossoverProbability_ 	= 0.9;
		
		if ( (problem_ instanceof GeneticModelProblem) && (((GeneticModelProblem) problem_).getNumOfRealVariables() > 0) ){
			realMutationProbability_ 	= 1.0 /((GeneticModelProblem) problem_).getNumOfRealVariables();			
		}
		else
			realMutationProbability_ 	= 1.0 / 1.1;
		if ( (problem_ instanceof GeneticModelProblem) && (((GeneticModelProblem) problem_).getNumOfIntVariables() > 0) ){
			intMutationProbability_ 	= 1.0 /((GeneticModelProblem) problem_).getNumOfIntVariables();			
		}
		else
			intMutationProbability_ 	= 1.0 / 1.1;
		
		distributionIndex_ 			= 20;
	} // NSGAII_Settings

	
	/**
	 * Configure NSGAII with default parameter experiments.settings
	 * 
	 * @return A NSGAII algorithm object
	 * @throws jmetal.util.JMException
	 */
	public Algorithm configure() throws JMException {
		Algorithm algorithm;
		Selection selection;
		Crossover crossover;
		Mutation mutation;

		HashMap<String, Double> parameters; // Operator parameters

		// Creating the algorithm. There are two choices: NSGAII and its steady-state variant ssNSGAII
		MultiProcessEvaluator evaluator = new MultiProcessEvaluator(0);
		algorithm = new pNSGAIIRegion(problem_, evaluator);

		// Algorithm parameters
		algorithm.setInputParameter("populationSize", populationSize_);
		algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

		// Mutation and Crossover for Real codification
		parameters = new HashMap<String, Double>();

		parameters.put("realCrossoverProbability", this.realCrossoverProbability_);
		parameters.put("intCrossoverProbability", this.intCrossoverProbability_);
		parameters.put("distributionIndex", this.distributionIndex_);
		crossover = CrossoverFactory.getCrossoverOperator("SBXSinglePointCrossover", parameters);

		parameters = new HashMap<String, Double>();
		parameters.put("realMutationProbability", this.realMutationProbability_);
		parameters.put("intMutationProbability", this.intMutationProbability_);
		parameters.put("distributionIndex", this.distributionIndex_);
		mutation = MutationFactory.getMutationOperator("PolynomialUniformMutation", parameters);		
		
		// Add the operators to the algorithm
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		
		// Selection Operator
		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament",parameters);
		algorithm.addOperator("selection", selection);

		return algorithm;
	} // configure
	
	
	/**
	 * Configure NSGAII with user-defined parameter experiments.settings
	 * 
	 * @return A NSGAII algorithm object
	 */
	@Override
	public Algorithm configure(Properties configuration) throws JMException {
		return null;
	}
} // NSGAII_Settings
