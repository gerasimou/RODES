package evochecker.auxiliary;

/**
Collected constants of general utility.
<P>All members of this class are immutable. 
*/

public class Constants {

  /** The caller should be prevented from constructing objects of 
   * this class, by declaring this private constructor. 
   */
  private Constants() {
    //this prevents even the native class from 
    //calling this ctor as well :
    throw new AssertionError();
  }
  
  
  /** Keyword for probabilistic model template*/
  public static final String MODEL_FILE_KEYWORD = "MMODEL_TEMPLATE_FILE";

  /** Keyword for probabilistic properties file*/
  public static final String PROPERTIES_FILE_KEYWORD = "PPROPERTIES_FILE";

  /** Keyword for algorithm*/
  public static final String ALGORITHM_KEYWORD = "AALGORITHM";

  /** Keyword for tolerance*/
  public static final String TOLERANCE_KEYWORD = "TTOLERANCE";

  /** Keyword for multiple tolerance values*/
  public static final String TOLERANCES_KEYWORD = "TTOLERANCES";
  
  /** Keyword for epsilon*/
  public static final String EPSILON_KEYWORD = "EEPSILON";

  /** Keyword for multiple epsilon values*/
  public static final String EPSILONS_KEYWORD = "EEPSILONS";

  /** Keyword for sensitivity*/
  public static final String SENSITIVITY_KEYWORD = "SSENSITIVITY";
  
  /** Keyword for maximum evaluations*/
  public static final String MAX_EVALUATIONS_KEYWORD = "MMAX_EVALUATIONS";

  /** Keyword for population size*/
  public static final String POPULATION_SIZE_KEYWORD = "PPOPULATION_SIZE";

  /** Keyword for processors*/
  public static final String PROCESSORS_KEYWORD = "PPROCESSORS";

  /** Keyword for initial port number*/
  public static final String INITIAL_PORT_KEYWORD = "IINIT_PORT";

  /** Keyword for initial JVM*/
  public static final String JVM_KEYWORD = "JJVM";
  
  /** Keyword for interval */
  public static final String INTERVAL_KEYWORD = "INTERVAL";

  /** Integer REGEX*/
  public static  final String INTEGER_REGEX = "^\\d+$";

  /** Double REGEX*/
  public static final String DOUBLE_REGEX  = "[0-9]+(.[0-9]+)?";

  
  
  /** Algorithms currently supported by our implementation*/
  public static enum ALGORITHM{
	    NSGAII,
	    SPEA2,
	    MOCELL,
	    RANDOM
	    ;
	}

}