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

import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.util.JMException;

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
	private RegionObjective[] regionObjectives;

	/** volume parameter (in the objective space)*/
	private double volume;
	
	private RegionParameter[] regionParams;
	
	
	public RegionSolution(Problem problem) throws ClassNotFoundException {
		super(problem);
		init();
	}
	
	
	public RegionSolution (Solution solution){
		super(solution);
		init();
	}
	
	
	private void init(){
		//initialise region objectives array
		this.regionObjectives = new RegionObjective[getNumberOfObjectives()];
		for (int index=0; index < regionObjectives.length; index++){
			regionObjectives[index] = new RegionObjective();
		}
		
		try {
			ArrayReal arrayRealVariable = ((ArrayReal) this.getDecisionVariables()[0]);
			int params		 = 	arrayRealVariable.getLength();
			this.regionParams = new RegionParameter[params];
			for (int i=0; i<params; i++){
					this.regionParams[i] = new RegionParameter(arrayRealVariable.getValue(i), arrayRealVariable.getLowerBound(i), arrayRealVariable.getUpperBound(i));
			}
		} catch (JMException e) {
			e.printStackTrace();
			System.exit(0);
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
	
	
	
	public double getVolume(){
		double vol	= 1;
		for (int i=0; i<regionObjectives.length; i++){
			double min 	= regionObjectives[i].getMinObjective();
			double max 	= regionObjectives[i].getMaxObjective();
//			vol 		*= Math.abs(min-max); 
			if (i==0){
				vol 		*= Math.abs(min-max);
			} else {
				vol 		*= Math.abs(min-max)/100;
			}
			
		}
		this.volume = vol;
		return this.volume;
	}

	
	public double getSensitivity(){
		double volObj	= 1;
		for (int i=0; i<regionObjectives.length; i++){
			double min 	= regionObjectives[i].minObjective;
			double max 	= regionObjectives[i].maxObjective;
			volObj 		*= Math.abs(min-max); 
		}
		double volParam = 1;
		for (int i=0; i<regionParams.length; i++){
			double min 	= regionParams[i].minValue;
			double max 	= regionParams[i].maxValue;
			volParam   *= Math.abs(min-max);
		}
		double sensitivity = volObj/volParam; 
		if (sensitivity <= 0)
			return 0.001;
		else
			return sensitivity;
	}
	
	public String getRegionParameter(int index) throws EvoCheckerException{
		if (index>=0 && index<regionParams.length){
			return regionParams[index].toString();
		}
		else
			throw new EvoCheckerException(this.getClass().getSimpleName() +"\t"+ index + "out of bounds " + regionParams.length);
	}
	
	
	public int getSizeRegionParameters(){
		return this.regionParams.length;
	}
	
	/**
	 * Class representing the region of a parameter
	 * @author sgerasimou
	 *
	 */
	class RegionParameter{
		/** min value*/
		private double minValue;
		
		/** max value*/
		private double maxValue;
		
		private double lowerBound;
		
		private double upperBound;
		
		/** tolerance*/
		private double tolerance = Double.parseDouble(Utility.getProperty("TOLERANCE"));

		
		private RegionParameter(double value, double lowerBound, double upperBound){
			this.lowerBound	= lowerBound;
			this.upperBound	= upperBound;
//			minValue 	  = value-value*tolerance/2;
//			maxValue 	  = value+value*tolerance/2;	
//			if (minValue < lowerBound){
//				minValue	 = lowerBound;
//				maxValue = lowerBound+value*tolerance;
//			}
//			else if (maxValue > upperBound){
//				minValue = upperBound-upperBound*tolerance;
//				maxValue = upperBound;
//			}						
			double radius	= tolerance*Math.abs(upperBound-lowerBound);
			minValue 	  	= value-radius/2;
			maxValue 	  	= value+radius/2;
			if (minValue < lowerBound){
				minValue	= lowerBound;
				maxValue 	= lowerBound+radius;
			}
			else if (maxValue > upperBound){
				minValue = upperBound-radius;
				maxValue = upperBound;
			}						

		}
		
		
		public String toString(){
			return minValue +":"+ maxValue;
		}
		
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
