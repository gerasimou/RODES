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
package evochecker.genetic.jmetal.encoding.solution;

import jmetal.core.Problem;
import jmetal.core.Solution;

/**
 * Class representing a region solution
 * @author sgerasimou
 * 
 * <b>Note:</b> At the moment, this class is identical to {@link jmetal.core.Solution}
 * with the addition of a radius that represents the bounds of the
 *
 */
public class RegionSolution extends Solution{  
	
	
	/** Array for keeping the objectives regions*/
	private final RegionObjective[] regionObjectives;
	
	
	public RegionSolution(Problem problem) throws ClassNotFoundException {
		super(problem);
		
		//initialise region objectives array
		this.regionObjectives = new RegionObjective[getNumberOfObjectives()];
		for (int index=0; index < regionObjectives.length; index++){
			regionObjectives[index] = new RegionObjective();
		}
	}
	
	
	public RegionSolution (Solution solution){
		super(solution);
		
		//initialise region objectives array
		this.regionObjectives = new RegionObjective[getNumberOfObjectives()];
		for (int index=0; index < regionObjectives.length; index++){
			regionObjectives[index] = new RegionObjective();
		}
	}
	
	
	/** Sets the bounds & midpoint of the i-th objective.
	 * @param i The number identifying the objective.
	 * @param min The lower bound
	 * @param max The upper bound
	 */ 
	public void setObjectiveBounds(int i, double min, double max) {
		  //set region objective bounds
		  regionObjectives[i].setObjectiveBounds(min, max);
		  
		  //set objective midpoint
		  super.setObjective(i, (min+max)/2);
	  }
	
	
	public Double[] getObjectiveBounds(int i){
		return regionObjectives[i].getObjectiveBounds();
	}
	
	
	
	/**
	 * Class representing a region for an objective
	 * @author sgerasimou
	 *
	 */
	class RegionObjective{
		/** min objective*/
		private double minObjective;
		
		/** max objective*/
		private double maxObjective;
		
		
		/** 
		 * Class constructor
		 */
		public RegionObjective() {
			this.minObjective = Double.NEGATIVE_INFINITY;
			this.maxObjective = Double.POSITIVE_INFINITY;
		}
		
		
		/**
		 * Set the region boundaries
		 * @param min
		 * @param max
		 */
		private void setObjectiveBounds(double min, double max){
			this.minObjective = min;
			this.maxObjective = max;
		}
		
		
		/**
		 * Get objective lower bound
		 * @return
		 */
		private double getMinObjective(){
			return this.minObjective;
		}
		
		
		/**
		 * Get objective upper bound
		 * @return
		 */
		private double getMaxObjective(){
			return this.maxObjective;
		}
		
		
		/**
		 * Get the objective bounds as a double array [min, max]
		 * @return
		 */
		private Double[] getObjectiveBounds(){
			return new Double[]{this.minObjective, this.maxObjective};
		}
		
	}
}
