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

			String tolerance = Utility.getProperty("TOLERANCES");
		
			String tolerances[] = tolerance.split(",");
			String fileNames[]  = new String[tolerances.length]; 
			
			System.out.println(Arrays.toString(tolerances));
			
			int i=0;
			for (String t : tolerances){
					Utility.setProperty("TOLERANCE", t);
					System.out.println(Utility.getProperty("TOLERANCE"));
					EvoChecker.main(null);
					fileNames[i++] = t;
			}
			
			
			createRegionsCombinedFile(fileNames, "FUN_all");
		} catch (EvoCheckerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	
	public static void createRegionsCombinedFile(String[] fileNames, String endFile){
		String dataPath = "data/";
		StringBuilder str = new StringBuilder();
		for (String fileName : fileNames){
				str.append(Utility.readFile(dataPath + fileName));
				str.append("\n\n");
		}
		Utility.exportToFile(dataPath + endFile, str.toString(), false);
	}

}
