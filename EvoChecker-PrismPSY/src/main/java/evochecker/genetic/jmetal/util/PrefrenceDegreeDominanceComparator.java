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
 *  Implementation of preference degree comparator described in the paper:
 *  
 *  "A preference aggregation method through the estimation of utility intervals"
 *  by Y.-M. Wanga, J.-B. Yanga, D.-L. Xua
 *  Computers & Operations Research, Vol. 32, Pages 2027-2049, 2005
 */
public class PrefrenceDegreeDominanceComparator extends RegionDominanceComparator {

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
