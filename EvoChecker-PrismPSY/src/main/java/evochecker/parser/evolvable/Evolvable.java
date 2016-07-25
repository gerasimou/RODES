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
 * Abstract class representing an evolvable element
 * @author sgerasimou
 *
 */
public abstract class Evolvable {
	
	/** evolvable name*/
	protected String name;
	
	/** evolvable identifier*/
	protected String identifier;
	
	/** evolvable minimum value*/
	protected Number minValue;
	
	/** evolvable maximum value*/
	protected Number maxValue;

	
	/**
	 * Class constructor
	 * @param name
	 * @param minValue
	 * @param maxValue
	 * @param evolvableID
	 */
	public Evolvable (String name, Number minValue, Number maxValue, EvolvableID evolvableID){
		this.name 		= name;
		this.minValue	= minValue;
		this.maxValue	= maxValue;
		this.identifier	= EvolvableID.getEvolvableIDLiteral(evolvableID) + name; 
	}
	
	
	/** 
	 * Get minimum value
	 */
	public Number getMinValue(){
		return this.minValue;
	}
	
	
	/** 
	 * Get maximum value
	 */
	public Number getMaxValue(){
		return this.maxValue;
	}

	
	/**
	 * Get name
	 * @return
	 */
	public String getName(){
		return this.name;
	}

	
	/**
	 * Get identifier
	 * @return
	 */
	public String getIdentifier (){
		return this.identifier;
	}
	
	
	/**
	 * Print 
	 */
	public String toString(){
		return (this.name +" ("+ this.identifier +")"); 
	}
	
	
	/**
	 * Get this evolvable command
	 * @param variable
	 * @return
	 */
	public abstract String getCommand(Object variable);
	
	
	
	public Evolvable (Evolvable anEvolvable){
		this.name 		= anEvolvable.name;
		this.identifier	= anEvolvable.identifier;
		this.minValue	= anEvolvable.minValue;
		this.maxValue	= anEvolvable.maxValue;
	}
}
