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
 *  "Numerical methods for interval and fuzzy number comparison based on the probabilistic approach and Dempster–Shafer theory"
 *  by P. Sevastianov
 *  Information Sciences, Vol. 177, Pages 4645–4661, 2007
 */
public class ProbabilisticDominanceComparator extends RegionDominanceComparator {

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
