package evochecker.auxiliary;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Singleton class that keeps information to be shown on the UI
 * @author sgerasimou
 *
 */
public class KnowledgeSingleton{

	/** Knowledge singleton instance*/
	private static KnowledgeSingleton instance = null;
	
	/** Map*/
	private static Map<String, Object> knowledge = new ConcurrentHashMap<String, Object>();

	/** Message queue*/
//    private final BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(20);
	private final AbstractQueue<String> messageQueue = new ConcurrentLinkedQueue <String>();
	
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
	
	
	public void addMessage (Object value) {
		try {
			messageQueue.add(value.toString());
		} 
		catch (Exception e) {
            System.out.println("Message producer interrupted: exiting.");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	public void put (String key, Object value) {
		knowledge.put(key, value);
	}
	
	
	public Object get (String key) {
		return knowledge.get(key);
		
	}
	
	public String getMessage(){
		String str = "";

		if (messageQueue.size()==1) 
			str = String.join("", messageQueue.poll(), "\n");
		else {
			str = messageQueue.stream().collect(Collectors.joining("\n"));
			messageQueue.clear();
		}
		
		//for BlockingQueue
//		List<String> list = new ArrayList<String>();
//		messageQueue.drainTo(list);
//		
//		if (list.size()==1) 
//			str = String.join("", list.get(0), "\n");
//		else if (list.size()>1)
//			str = list.stream().collect(Collectors.joining("\n"));
////					map(Object::toString).collect(Collectors.joining("\n,"));// forEach(sb::append);
		return str;
	}
}
