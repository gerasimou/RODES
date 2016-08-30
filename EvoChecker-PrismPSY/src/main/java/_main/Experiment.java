package _main;

import java.util.Arrays;

import evochecker.EvoChecker;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;

public class Experiment {

	public Experiment() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {

			String tolerance = Utility.getProperty("TOLERANCES").replaceAll("\\s+","");
		
			String tolerances[] = tolerance.split(",");
			String fileNamesFUN[]  = new String[tolerances.length]; 
			String fileNamesVAR[]  = new String[tolerances.length]; 
			
			System.out.println(Arrays.toString(tolerances));
			
			int i=0;
			for (String t : tolerances){
					Utility.setProperty("TOLERANCE", t);
					System.out.println(Utility.getProperty("TOLERANCE"));
					EvoChecker.main(null);
					fileNamesFUN[i] = "FUN_REGION_"+t.replace(".", "");
					fileNamesVAR[i++] = "VAR_REGION_"+t.replace(".", "");
			}
			
			
			createRegionsCombinedFile(fileNamesFUN, "FUN_REGION_all");
			createRegionsCombinedFile(fileNamesVAR, "VAR_REGION_all");
		} catch (EvoCheckerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	public static void createRegionsCombinedFile(String[] fileNames, String endFile){
		String dataPath = "data/";
		StringBuilder str = new StringBuilder();
		
		for (int i=fileNames.length-1; i>=0; i--){
			String fileName = fileNames[i];			
			str.append(Utility.readFile(dataPath + fileName));
			str.append("\n\n");
		}
		Utility.exportToFile(dataPath + endFile, str.toString(), false);
	}

}
