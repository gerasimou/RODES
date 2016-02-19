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

package evochecker.parser;

/**
 * Class representing a PrismPSY instantiator
 * constructing a parametric model in which evolvable integers
 * and evolvable modules have concrete values while evolvable doubles
 * and distributions remain unchanged
 * @author sgerasimou
 *
 */
public interface InstantiatorInterfacePrismPSY extends InstantiatorInterface{

	/**
	 * Get parameters with ranges as a string
	 *  <param_name>=min:max, <param_name>=min:max,... 
	 * @return
	 */
	public String getParamsWithRanges();

	/**
	 * Get decomposition type
	 * @return
	 */
	public String getDecompositionType();
	
	/** Get accuracy*/
	public String getAccuracy();
}
