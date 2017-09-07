//  pNSGAIIRegion.java
//
//  Author(s):
//		 Simos Gerasimou  <simos@cs.york.ac.uk>
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
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

import java.util.List;

import evochecker.auxiliary.KnowledgeSingleton;
import evochecker.genetic.jmetal.encoding.solution.RegionSolution;
import evochecker.genetic.jmetal.util.ExampleRegionDistance;
import evochecker.genetic.jmetal.util.RegionDistance;
import evochecker.genetic.jmetal.util.RegionDominanceComparator;
import evochecker.genetic.jmetal.util.RegionRanking;
import evochecker.genetic.jmetal.util.eDominanceRevisedWorstCaseDominanceComparator;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.comparators.CrowdingComparator;

/** 
 *  Modified implementation of NSGA-II for regions
 * 
 */

public class pNSGAIIRegion extends Algorithm {

  /** Parallel evaluator handler*/
  IParallelEvaluator parallelEvaluator_ ; 

  /** Region comparator handler*/
  RegionDominanceComparator regionDominanceComparator;
  
  /** Region distance handler*/
  RegionDistance regionDistance;
  
  /** Knowledge singleton*/
  KnowledgeSingleton knowledge = KnowledgeSingleton.getInstance();
  
  int currentGeneration = 0;
  
  /**
   * Constructor
   * @param problem Problem to solve
   * @param evaluator Parallel evaluator
   */
  public pNSGAIIRegion(Problem problem, IParallelEvaluator evaluator) {
    super (problem) ;

    this.parallelEvaluator_ = evaluator;

    //New commands for regions: SET THE DOMINANCE & DISTANCE COMPARATOR
    this.regionDominanceComparator	= new eDominanceRevisedWorstCaseDominanceComparator(); 
    		//new eDominanceWorstCaseDominanceComparator();
    this.regionDistance				= new ExampleRegionDistance();

  } // pNSGAII

  
  
  /**   
   * Runs the NSGA-II algorithm.
   * @return a <code>SolutionSet</code> that is a set of non dominated solutions
   * as a result of the algorithm execution
   * @throws JMException 
   */
  public SolutionSet execute() throws JMException, ClassNotFoundException {
    int populationSize;
    int maxEvaluations;
    int evaluations;

    QualityIndicator indicators; // QualityIndicator object
    int requiredEvaluations; // Use in the example of use of the
    // indicators object (see below)

    SolutionSet population;
    SolutionSet offspringPopulation;
    SolutionSet union;

    Operator mutationOperator;
    Operator crossoverOperator;
    Operator selectionOperator;

    //Read the parameters
    populationSize 	= ((Integer) getInputParameter("populationSize")).intValue();
    maxEvaluations 	= ((Integer) getInputParameter("maxEvaluations")).intValue();
    indicators 		= (QualityIndicator) getInputParameter("indicators");

    parallelEvaluator_.startEvaluator(problem_) ;

    //Initialize the variables
    population = new SolutionSet(populationSize);
    evaluations = 0;

    requiredEvaluations = 0;

    //Read the operators
    mutationOperator 	= operators_.get("mutation");
    crossoverOperator 	= operators_.get("crossover");
    selectionOperator 	= operators_.get("selection");
    
    // Create the initial solutionSet
    Solution newSolution;
    for (int i = 0; i < populationSize; i++) {
      newSolution = new RegionSolution(problem_); //Solution(problem_);
      parallelEvaluator_.addSolutionForEvaluation(newSolution) ;
    }

    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation() ;
    for (Solution solution : solutionList) {
      population.add(solution) ;
      evaluations ++ ;
    }
    
    //process generation
    knowledge.processGeneration(population, currentGeneration++);
        
    // Generations 
    while (evaluations < maxEvaluations) {
    		System.out.println("Evaluations:\t" + evaluations);
    	    	knowledge.addMessage("Evaluations:\t" + evaluations);
    		
	      // Create the offSpring solutionSet      
	      offspringPopulation = new SolutionSet(populationSize);
	      Solution[] parents = new Solution[2];
	      for (int i = 0; i < (populationSize / 2); i++) {
	        if (evaluations < maxEvaluations) {
	          //obtain parents
	          parents[0] = (Solution) selectionOperator.execute(population);
	          parents[1] = (Solution) selectionOperator.execute(population);
	          Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
	          mutationOperator.execute(offSpring[0]);
	          mutationOperator.execute(offSpring[1]);
	          parallelEvaluator_.addSolutionForEvaluation(new RegionSolution(offSpring[0])) ;
	          parallelEvaluator_.addSolutionForEvaluation(new RegionSolution(offSpring[1])) ;
	        } // if                            
	      } // for
	
	      List<Solution> solutions = parallelEvaluator_.parallelEvaluation() ;
	
	      for(Solution solution : solutions) {
	        offspringPopulation.add(solution);
	        evaluations++;	    
	      }
	
	      // Create the solutionSet union of solutionSet and offSpring
	      union = ((SolutionSet) population).union(offspringPopulation);
	
	      // Ranking the union
	      //Ranking ranking = new Ranking(union);
	      RegionRanking ecRanking = new RegionRanking(union, regionDominanceComparator);
	
	      int remain = populationSize;
	      int index = 0;
	      SolutionSet front = null;
	      population.clear();
	
	      // Obtain the next front
	      front = ecRanking.getSubfront(index);
	
	      while ((remain > 0) && (remain >= front.size())) {
	        //Assign crowding distance to individuals
	        regionDistance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
	        //Add the individuals of this front
	        for (int k = 0; k < front.size(); k++) {
	          population.add(front.get(k));
	        } // for
	
	        //Decrement remain
	        remain = remain - front.size();
	
	        //Obtain the next front
	        index++;
	        if (remain > 0) {
	          front = ecRanking.getSubfront(index);
	        } // if        
	      } // while
	
	      // Remain is less than front(index).size, insert only the best one
	      if (remain > 0) {  // front contains individuals to insert                        
	        regionDistance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
	        front.sort(new CrowdingComparator());
	        for (int k = 0; k < remain; k++) {
	          population.add(front.get(k));
	        } // for
	
	        remain = 0;
	      } // if                               
	
	      //process generation
	      knowledge.processGeneration(ecRanking.getSubfront(0), currentGeneration++);

	      // This piece of code shows how to use the indicator object into the code
	      // of NSGA-II. In particular, it finds the number of evaluations required
	      // by the algorithm to obtain a Pareto front with a hypervolume higher
	      // than the hypervolume of the true Pareto front.
	//      if ((indicators != null) &&
	//          (requiredEvaluations == 0)) {
	//        double HV = indicators.getHypervolume(population);
	//        if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
	//          requiredEvaluations = evaluations;
	//        } // if
	//      }// if
	      
	    } // while
	
	    parallelEvaluator_.stopEvaluators();
	
	    // Return as output parameter the required evaluations
	    setOutputParameter("evaluations", requiredEvaluations);
	
	    // Return the first non-dominated front
	    RegionRanking ecRanking = new RegionRanking(population, regionDominanceComparator);
	    return ecRanking.getSubfront(0);
  } // execute
} // pNSGAII
