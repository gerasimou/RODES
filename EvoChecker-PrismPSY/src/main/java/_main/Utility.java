package _main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;

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
			File outputFile = new File(fileName);
			if (!outputFile.exists()){
				System.out.println(outputFile.getAbsolutePath());
				outputFile.createNewFile();
			}
			
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
	
	
	public static String readFile (String fileName){
		StringBuilder model = new StringBuilder(100);
		BufferedReader bfr = null;
		
		try {
			bfr = new BufferedReader(new FileReader(new File(fileName)));
			String line = null;
			while ( (line = bfr.readLine()) != null){
				model.append(line +"\n");
			}
			model.delete(model.length()-1, model.length());
			return model.toString();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
