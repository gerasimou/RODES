package evochecker.prism;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import prism.Prism;
import prism.PrismLangException;

public class PropertyFactory {

	private static String propertiesFilename	= Utility.getProperty(Constants.PROPERTIES_FILE_KEYWORD);
	
	private final static String OBJECTIVE 	= "OBJECTIVE";
	private final static String CONSTRAINT 	= "CONSTRAINT";
	private final static String MAX 			= "MAX";
	private final static String MIN		 	= "MIN";
	
	
	public static List<List<Property>> getObjectivesConstraints(String internalModel){
		List<Property> objectivesList = new ArrayList<Property>();		
		List<Property> constaintsList = new ArrayList<Property>();		
		
		try {
			File propsStrFile 	= new File(propertiesFilename);
			
			Prism prism = new Prism(null);
			
			ModulesFile modelFile = prism.parseModelString(internalModel);// (modelStrFile);
			
			PropertiesFile propsFile = prism.parsePropertiesFile(modelFile, propsStrFile);
			int numProps = propsFile.getNumProperties();
			for (int i=0; i<numProps; i++) {
				parser.ast.Property prop = propsFile.getPropertyObject(i);

//				System.out.println(prop);
				String comment = prop.getComment();
				if (comment != null) {
					String[] commentElements = comment.trim().split(",");
					
					if (commentElements[0].trim().toUpperCase().equals(OBJECTIVE))
						objectivesList.add(createObjective(commentElements, prop.toString()));					
					else if (commentElements[0].trim().toUpperCase().equals(CONSTRAINT))
						constaintsList.add(createConstraint(commentElements, prop.toString()));					
				}
			}
			
			List<List<Property>> list= new ArrayList<>();
			list.add(objectivesList);
			list.add(constaintsList);
			return list;
		} 
		catch (FileNotFoundException | PrismLangException | EvoCheckerException e) {
			e.printStackTrace();
		}	
		//never happens
		return null;
	}
 
	
	private static Objective createObjective(String[] objElements, String prop) throws EvoCheckerException {
		
		if (objElements.length != 2)
			throw new EvoCheckerException("2 elements are required for specifying an objective (Objective, MAX|MIN), " 
										   + objElements.length +" provided!");
		
		if (objElements[1].trim().toUpperCase().equals(MAX)) 
				return new Objective(true, prop);
		else if (objElements[1].trim().toUpperCase().equals(MIN))
			return new Objective(false, prop);
		else
			throw new EvoCheckerException("MAX|MIN not specified for objective: " + prop);			
	}
	
	
	private static Constraint createConstraint(String[] constraintElements, String prop) throws EvoCheckerException {		
		if (constraintElements.length != 3)
			throw new EvoCheckerException("3 elements are required for specifying a constraint (Constraint, MAX|MIN, Limit), " 
					   + "Provided: " + Arrays.toString(constraintElements));
		
		String maxMin 	= constraintElements[1].trim().toUpperCase();
		String limit 	= constraintElements[2].trim().toUpperCase();
		
		if (maxMin.equals(MAX) && isDouble(limit) )
				return new Constraint(true, Double.parseDouble(limit), prop);
		else  if (maxMin.equals(MIN) && isDouble(limit) )
				return new Constraint(false, Double.parseDouble(limit), prop);
		else
			throw new EvoCheckerException("Incorrect specification "+ Arrays.toString(constraintElements) +" for constraints: " + prop);
			
	}
	
	
	
	private static boolean isDouble (final String str) {
		try {
			Double.valueOf(str);
			return true;
		}
		catch (NumberFormatException e) {
			return false;
		}
	}
}
