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
package evochecker.genetic.jmetal.util;


/** 
 *  Implementation of a-degree Pareto dominance relation described in the paper:
 *  
 *  "Interval-based ranking in noisy evolutionary multi-objective optimization"
 *  by H. Karshenas, C. Bielza, P. Larrañaga
 *  Computational Optimization and Applications, Vol. 61, Pages 517-555, 2015
 */
public class aDegreeDominanceComparator extends RegionDominanceComparator {

	/**
	 * Compares two solutions.
	 * @param object1 Object representing the first <code>Solution</code>.
	 * @param object2 Object representing the second <code>Solution</code>.
	 * @return -1, or 0, or 1 if .... use dominance logic
	 * (e.g., solution1 dominates solution2, both are non-dominated, or solution1  
	 * is dominated by solution22, respectively.)
	 */
	@Override
	public int compare(Object o1, Object o2) {
		return 0;
	}

}
