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
package evochecker.genetic.genes;

import evochecker.auxiliary.Utility;


/**
 * Class representing a region gene
 * @author sgerasimou
 *
 */
public class RegionGene extends AbstractGene {

	/** Variable representing the radius of this region
	 * e.g., if point=+5, regionRadius=+1 ==> region = [4,6]  
	 */
	private double tolerance;
	
	private double radius;
	
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public RegionGene(String name, Number minValue, Number maxValue) {
		super(name, minValue, maxValue);
		this.tolerance = Double.parseDouble(Utility.getProperty("TOLERANCE"));//.concat(name).toUpperCase()));
		this.radius	   = Math.abs(maxValue.doubleValue()-minValue.doubleValue()) *tolerance;
	}
	
	
	
	/**
	 * Returns the radius for this region
	 * @return
	 */
	public double getRegionRadius(){
		return this.radius;
	}
}
