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

import java.util.ArrayList;
import java.util.List;

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

	
	/** Get the region represented by this region gene 
	 * Region is simply an array [min,max]
	 */
	 public double[] getRegion(){
		return new double[]{(double) this.getMinValue(), (double) this.getMaxValue()};
	}
	 
	
	 /** 
	  * Decompose this region into <b>subRegionsNum</b> regions
	  * @param subRegionsNum
	  * @return
	  */
	 public List<RegionGene> decompose(int subRegionsNum){
		 List<RegionGene> regionGenesList = new ArrayList<RegionGene>();
		 //TODO: decomposing logic, maybe uniform for start?
		 return regionGenesList;
	 }
}
