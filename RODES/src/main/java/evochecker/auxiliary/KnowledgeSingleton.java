package evochecker.auxiliary;

import java.io.File;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import evochecker.exception.EvoCheckerException;
import evochecker.genetic.jmetal.encoding.solution.RegionSolution;
import evochecker.prism.Property;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;

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
	
	/** Keeps solutions per generation*/
	private List<SolutionSet> generationsRepository = new ArrayList<SolutionSet>();
	
	
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
	
	
	public void processGeneration (SolutionSet solutionSet) {
		int populationSize = solutionSet.size();

		SolutionSet generation = new SolutionSet(populationSize);
	
		//create copy solution list
		for (int i=0; i<populationSize; i++) {
			Solution solution = solutionSet.get(i);
			if (solution instanceof RegionSolution) {
				Solution copySolution = new RegionSolution(((RegionSolution)solution));
				generation.add(copySolution);
			}
		}
		
		//add solution list to generation repository 
		generationsRepository.add(generation);
	}
	
	
	public void processGeneration (SolutionSet population, int generation) {
		String outputDir 		= knowledge.get(Constants.OUTPUT_DIR_KEYWORD).toString();
		String outputFileSuffix = knowledge.get(Constants.OUTPUT_FILE_SUFFIX).toString();
		String outputFile 		= outputFileSuffix +"_"+ generation;
		
		try {
			//get properties
			Object obj = knowledge.get(Constants.OBJECTIVES_KEYWORD);
			List<Property>  propertyList = null;
			if (! (obj instanceof List<?>))
				throw new EvoCheckerException(obj + " is not a List<Property>");
			propertyList = (List<Property>)obj;

			//		Utility.printVariableRegionsToFile( outputDir + File.separatorChar + "VAR_REGION_" +  outputFile, generation, false, regionsRadii);
			Utility.printObjectiveRegionsToFile(outputDir + File.separatorChar + "FUN_REGION_" +  outputFile, population, false, propertyList);
			Utility.printVariableRegionsToFile(outputDir + File.separatorChar + "VAR_REGION_" + outputFile, population, true);	
		} 
		catch (JMException | EvoCheckerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	public List<SolutionSet> getGenerationsRepository() {
		return generationsRepository;
	}
}
