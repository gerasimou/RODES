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


/**
 * Class representing a distribution gene
 * @author sgerasimou
 *
 */
public class DiscreteDistributionGene extends AbstractGene {

	/** number of distribution outcome*/
	private int numberOfOutcomes;

	
	/**
	 * Class constructor
	 * @param name
	 * @param numberOfOutcomes
	 */
	public DiscreteDistributionGene(String name, int numberOfOutcomes) {
		super(name, 0.0, 1.0, 0);
		this.numberOfOutcomes = numberOfOutcomes;
	}

	
	/**
	 * Get the number of outcomes
	 * @return
	 */
	public int getNumberOfOutcomes() {
		return numberOfOutcomes;
	}	
}
