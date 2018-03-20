package rodes;

import java.util.Arrays;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.KnowledgeSingleton;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;

public class RODESExperimentRuns {

	private static int run=1;
	
	public static void main(String[] args) {		
		try {

			String tolerance		= Utility.getProperty(Constants.TOLERANCES_KEYWORD).replaceAll("\\s+","");
			String epsilon		= Utility.getProperty(Constants.EPSILONS_KEYWORD).replaceAll("\\s+","");
			String algorithm 	= Utility.getProperty(Constants.ALGORITHM_KEYWORD).toUpperCase();
			final int	RUNS		= Integer.parseInt(Utility.getProperty(Constants.RUNS_KEYWORD).trim());

			String tolerances[]		= tolerance.split(",");
			String epsilons[]		= epsilon.split(",");
			String fileNamesFUN[]  	= new String[epsilons.length * tolerances.length]; 
			String fileNamesVAR[]  	= new String[epsilons.length * tolerances.length]; 
			
			System.out.println("Tolerances:" + Arrays.toString(tolerances));
			System.out.println("Leniencies:" + Arrays.toString(epsilons));
			System.out.println("Algorithm:" + algorithm);
			
			
			for (; run<=RUNS; run++){
				System.out.println("Run: " + run);
				int i=0;
				for (String t : tolerances){
					for (String l : epsilons){
						Thread.sleep(1000);
						Utility.setProperty(Constants.TOLERANCE_KEYWORD, t);
						Utility.setProperty(Constants.EPSILON_KEYWORD,  l);
						System.out.println("Tolerance: "  + Utility.getProperty(Constants.TOLERANCE_KEYWORD) + 
										   "\tEpsilon: " + Utility.getProperty(Constants.EPSILON_KEYWORD));
						RODES.main(null);
						fileNamesFUN[i] = "FUN_REGION_"+t.replace(".", "")+"_"+l.replace(".", "")+"_"+run;
						fileNamesVAR[i++] = "VAR_REGION_"+t.replace(".", "")+"_"+l.replace(".", "")+"_"+run;
					}
				}
				createOutputFile(fileNamesFUN, "FUN_REGION_all");
				createOutputFile(fileNamesVAR, "VAR_REGION_all");
			}
			
			System.exit(0);			
		} catch (EvoCheckerException | InterruptedException e) {
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
	
	
	
	public static void createOutputFile(String[] fileNames, String endFile){		
		String outputDir 		= KnowledgeSingleton.getInstance().get(Constants.OUTPUT_DIR_KEYWORD).toString();

		StringBuilder str = new StringBuilder();
		
		for (int i=0; i<fileNames.length; i++){
			String fileName = fileNames[i];			
			str.append(Utility.readFile(outputDir + fileName));
			str.append("\n\n");
		}
		Utility.exportToFile(outputDir + endFile, str.toString(), false);
	}
	
	
	
	public static int getRun(){
		return run;
	}
	
	

}
