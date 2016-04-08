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

import java.util.Comparator;

/**
 * Abstract class associated with a comparator between solutions
 * This class implements a <code>Comparator</code> (a method for comparing
 * <code>Solution</code> objects) based on a constraint violation test + 
 * dominance checking, as in NSGA-II.
 * @author sgerasimou
 *
 */
public abstract class RegionDominanceComparator implements Comparator<Object>{

 /**
  * Compares two solutions.
  * @param object1 Object representing the first <code>Solution</code>.
  * @param object2 Object representing the second <code>Solution</code>.
  * @return -1, or 0, or 1 if .... use dominance logic
  * (e.g., solution1 dominates solution2, both are non-dominated, or solution1  
  * is dominated by solution22, respectively.)
  */
  public abstract int compare(Object o1, Object o2);
}
