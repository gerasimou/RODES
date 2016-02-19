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

import java.util.List;

import evochecker.genetic.genes.AbstractGene;

/**
 * Interface used to instantiate a concrete model 
 * @author sgerasimou
 *
 */
public interface InstantiatorInterface {
	
	
	/**
	 * Return a valid prism model instance
	 * @param individual
	 * @return
	 */
	public String getPrismModelInstance(List<AbstractGene> individual);
	
	
	/**
	 * Return the name of properties file
	 */
	public String getPrismPropertyFileName();

}
