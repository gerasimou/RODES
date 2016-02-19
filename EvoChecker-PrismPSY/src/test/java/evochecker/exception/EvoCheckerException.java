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

package evochecker.exception;

/**
 * EvoChecker exception class
 * @author sgerasimou
 *
 */
public class EvoCheckerException extends Exception {
	
	/**
	 * Class constructor
	 * @param s
	 */
	public EvoCheckerException(String s)
	{
		super(s);
	}
	
	public String toString()
	{
		return "Error: " + getMessage() + ".";
	}
}
