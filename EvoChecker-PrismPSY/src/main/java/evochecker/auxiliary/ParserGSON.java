package evochecker.auxiliary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ParserGSON {
	private static Gson gson;
	
	static{
		gson = new GsonBuilder()
				.disableHtmlEscaping()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setPrettyPrinting()
				.serializeNulls()
				.create();
	}
	
	
	public static synchronized List<String> parseGSON(String GSONstr) throws NullPointerException{
		List<String> resultList = new ArrayList<String>();
		
		//convert string to JSON object
		JsonObject JSONobject =  gson.fromJson(GSONstr, JsonObject.class);
		
		//parse for each property
		for (Entry<String, JsonElement> entry :  JSONobject.entrySet()){
			JsonArray propertiesJSON = (JsonArray)entry.getValue();
			JsonObject JSONsubregion = (JsonObject) propertiesJSON.iterator().next();
			//find min and max
			double min 				 = JSONsubregion.get("min").getAsDouble();
			double max 				 = JSONsubregion.get("max").getAsDouble();
//			System.out.println(entry.getKey() +"\t["+ min +", "+ max +"]");	
			//append to results list
			resultList.add(min +"");
			resultList.add(max +"");
//			resultList.add((min+max)/2 +"");
		}
		
		return resultList;
		//print
//		printJSON(JSONobject);
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
