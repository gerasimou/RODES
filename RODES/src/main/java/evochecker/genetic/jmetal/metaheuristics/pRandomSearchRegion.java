package evochecker.genetic.jmetal.metaheuristics;

import java.util.List;
import java.util.Random;

import evochecker.auxiliary.KnowledgeSingleton;
import evochecker.genetic.jmetal.encoding.solution.RegionSolution;
import evochecker.genetic.jmetal.util.NonDominatedRegionSolutionList;
import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class pRandomSearchRegion extends Algorithm{

	  /** Parallel evaluator handler*/
	IParallelEvaluator parallelEvaluator_ ; 

	/** Random handler */
	Random rand;
	
	/** Knowledge singleton*/
	KnowledgeSingleton knowledge = KnowledgeSingleton.getInstance();
	
	
	/**
	 * Constructor
	 * @param problem Problem to solve
	 * @param evaluator Parallel evaluator
	 */
	public pRandomSearchRegion(Problem problem, IParallelEvaluator evaluator) {
		super(problem);
		parallelEvaluator_ = evaluator;
		rand = new Random (System.currentTimeMillis());	
	}//constructor

	
	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;
		
//		SolutionSet population;
	    NonDominatedRegionSolutionList ndList = new NonDominatedRegionSolutionList();

	    QualityIndicator indicators; // QualityIndicator object

	    //Read the parameters
		populationSize 	= ((Integer)getInputParameter("populationSize")).intValue();
		maxEvaluations 	= ((Integer)getInputParameter("maxEvaluations")).intValue();		
	    indicators 		= (QualityIndicator) getInputParameter("indicators");
		
	    //Start the parallel evaluator
	    parallelEvaluator_.startEvaluator(problem_);

	    //Initialise the variables
//	    population 	= new SolutionSet(populationSize);
	    evaluations	= 0;
	    
	    //Create the initial solution set
	    Solution newSolution;
	    for (int i=0; i<populationSize; i++){
		    	newSolution = new RegionSolution(problem_); //Solution(problem_);
		    	parallelEvaluator_.addSolutionForEvaluation(newSolution);
	    }
	    
	    //Run parallel evaluation
	    List<Solution> solutionList = parallelEvaluator_.parallelEvaluation();
	    
	    //Add the solutions to the population
	    for (Solution solution : solutionList){
	//	    	population.add(solution);
		    	ndList.add(solution);
		    	evaluations++;
	    }

	    
	    //process generation
	    knowledge.processGeneration(ndList);
	    
	    
	    //Iterate until the max generations
	    while (evaluations < maxEvaluations){
	    	System.out.println("Evaluations:\t" + evaluations);
	    	
	    	 for (int i=0; i<populationSize; i++){
	  	    	newSolution = new RegionSolution(problem_); //Solution(problem_);
	  	    	parallelEvaluator_.addSolutionForEvaluation(newSolution);   
	    	 }
	    	
		    //Run parallel evaluation
		   solutionList = parallelEvaluator_.parallelEvaluation();
	    
		   //Add the solutions to the population
		    for (Solution solution : solutionList){
			    	ndList.add(solution);
			    	evaluations++;
		    }
		    
		    
		  //keep pareto size to a maximum population size
		    while (ndList.size()>populationSize){
			    	int size 	= ndList.size();
			    	int index	= rand.nextInt(size);
			    	ndList.remove(index);
		    }
		    
		    //process generation
		    knowledge.processGeneration(ndList);
	    }
	    
	    parallelEvaluator_.stopEvaluators();
	    
	    // Return as output parameter the required evaluations
//	    setOutputParameter("evaluations", requiredEvaluations);

	    return ndList;
	    
	}//execute

	
}
