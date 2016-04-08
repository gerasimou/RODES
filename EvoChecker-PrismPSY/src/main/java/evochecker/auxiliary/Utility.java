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

package evochecker.auxiliary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.RegionGene;
import evochecker.genetic.jmetal.encoding.ArrayInt;
import evochecker.genetic.jmetal.encoding.ArrayReal;
import evochecker.genetic.jmetal.encoding.solution.RegionSolution;
import evochecker.prism.Property;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.Configuration;
import jmetal.util.JMException;

/**
 * Utility class with helper functions
 * @author sgerasimou
 *
 */
public class Utility {
	
	private static String fileName = "resources/config.properties";
	private static Properties properties;
	
	private static void loadPropertiesInstance(){
		try {
			if (properties == null){
				properties = new Properties();
				properties.load(new FileInputStream(fileName));
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void setProperty (String key, String value){
		loadPropertiesInstance();
		properties.setProperty(key, value);
	}

	
	public static String getProperty (String key){
		loadPropertiesInstance();
		String result = properties.getProperty(key); 
		if (result == null)
			  throw new IllegalArgumentException(key.toUpperCase() + " name not found!");
		return result;		
	}
	
	public static String getProperty (String key, String defaultValue){
		loadPropertiesInstance();
		String output = properties.getProperty(key);
		return (output != null ? output : defaultValue);
	}
	
	
	public static void exportToFile(String fileName, String output, boolean append){
		try {
			FileWriter writer = new FileWriter(fileName, append);
			writer.append(output +"\n");
			writer.flush();
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void createFileAndExport(String inputFileName, String outputFileName, String outputStr){
		FileChannel inputChannel 	= null;
		FileChannel outputChannel	= null;
				
		try {
			File input 	= new File(inputFileName);
			File output 	= new File(outputFileName);
			
			inputChannel 	= new FileInputStream(input).getChannel();
			outputChannel	= new FileOutputStream(output).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

			inputChannel.close();
			outputChannel.close();
			
			exportToFile(outputFileName, outputStr, false);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("resource")
	public static String readFile(String fileName) {
		StringBuilder model = new StringBuilder(100);
		BufferedReader bfr = null;

		try {
			bfr = new BufferedReader(new FileReader(new File(fileName)));
			String line = null;
			while ((line = bfr.readLine()) != null) {
				model.append(line + "\n");
			}
			model.delete(model.length() - 1, model.length());
			return model.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static void exportToFile(List<String> outputList, String fileName){
		try {
			FileWriter writer = new FileWriter(fileName);
			for (String str : outputList){	
				writer.append(str +"\n");
			}
				writer.flush();
				writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Writes the decision encodings.variable values of the
	 * <code>Solution</code> solutions objects into the set in a file.
	 * 
	 * @param path
	 *            The output file name
	 */
	static public void printVariablesToFile(String path, Solution solution, boolean append) {
		try {
			FileOutputStream fos = new FileOutputStream(path, append);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			int numberOfVariables = solution.getDecisionVariables().length;
			for (int j = 0; j < numberOfVariables; j++)
				bw.write(solution.getDecisionVariables()[j].toString() + " ");
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}
	} // printVariablesToFile



	
	public static void printVariableRegionsToFile(String path, SolutionSet population, boolean append, Double[] radius) throws JMException{
		try {
			FileOutputStream fos = new FileOutputStream(path, append);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			Iterator<Solution> it = population.iterator();
			
			while (it.hasNext()){
				RegionSolution regionSolution = (RegionSolution) it.next();
			
				ArrayReal arrayRealVariable = (ArrayReal)regionSolution.getDecisionVariables()[0];
				for (int i=0; i<arrayRealVariable.getLength(); i++){
					double value = arrayRealVariable.getValue(i);
					bw.write(Math.max(value-radius[i], arrayRealVariable.getLowerBound(i)) 
							+":"+
							Math.min(value+radius[i], arrayRealVariable.getUpperBound(i))
							+","
							);				
				}
					
				ArrayInt  arrayIntVariable  = (ArrayInt)regionSolution.getDecisionVariables()[1];
				bw.write(arrayIntVariable.toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}
	}
	
	
	
	public static void printObjectiveRegionsToFile(String path, SolutionSet population, boolean append, 
													List<Property> propertyList) throws JMException{
		try {
			FileOutputStream fos = new FileOutputStream(path, append);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			Iterator<Solution> it = population.iterator();
			
			while (it.hasNext()){
				RegionSolution regionSolution = (RegionSolution) it.next();
				
				int numOfObjectives = regionSolution.getNumberOfObjectives();
				
				for (int i=0; i<numOfObjectives; i++){
					Double[] bounds = regionSolution.getObjectiveBounds(i);
					
					//check if maximisation & negate
					if (propertyList.get(i).isMaximization()){
						bw.write(-bounds[1] +":"+ -bounds[0] +",");
					}
					else{
						bw.write(bounds[0] +":"+ bounds[1] +",");
					}
				}
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}
	}
	
	
	
	public static Double[] getRadiusAsArray(List<AbstractGene> genes ){
		List<Double> radiusList = new ArrayList<Double>();
		for (AbstractGene gene : genes){
			if (gene instanceof RegionGene){
				radiusList.add(((RegionGene) gene).getRegionRadius());
			}
		}
		Double[] radius = new Double[radiusList.size()];
		return radiusList.toArray(radius);
	}
}
