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
	
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public RegionGene(String name, Number minValue, Number maxValue) {
		super(name, minValue, maxValue);
		this.regionRadius = Double.parseDouble(Utility.getProperty("REGION_RADIUS_".concat(name).toUpperCase()));
	}
	
	
	
	/**
	 * Returns the radius for this region
	 * @return
	 */
	public double getRegionRadius(){
		return this.regionRadius;
	}
}
