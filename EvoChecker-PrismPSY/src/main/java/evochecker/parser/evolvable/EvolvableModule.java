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

package evochecker.parser.evolvable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an evolvable module element
 * @author sgerasimou
 *
 */
public class EvolvableModule extends Evolvable {

	/** list of evolvable distributions for this module*/
	List<EvolvableDistribution> evolvableDistributionList;
	
	/** string representation of this module*/
	String moduleString;
	
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 * @param moduleString
	 */
	public EvolvableModule(String name, int minValue, int maxValue, String moduleString){
		super(name, minValue, maxValue, EvolvableID.MODULE);
		this.evolvableDistributionList = new ArrayList<EvolvableDistribution>();
		this.moduleString = new String(moduleString);
	}

	
	/**
	 * Copy constructor
	 * @param evolvableModule
	 */
	public EvolvableModule (EvolvableModule evolvableModule){
		this(evolvableModule.name, 0, 1, evolvableModule.moduleString);
		for (EvolvableDistribution evolvableDistribution : evolvableModule.evolvableDistributionList){
			appendEvolvableDistribution(evolvableDistribution);
		}
	}

	
	/**
	 * Get string representation of this module
	 * @return
	 */
	public String getModuleString(){
		return this.moduleString;
	}
	
	
	/**
	 * Set string representation of this module
	 * @param moduleString
	 */
	public void setModuleString(String moduleString){
		this.moduleString = moduleString;
	}

	
	/**
	 * Append evolvable distribution to the module
	 * @param evolvableDistribution
	 */
	public void appendEvolvableDistribution(EvolvableDistribution evolvableDistribution){
		this.evolvableDistributionList.add(new EvolvableDistribution(evolvableDistribution));
	}
	
	
	/**
	 * Print toString()
	 */
	public String toString(){
		String str = super.toString();
		str += "["+ this.minValue +":"+ this.maxValue +"]";
		str += moduleString;
		return str;
	}
	
	
	/**
	 * Get command
	 */
	@Override
 	public String getCommand(Object variable) {
		return "//Not Implemented Yet";//const double " + name +" = "+ (double)variables[0]  +";";
	}

	
}
