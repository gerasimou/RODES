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

import jmetal.core.SolutionSet;

/**
 * Abstract class associated with a region distance facility
 * All classes realising an implementation of a distance between
 * solutions representing a region must extend this class 
 * 
 * @author sgerasimou
 *
 */
public abstract class RegionDistance{
	
	
	/**
	 * Class constructor: create a new region dinstance
	 */
	public RegionDistance() {
		//Do nothing
	}
	
 
	/** Assigns crowding distances to all solutions in a <code>SolutionSet</code>.
	 * @param solutionSet The <code>SolutionSet</code>.
	 * @param nObjs Number of objectives.
	 */
	public abstract void crowdingDistanceAssignment(SolutionSet solutionSet, int nObjs);
}
