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

package evochecker.genetic.jmetal.single;

import jmetal.core.SolutionSet;
import jmetal.util.JMException;

public interface AlgorithmSteps {
	
	
  
	/**
   * Do any necessary initialisations
   */
	public void initialise();
	
  
	/**
	* Do any necessary cleanup
	*/
	public void finalise();
	
	
	/**
	 * Execute algorithm logic
	 * @return
	 * @throws JMException
	 * @throws ClassNotFoundException
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException;

	
	/**
	 * Initialise population
	 */
	public void createInitialPopulation() throws ClassNotFoundException;

}
