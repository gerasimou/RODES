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
 * Class representing an evolvable template parser
 * for PRISM-PSY
 * @author sgerasimou
 *
 */
public class ParserEnginePrismPSY extends ParserEngine {

	/**
	 * Class constructor
	 * @param fileName
	 * @param propertiesFilename
	 */
	public ParserEnginePrismPSY(String fileName, String propertiesFilename) {
		super(fileName, propertiesFilename);
	}

	
	/**
	 * Return a model instance conforming to
	 * Prism-PSY semantics
	 * @param individual
	 * @return
	 */
	@Override
	public String getValidModelInstance(List<AbstractGene> individual) {
		
		return null;
	}


}
