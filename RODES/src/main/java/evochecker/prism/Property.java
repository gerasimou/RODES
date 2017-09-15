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
public abstract class Property {
	
	protected boolean maximization;
			

	public Property(boolean maximization){
		this.maximization = maximization;
	}

	
	public boolean isMaximization() {
		return maximization;
	}

	
	public void setMaximization(boolean maximization) {
		this.maximization = maximization;
	}
	
	
	public Property (Property aProperty){
		this.maximization 	= aProperty.maximization;
	}
	
	
	public abstract double evaluate (double result);// {return 0;};
	
	

}
