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

public class eWorstCaseDominanceComparator extends RegionDominanceComparator {

	double epsilon; //relative

	public eWorstCaseDominanceComparator(double epsilon) {
		this.epsilon = epsilon;
	}


	/**
	 * Compares two solutions.
	 * @param object1 Object representing the first <code>Solution</code>.
	 * @param object2 Object representing the second <code>Solution</code>.
	 * @return -1, or 0, or 1 if .... use dominance logic
	 * (e.g., solution1 dominates solution2, both are non-dominated, or solution1  
	 * is dominated by solution22, respectively.)
	 */
	@Override
	public int compare(Object object1, Object object2) {
		if (object1==null)
			return 1;
		else if (object2 == null)
			return -1;

        evochecker.genetic.jmetal.encoding.solution.RegionSolution solution1 = (evochecker.genetic.jmetal.encoding.solution.RegionSolution)object1;
        evochecker.genetic.jmetal.encoding.solution.RegionSolution solution2 = (evochecker.genetic.jmetal.encoding.solution.RegionSolution)object2;

		int dominate1 ; // dominate1 indicates if some objective of solution1
		// dominates the same objective in solution2. dominate2
		int dominate2 ; // is the complementary of dominate1.

		dominate1 = 0 ;
		dominate2 = 0 ;

		int flag; //stores the result of the comparison

		// Test to determine whether at least a solution violates some constraint
		// No constraints YET

		// Equal number of violated constraints. Applying a dominance Test then
		double value1, value2;
		for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
            value1 = solution1.getObjectiveBounds(i)[1]; // maxBound = worst case
			value2 = solution2.getObjectiveBounds(i)[1]; // maxBound = worst case
			if (value1 < value2) {
				flag = -1;
			} else if (value1 > value2) {
				flag = 1;
			} else {
				flag = 0;
			}

			if (flag == -1) {
				dominate1 = 1;
			}

			if (flag == 1) {
				dominate2 = 1;
			}
		}

		if (dominate1 == dominate2) {
			return 0; //No one dominate the other
		}
		if (dominate1 == 1) {
			return -1; // solution1 dominate
		}
		return 1;    // solution2 dominate
	} // compare

}


