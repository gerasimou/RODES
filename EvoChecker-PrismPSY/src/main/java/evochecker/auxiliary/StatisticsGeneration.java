package evochecker.auxiliary;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;

import _main.EvoCheckerStudy;
import evochecker.genetic.jmetal.util.MetricsRegionUtil;
import evochecker.genetic.jmetal.util.NonDominatedRegionSolutionList;
import evochecker.genetic.jmetal.util.eToleranceWorstCaseDominanceComparator;
import evochecker.prism.Property;
import jmetal.util.JMException;

public class StatisticsGeneration {
//												  "/Users/sgerasimou/Documents/Git/search-based-model-synthesis/Experiments/FinalExperiments/Google/Constrained/Algorithms/RS/AllData/;
	private final static String directory 		= "/Users/sgerasimou/Documents/Git/search-based-model-synthesis/Experiments/FinalExperiments/Google/Constrained/Algorithms/RS/AllData/";
	private final static int    RUNS	 	 	= 30;
	private final static String TOLERANCES[]	= {"001", "002", "005"};
	private final static String LENIENCIES[]	= {"000", "005", "010"};
	private final static String ALGORITHMS[]	= {"GA", "RS"};

	
	public static void main(String[] args) {
		try {
			
			//Step 1: Generate reference fronts
//			generateReferenceFronts();
			
			//Step 2: Generate quality indicators using JMetal
//			generateQualityIndicators();
			
			//Step 3: Merge quality indicators for different algorithms
//			generateQualityIndicatorsForComparison();

			//Deprecated
//			prepareData();
//			generateDataForQualityIndicators();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static void generateReferenceFronts() throws Exception{
		final String baseDir 	= "/Users/sgerasimou/Documents/Git/search-based-model-synthesis/Experiments/FinalExperiments/";
	    final String delimiters = "\\:|\\,";

		MetricsRegionUtil metricsUtils = new MetricsRegionUtil();
	    
	    ArrayList<Property> propertyList = new ArrayList<Property>();
		propertyList.add(new Property(true));
		propertyList.add(new Property(true));


		final String problemDir	=	baseDir +"Google/Constrained/Algorithms/";
		for (String tolerance : TOLERANCES){			//for all tolerances
			for (String leniency : LENIENCIES){			//for all lenciences
			    NonDominatedRegionSolutionList solutionSet = new NonDominatedRegionSolutionList(new eToleranceWorstCaseDominanceComparator(true,Double.parseDouble(leniency)));
				for (String algorithm : ALGORITHMS){	//for all algorithms
					for (int run=1; run<=30; run++){	//for all runs
						String paretoFrontFile 	= problemDir + algorithm +"/AllData/FUN_REGION_"+ tolerance +"_"+ leniency +"_"+ run;
						String paretoSetFile 	= problemDir + algorithm +"/AllData/VAR_REGION_"+ tolerance +"_"+ leniency +"_"+ run;
						metricsUtils.readNonDominatedRegionSolutionSet(paretoFrontFile, paretoSetFile, solutionSet);
					}
				}
				System.out.println(solutionSet.size());
				
				//create dir for this tolerance-leniency combination
				File experimentDir = new File(baseDir + "Google/StatisticalAnalysis/" +tolerance+"_"+leniency +"/referenceFronts");
				FileUtils.deleteDirectory(experimentDir);
				experimentDir.mkdirs();
		
				String regionReferenceFront = experimentDir +"/GeneticProblemRegion.rf";
				Utility.printObjectiveRegionsToFile(regionReferenceFront, solutionSet, false, propertyList);
			
				//output normal front using worst case
				StringBuilder outputString = new StringBuilder();
				String referenceRegionData[] = Utility.readFile(regionReferenceFront).split("\n");
				for (String dataRow : referenceRegionData){
					String data[] = dataRow.split(delimiters);
					String obj1Worst = data[0];
					String obj2Worst = data[2];
					outputString.append(obj1Worst +" "+ obj2Worst +"\n");
				}
				outputString.setLength(outputString.length()-1);//remove empty line
				System.out.println(outputString.toString().split("\n").length);
				
				String referenceFront = experimentDir +"/GeneticProblem.rf";
				Utility.exportToFile(referenceFront, outputString.toString(), false);			}
		}	
	}
	
	
	
	private static void generateQualityIndicators() throws IOException{
		for (String tolerance : TOLERANCES){			//for all tolerances
			for (String leniency : LENIENCIES){			//for all lenciences
				String params[] = {tolerance+"_"+leniency};
				EvoCheckerStudy.main(params);
			}
		}
	}
	
	
	
	private static void generateQualityIndicatorsForComparison(){
		final String baseDir = "/Users/sgerasimou/Documents/Git/search-based-model-synthesis/Experiments/FinalExperiments/Google/StatisticalAnalysis";

		String INDICATORS[] = {"EPSILON", "HV", "IGD"}; 
		
		for (String tolerance : TOLERANCES){			//for all tolerances
			for (String leniency : LENIENCIES){			//for all lenciences
				String experimentDir = baseDir +"/"+ tolerance+"_"+leniency;
				String gaDir = experimentDir +"/data/NSGAII/GeneticProblem"; 
				String rsDir = experimentDir +"/data/RS/GeneticProblem"; 
				for (String indicator : INDICATORS){
					StringTokenizer gaIndicator = new StringTokenizer(Utility.readFile(gaDir +"/"+ indicator), "\n");
					StringTokenizer rsIndicator = new StringTokenizer(Utility.readFile(rsDir +"/"+ indicator), "\n");
					StringBuilder outputStr		= new StringBuilder();
					while (gaIndicator.hasMoreTokens()) {
						String gaValue = gaIndicator.nextToken();
						String rsValue = rsIndicator.nextToken();
						outputStr.append(gaValue +","+ rsValue +"\n");
					}
					Utility.exportToFile(experimentDir +"/"+ indicator+".csv", outputStr.toString(), false);
				}
			}
		}

	}
	
	
	
	
	@Deprecated
	private static void generateDataForQualityIndicators(){
		final String dir = "/Users/sgerasimou/Documents/Git/search-based-model-synthesis/Experiments/FinalExperiments/Google/Constrained/Algorithms/";
		File dataDir = new File(dir);
		
		String delimiters = "\\:|\\,";
		
		
		for (File algorithm : dataDir.listFiles()){
			if (algorithm.isDirectory() && !algorithm.isHidden()){
				File allDataDir = algorithm.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
//						System.out.println(dir.toString() +"\t"+ name);
						if (name.equals("AllData"))
							return true;
						return false;
					}
				})[0];
//				System.out.println(allDataDir);
				
				for (File experiment : allDataDir.listFiles()){
//				File experiment = new File (allDataDir+"/FUN_REGION_001_000_1");
					StringBuilder outputString = new StringBuilder();
					if (!experiment.isHidden() && !experiment.isDirectory() &&  !experiment.canExecute() 
							&& !experiment.getName().contains("VAR") && !experiment.getName().contains("all") && experiment.getName().contains("FUN_REGION")){
							System.out.println(experiment.toString());
							String experimentData[] = Utility.readFile(experiment.getAbsolutePath()).split("\n");
							for (String dataRow : experimentData){
								String data[] = dataRow.split(delimiters);
								String obj1Worst = data[0];
								String obj2Worst = data[2];
								outputString.append(obj1Worst +" "+ obj2Worst +"\n");
							}
							outputString.setLength(outputString.length()-1);//remove empty line
//							System.out.println(outputString.toString() +"\t"+ outputString.length());
							Utility.exportToFile(experiment.getAbsolutePath().replaceAll("FUN_REGION", "FUN_WORST_POINTS/FUN_POINT"), outputString.toString(), false);
					}//if
				}
			}
		}	
	}//method
	
	
	
	private static void prepareData(){
		String fileNamesFUN[]  	= new String[LENIENCIES.length * TOLERANCES.length]; 
		String fileNamesVAR[]  	= new String[LENIENCIES.length * TOLERANCES.length]; 
				
		
		for (int run=1; run<=RUNS; run++){
			int i=0;
			for (String t : TOLERANCES){
				for (String l : LENIENCIES){
					fileNamesFUN[i] = "FUN_REGION_"+t.replace(".", "")+"_"+l.replace(".", "")+"_"+run;
					fileNamesVAR[i++] = "VAR_REGION_"+t.replace(".", "")+"_"+l.replace(".", "")+"_"+run;
				}
			}
			createOutputFile(fileNamesFUN, "FUN_REGION_all_"+run);
			createOutputFile(fileNamesVAR, "VAR_REGION_all_"+run);
		}
	}
	
	
	
	public static void createOutputFile(String[] fileNames, String endFile){
		StringBuilder str = new StringBuilder();
		
		for (int i=0; i<fileNames.length; i++){
			String fileName = fileNames[i];			
			str.append(Utility.readFile(directory + fileName));
			str.append("\n\n");
		}
		Utility.exportToFile(directory + endFile, str.toString(), false);
	}
}
