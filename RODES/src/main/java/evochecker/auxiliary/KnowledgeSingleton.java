package evochecker.auxiliary;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class that keeps information to be shown on the UI
 * @author sgerasimou
 *
 */
public class KnowledgeSingleton{

	/** Knowledge singleton instance*/
	private static KnowledgeSingleton instance = null;
	
	/** Map*/
	private static Map<String, String> knowledge = new ConcurrentHashMap<String, String>();
	
	/** Disable instantiation */
	private KnowledgeSingleton() {}
	
	
	public static KnowledgeSingleton getInstance() {
		if (instance == null) {
			synchronized(KnowledgeSingleton.class){
				if (instance == null) 
					instance = new KnowledgeSingleton();
			}
		}
		return instance;
	}
	
	
	public void set (String key, String value) {
		knowledge.put(key, value);
	}
	
	
	public String get (String key) {
		return knowledge.get(key);
	}
}
