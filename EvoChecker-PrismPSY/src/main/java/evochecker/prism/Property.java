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

package evochecker.prism;


/**
 * Class representing a property to be checked
 * TODO: add constraint handling and property evaluation
 * @author sgerasimou
 *
 */
public class Property {
	
	private boolean maximization;
	
	private double result;
		
	public Property(boolean maximization){
		this.maximization = maximization;
	}

	public boolean isMaximization() {
		return maximization;
	}

	public void setMaximization(boolean maximization) {
		this.maximization = maximization;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}
	
	public Property (Property aProperty){
		this.maximization 	= aProperty.maximization;
		this.result			= aProperty.result;
	}
	
	

}
