package rodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import evochecker.auxiliary.Constants;
import evochecker.auxiliary.ParserGSON;
import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;

public class BoundEvaluator {

	private int portNum = 8880;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	private String propertiesFile 	= "models/Google/google.csl";
	private String modelFile 		= "models/Google/googleNoEvolvables.sm";
	
	private String solutionsDir		= "/Users/sgerasimou/Documents/Git/RODES/Experiments_JSS/data/allData/10cores/Google_WCD/NSGAII/";
	private String solutionsFile		= "VAR_REGION_001_005_0";
	private String frontFile			= "FUN_REGION_001_005_0";
	
	private String experiments[]		= new String[]{	"001_000_0", "001_005_0",  "001_010_0",
													"002_000_0", "002_005_0",  "002_010_0",
													"005_000_0", "005_005_0",  "005_010_0"
												  };
	
	
	
	public static void main(String[] args) {
//		BoundEvaluator evaluator = new BoundEvaluator();
//		evaluator.startExecutor();
		
		String text = Utility.readFile("data/boundEvaluator-GFS.txt");
		String t[] = text.split("[0-9]{3,4}][[1-9]{1,3}.");
		for (String a : t) {
			System.out.println(a);
		}
	}
	
	
	
	/** 
	 * Initialise executors
	 */
	private void startExecutor() {
		try {									
			String params[] 	= new String[5];
			params[0] = Utility.getProperty(Constants.JVM_KEYWORD);
			params[1] = "-jar";
			params[2] = Utility.getProperty("EXECUTOR_PATH", "repo/PRISM-PSY-fat-1.0.2.jar");
					
			params[3] = String.valueOf(portNum);
			params[4] = String.valueOf(1);

			System.out.println("Starting server at port " + portNum);
			boolean isAlive = false;
			do {
				Process p = Runtime.getRuntime().exec(params);
				Thread.sleep(1000);
				isAlive = p.isAlive();
			} 
			while (!isAlive);
			System.out.println("Connecting");
			
			makeConnection(params);
			
			evaluateSolutions();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** 
	 * Try to make a connection until finding an open port
	 * @param portNum
	 * @param i
	 * @param params
	 */
	private void makeConnection(String[] params){
		try{
			socket 	= new Socket("127.0.0.1", portNum);
			in 		= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out 		= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
		} catch (IOException e) {
			try {
				Thread.sleep(2000);
				Process p = Runtime.getRuntime().exec(params);
			} catch (InterruptedException | IOException e1) {
				e1.printStackTrace();
			}			
			makeConnection(params);
		}
	}

	
	private void evaluateSolutions() {
		StringBuilder results 	= new StringBuilder();

		for (String experiment : experiments) {
			String solutionFile	= solutionsDir + "VAR_REGION_" + experiment;
			String frontFile 	= solutionsDir + "FUN_REGION_" + experiment;
		
			try {
				String modelString	= Utility.readFile(modelFile);
				String solutions[]	= Utility.readFile(solutionFile).split(System.lineSeparator());
				String front[]		= Utility.readFile(frontFile).split(System.lineSeparator());
				
				StringBuilder model 		= new StringBuilder();
				
				for (int i=0; i<solutions.length; i++) {
					model.setLength(0);
					String solution = solutions[i];
					String variables[] = solution.split(",");
					
					model.append(modelString +"\n");
					model.append("const int S_ = " + variables[2] +";");
					
					String paramsWithRanges 	= "c_hw_repair_rate=" + variables[0] +","+
											  "c_hw_fail_rate="   + variables[1];
					String decompositionType	= "psecheck";
					String accuracy			= "1";
	
					String outputStr 			= model.toString() +"\n";
					outputStr += "@"+ propertiesFile +"\n@"+ decompositionType +"\n@"+ 
								paramsWithRanges +"\n@"+ accuracy + "\nEND"; 
	
					List<String> resultsList 	= invokePrism(in, out, outputStr);
					System.out.println(Arrays.toString(variables) +"\t"+ resultsList +"\t["+ front[i]+"]");
					results.append(Arrays.toString(variables) +","+ resultsList +","+ front[i]);
				}
			} 
			catch (Exception e) {
					e.printStackTrace();
			}
		}
		Utility.exportToFile("data/boundEvaluator-GFS.txt", results.toString(), false);
	}
	
	
	
	/**
	 * Prism invocation method
	 * @param model
	 * @param propertyFile
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws EvoCheckerException 
	 */
	private List<String> invokePrism(BufferedReader in, PrintWriter out, String outputStr) throws IOException, EvoCheckerException {
		//send to server
		out.println(outputStr);
		out.flush();
		//read from server
		String response = in.readLine();
//		System.out.println(response);
		if (response==null || response.isEmpty())
			throw new EvoCheckerException("Error: invalid response from server:\t" + response);
		List<String> resultList = ParserGSON.parseGSON(response);
		return resultList;
	}

}
