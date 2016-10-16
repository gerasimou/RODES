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

import evochecker.auxiliary.Utility;
import evochecker.genetic.jmetal.encoding.solution.RegionSolution;
import jmetal.util.comparators.IConstraintViolationComparator;
import jmetal.util.comparators.OverallConstraintViolationComparator;

public class eToleranceWorstCaseDominanceComparator extends RegionDominanceComparator {

	double epsilon; 		//relative epsilon-dominance
    boolean sensitivity; 	//use sensitivity in the comparator
    double paramVolume; 	// volume of the parameter space
    IConstraintViolationComparator violationConstraintComparator_;
    
    private final double LENIENCY = Double.parseDouble(Utility.getProperty("LENIENCY"));

	public eToleranceWorstCaseDominanceComparator(double epsilon, double paramVolume, boolean sensitivity) {
		this.epsilon 		= epsilon;
        this.paramVolume 	= paramVolume;
        this. sensitivity 	= sensitivity;
        this.violationConstraintComparator_ = new OverallConstraintViolationComparator(); 
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

		int flag=-2; //stores the result of the comparison

		// Test to determine whether at least a solution violates some constraint
	    if (violationConstraintComparator_.needToCompare(solution1, solution2))
	      return violationConstraintComparator_.compare(solution1, solution2) ;


		// Equal number of violated constraints. Applying a dominance Test then
		double value1, value2;//, vol1 = 0, vol2 = 0;
		for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
			
			value1 = solution1.getObjectiveBounds(i)[1]; // maxBound = worst case
			value2 = solution2.getObjectiveBounds(i)[1]; // maxBound = worst case
			
//			//check the volume
//			if (i == 0){
//                vol1 = value1 - solution1.getObjectiveBounds(i)[0];
//                vol2 = value2 - solution2.getObjectiveBounds(i)[0];
//            } else {
//                vol1 = vol1 * (value1 - solution1.getObjectiveBounds(i)[0]);
//                vol2 = vol2 * (value2 - solution2.getObjectiveBounds(i)[0]);
//            }
			if ((1+epsilon)*value1 < value2) {
				flag = -1;
			} else if (value1 > (1+epsilon)*value2) {
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

        // sensitivity - I don't use the volume of the parameter space since it is fixed
        if (sensitivity) {
        	double vol1 = solution1.getSensitivity();// getVolume();
        	double vol2 = solution2.getSensitivity();//getVolume();
        	flag = 0;
//        	double LENIENCY = 0.1;
        	if (vol1 < vol2){
        		if (dominate2 > dominate1){//if sol2 dominates sol1, but sol1 has better sensitivity 
        			double v1, v2;//, vol1 = 0, vol2 = 0;
        			boolean lenientDomination = true;
        			for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
        				v1 = solution1.getObjectiveBounds(i)[1]; // maxBound = worst case
        				v2 = solution2.getObjectiveBounds(i)[1]; // maxBound = worst case
        			
        				if (v1<0 && v1 * (1+LENIENCY) > v2){
        					lenientDomination = false;
        					break;
        				}
        				else if (v1>=0 && v1 > v2 * (1-LENIENCY) ){
        					lenientDomination = false;
        					break;
        				}
        			}
        			if (lenientDomination)
        				flag=-1;
        		}
        	}
        	else if (vol1 > vol2){
        		if (dominate1 > dominate2){//if sol1 dominates sol2, but sol2 has better sensitivity
        			double v1, v2;
        			boolean lenientDomination = true;
        			for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
        				v1 = solution1.getObjectiveBounds(i)[1]; // maxBound = worst case
        				v2 = solution2.getObjectiveBounds(i)[1]; // maxBound = worst case
        				if (v1<0 && v1 < v2 * (1+LENIENCY)){
        					lenientDomination = false;
        					break;
        				}
        				else if (v1>=0 && v1 * (1-LENIENCY) < v2){
        					lenientDomination = false;
        					break;
        				}
        			}
        			if (lenientDomination)
        				flag=1;
        		}
        	}
        	else
        		flag = 0;
        	
//            //System.out.println(vol1 + " " + vol2);
//            if ((1-epsilon)*vol1 < vol2) {
//                flag = -1;
//            } else if (vol1 > (1-epsilon)*vol2) {
//                flag = 1;
//            } else {
//                flag = 0;
//            }

            if (flag == -1) {
                dominate1 = 1;
            }

            if (flag == 1) {
                dominate2 = 1;
            }
        }

        
        //domination criteria
		if (dominate1 == dominate2) {
			return 0; //No one dominate the other
		}
		if (dominate1 == 1) {
			return -1; // solution1 dominate
		}
		return 1;    // solution2 dominate
	} // compare

}


