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

/**
 * Class representing an evolvable double element
 * @author sgerasimou
 *
 */
public class EvolvableDouble extends Evolvable {
	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 */
	public EvolvableDouble(String name, Number minValue, Number maxValue){
		super(name, minValue, maxValue, EvolvableID.CONSTANT_DOUBLE);
	}
	
	
	/**
	 * Print toString()
	 */
	public String toString(){
		String str = super.toString();
		str += "["+ this.minValue +":"+ this.maxValue +"]";
		return str;
	}


	/**
	 * Get command 
	 */
	@Override
	public String getCommand(Object variable) {
		//change made to accommodate PrismPSY
		return "const double " + name +";";// = "+ (double)variable  +";\n";
	}
}
