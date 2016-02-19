package _main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import evochecker.auxiliary.Utility;

public class MainX {
	
	private static Gson gson = new GsonBuilder()
            					.disableHtmlEscaping()
            					.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            					.setPrettyPrinting()
            					.serializeNulls()
            					.create();
	
	private static int portNum = 8860;
	
	private static Process p;
	

	public static void main(String[] args){
		startPRISMPSYserver();
		clientAPI();
	}
	
	
	/**
	 * Start server
	 */
	private static void startPRISMPSYserver(){
		try {
			String params[] = new String[4];
			params[0] = "/System/Library/Frameworks/JavaVM.framework/Versions/Current/Commands/java";//Utility.getProperty("JVM");
			params[1] = "-jar";
			params[2] = "repo/PRISM-PSY-fat.jar";
			params[3] =  String.valueOf(portNum);
			System.out.println("Starting PRISM-PSY server @ " + params[3]);
			p = Runtime.getRuntime().exec(params);
			System.out.println(p.isAlive());
			Thread.sleep(3000);
		} 
		catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	
	/** Run Prism-PSY API  as a server and send a string as reponse*/
	private static void clientAPI(){
		try{
			String serverAddress 			= "127.0.0.1";
			int serverPort       			= 8860;
			Socket socket;
			socket = new Socket(serverAddress, serverPort);
			BufferedReader inFromServer 	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter outToServer			= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
			//send to server
			for (int i=0; i<10; i++){
			StringBuilder outputString 		= new StringBuilder();
			outputString.append(Utility.readFile("models/Google/google-source.sm") + "\n@");	//model String
			outputString.append("models/Google/google.csl" +"\n@");								//properties filename
			outputString.append("-psecheck" +"\n@");											//decompositionType	
			outputString.append("c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6" +"\n@");				//params and ranges
			outputString.append("100 \nEND");													//accuracy
			outToServer.println(outputString.toString());
			outToServer.flush();
			//read from server
			String response = inFromServer.readLine();
//			System.out.println("Result:\t" + response);
			printJSON(gson.fromJson(response, JsonObject.class));
			
			Thread.sleep(2000);
			}
		} 
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Print response given as a JSON element
	 * @param JSONelement
	 */
	private static void printJSON(JsonObject JSONobject){
		//for each property
		for (Entry<String, JsonElement> entry :  JSONobject.entrySet()){
			System.out.println(entry.getKey() +"\t"+ entry.getValue());
			JsonArray propertiesJSON = (JsonArray)entry.getValue();
			//for each subregion of a property
			Iterator propertiesSubregion = propertiesJSON.iterator();
			while (propertiesSubregion.hasNext()){
				JsonObject JSONsubregion = (JsonObject) propertiesSubregion.next();
				System.out.println(JSONsubregion.get("min") +"\t");
				System.out.println(JSONsubregion.get("max") +"\t");				
			}
		}
	}
}
