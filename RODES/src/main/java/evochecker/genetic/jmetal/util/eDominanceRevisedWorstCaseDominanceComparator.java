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

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.jmetal.encoding.solution.RegionSolution;
import jmetal.util.comparators.IConstraintViolationComparator;
import jmetal.util.comparators.OverallConstraintViolationComparator;

public class eDominanceRevisedWorstCaseDominanceComparator extends RegionDominanceComparator {

    IConstraintViolationComparator violationConstraintComparator_;
    
    private final boolean SENSITIVITY; //use sensitivity in the comparator
    private final double EPSILON; 		


    public eDominanceRevisedWorstCaseDominanceComparator() {
		SENSITIVITY = Boolean.parseBoolean(Utility.getProperty(Constants.SENSITIVITY_KEYWORD));
		EPSILON	= Double.parseDouble(Utility.getProperty(Constants.EPSILON_KEYWORD));
        violationConstraintComparator_ = new OverallConstraintViolationComparator(); 
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

        RegionSolution solution1 = (RegionSolution)object1;
        RegionSolution solution2 = (RegionSolution)object2;

		int dominate1 ; // dominate1 indicates if some objective of solution1
		// dominates the same objective in solution2. dominate2
		int dominate2 ; // is the complementary of dominate1.

		dominate1 = 0 ;
		dominate2 = 0 ;

		// Test to determine whether at least a solution violates some constraint
	    if (violationConstraintComparator_.needToCompare(solution1, solution2))
	      return violationConstraintComparator_.compare(solution1, solution2) ;


		// Equal number of violated constraints. Applying a dominance Test then
		double value1, value2;//, vol1 = 0, vol2 = 0;
		for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
			
			value1 = solution1.getObjectiveBounds(i)[1]; // maxBound = worst case
			value2 = solution2.getObjectiveBounds(i)[1]; // maxBound = worst case
			
			if (value1 < value2) 
				dominate1 = 1;
			else if (value1 > value2) 
				dominate2 = 1;

		}
		
        //domination criteria
		if (dominate1 == dominate2) {
			return 0; //No one dominate the other
		}
		
		//check epsilon dominance, i.e., first part of the new dominance relation
		boolean epsilonBetter = false;
		if (EPSILON > 0){
			if (dominate1 > dominate2){
				for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
					value1 = solution1.getObjectiveBounds(i)[1]; // maxBound = worst case
					value2 = solution2.getObjectiveBounds(i)[1]; // maxBound = worst case
					
					if (value1<0 && ((1+EPSILON)*value1 > value2) )		//maximisation objective
						epsilonBetter = true;
					else if (value1>=0 && ((1+EPSILON)*value1 < value2 ) ) //minimisation objective
						epsilonBetter = true;
				}
			}
			else if (dominate1 < dominate2){
				for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
					value1 = solution1.getObjectiveBounds(i)[1]; // maxBound = worst case
					value2 = solution2.getObjectiveBounds(i)[1]; // maxBound = worst case
					
					if (value2<0 && (value1 < (1+EPSILON)*value2) )		//maximisation objective
						epsilonBetter = true;
					else if (value2>=0 && (value1 > (1+EPSILON)*value2 ) )	//minimisation objective
						epsilonBetter = true;
				}
			}
		
			if (epsilonBetter){
				if (dominate1 == 1) {
					return -1; // solution1 dominates
				}
				return 1;    // solution2 dominates
			}
		}
		
		//check sensitivity, i.e., second part of the new dominance relation
		if (SENSITIVITY){
			double maxDelta = Double.NEGATIVE_INFINITY;
			double sensitivity1 = solution1.getSensitivity();
        		double sensitivity2 = solution2.getSensitivity();
        	
			if (dominate1 > dominate2){
				for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
					value1 = solution1.getObjectiveBounds(i)[1]; // maxBound = worst case
					value2 = solution2.getObjectiveBounds(i)[1]; // maxBound = worst case
					
					double onePlusDelta = Double.NEGATIVE_INFINITY;
					if (value1<=0 && value2<=0)
						onePlusDelta = value1/value2;
					else if (value1>0 && value2>0)
						onePlusDelta = value2/value1;
					double delta		= onePlusDelta - 1;
					if (maxDelta < delta)
						maxDelta = delta;
				}
			}
			else if (dominate1 < dominate2){
				for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
					value1 = solution1.getObjectiveBounds(i)[1]; // maxBound = worst case
					value2 = solution2.getObjectiveBounds(i)[1]; // maxBound = worst case
					
					double onePlusDelta = Double.NEGATIVE_INFINITY;
					if (value1<=0 && value2<=0)
						onePlusDelta = value2/value1;
					else if (value1>0 && value2>0)
						onePlusDelta = value1/value2;
					double delta		= onePlusDelta - 1;
					if (maxDelta < delta)
						maxDelta = delta;
				}
			}
			
			try {
				if (maxDelta < 0)
				throw new EvoCheckerException("MaxDelta <0\t" + maxDelta);
			} catch (EvoCheckerException e) {
				e.printStackTrace();
			}
			
			if (dominate1 > dominate2){
				if (sensitivity1 < sensitivity2)
					return -1;
				else if (sensitivity1 < (1+maxDelta)*sensitivity2) {
					System.out.println("Added due to new dominance relation -1");
					return -1;
				}
			}
			else if (dominate1 < dominate2){
				if (sensitivity1 > sensitivity2)
					return 1;					
				else if ((1+maxDelta)*sensitivity1 > sensitivity2) {
					System.out.println("Added due to new dominance relation -1");
					return 1;
				}
			}
		}

		//if all domination criteria fail
		return 0; //No one dominates the other
        
//        //domination criteria
//		if (dominate1 == dominate2) {
//			return 0; //No one dominates the other
//		}
//		if (dominate1 == 1) {
//			return -1; // solution1 dominates
//		}
//		return 1;    // solution2 dominates
	} // compare

}


