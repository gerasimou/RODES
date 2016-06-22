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
import evochecker.exception.EvoCheckerException;


/**
 * Class representing a region gene
 * @author sgerasimou
 *
 */
public class RegionGene extends AbstractGene {

	/** Variable representing the radius of this region
	 * e.g., if point=+5, regionRadius=+1 ==> region = [4,6]  
	 */
	private double regionRadius;
	
	/** region boundaries*/
	private double regionMin;
	private double regionMax;
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public RegionGene(String name, Number minValue, Number maxValue) {
		super(name, minValue, maxValue);
		this.regionMin 	= Double.parseDouble(Utility.getProperty("REGION_RADIUS_".concat(name +"_MIN").toUpperCase()));
		this.regionMax 	= Double.parseDouble(Utility.getProperty("REGION_RADIUS_".concat(name +"_MAX").toUpperCase()));
//		this.regionRadius = Double.parseDouble(Utility.getProperty("REGION_RADIUS_".concat(name).toUpperCase()));
	}
	
	
	
	/**
	 * Returns the radius for this region
	 * @return
	 */
	public double getRegionRadius(){
		return this.regionRadius;
	}
	
	
	public void setRegionRadius(double radius) throws EvoCheckerException{
		if (radius<this.regionMin || radius>this.regionMax)
			throw new EvoCheckerException("Radius outside boundaries");
		this.regionRadius = radius;
	}
	
	
	public double getRegionMin(){
		return this.regionMin;
	}
	
	
	public double getRegionMax(){
		return this.regionMax;
	}
}
