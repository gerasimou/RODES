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

package evochecker.genetic.jmetal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
//import org.apache.commons.lang.NotImplementedException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.parser.InstantiatorInterface;
import evochecker.prism.Property;
import jmetal.core.Solution;
import jmetal.util.JMException;

/**
 * Class representing a genetic problem to be solved through 
 * using search-based techniques
 * @author sgerasimou
 *
 */
public class GeneticProblem extends GeneticModelProblem {
	
	/**
	 * Class constructor: create a new Genetic Problem instance
	 * @param genes
	 * @param properties
	 * @param instantiator
	 * @param numOfConstraints
	 */
	public GeneticProblem(List<AbstractGene> genes, InstantiatorInterface instantiator,
						  List<Property> objectivesList, List<Property> constraintsList, String problemName){
		super(genes, instantiator, objectivesList, constraintsList, problemName);
	}


	/** 
	 * Evaluate 
	 * @param solution
	 * @param out
	 * @param in
	 * @throws JMException
	 * @throws EvoCheckerException 
	 */
	@Override
	public void parallelEvaluate(BufferedReader in, PrintWriter out, Solution solution) throws JMException, EvoCheckerException {
		//Populate genes
		this.populateGenesWithRealSolution(solution);
		this.populateGenesWithIntSolution(solution);

		// Prepare parameters
		String model 		= instantiator.getValidModelInstance(this.genes);
		String propertyFile = instantiator.getPrismPropertyFileName();
//		Utility.exportToFile("model.txt", model);
		
		List<String> resultsList;
		try {
			String outputStr = model + "@" + propertyFile + "\nEND\n";
			resultsList 	 = this.invokePrism(in, out, outputStr);

			for (int i = 0; i < this.numberOfObjectives_; i++) {
				Property p = objectivesList.get(i);
				double result;
				if (p.isMaximization()) {
					result = new BigDecimal(- Double.parseDouble(resultsList.get(i))).setScale(3, RoundingMode.HALF_DOWN).doubleValue();
				}
				else{
					result = new BigDecimal(Double.parseDouble(resultsList.get(i))).setScale(3, RoundingMode.HALF_UP).doubleValue();
				}
				solution.setObjective(i, result);
				System.out.print("FITNESS: "+ result +"\t");
			}
			
			this.evaluateConstraints(solution, resultsList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
	}


	/**
	 * Prism invocation method
	 * @param model
	 * @param propertyFile
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 */
	protected List<String> invokePrism(BufferedReader in, PrintWriter out, String output) throws IOException {
//		System.out.println("Sending to PRISM: "+propertyFile);
//		System.out.println("Sending to PRISM: "+model);
		out.print(output);
		out.flush();

		String line;
		StringBuilder modelBuilder = new StringBuilder();
		do {
//			System.out.println("Waiting PRISM");
			line = in.readLine();
			if (line.endsWith("END"))
				break;
			modelBuilder.append(line);
			modelBuilder.append("\n");
		} while (true);

		String res[] = modelBuilder.toString().trim().split("@");
//		System.out.println("Received from PRISM: "+ modelBuilder.toString());
		return Arrays.asList(res);
	}
	
	
	public GeneticProblem (GeneticProblem aProblem) throws EvoCheckerException{		
		super((GeneticModelProblem)aProblem);
	}
}
