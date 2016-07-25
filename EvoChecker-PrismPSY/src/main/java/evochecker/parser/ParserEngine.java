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

package evochecker.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import evochecker.auxiliary.Utility;
import evochecker.exception.EvoCheckerException;
import evochecker.genetic.GenotypeFactory;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;
import evochecker.parser.evolvable.Evolvable;
import evochecker.parser.evolvable.EvolvableDistribution;
import evochecker.parser.evolvable.EvolvableDouble;
import evochecker.parser.evolvable.EvolvableInteger;
import evochecker.parser.evolvable.EvolvableModule;
import evochecker.parser.evolvable.EvolvableModuleAlternative;
import evochecker.parser.handler.PrismListener;
import evochecker.parser.handler.PrismVisitor;
import evochecker.parser.src.gen.PrismLexer;
import evochecker.parser.src.gen.PrismParser;

/**
 * Class representing an evolvable template parser
 * @author sgerasimou
 *
 */
public class ParserEngine implements InstantiatorInterface {
	
	/** String that keeps the model template */
	protected String internalModelRepresentation;

	/** properties filename */
	private String propertiesFilename;

	/** list of evolvable elements*/
	private List<Evolvable> evolvableList;
	
	/** map that keeps pairs of genes and evolvable elements*/
	protected Map<AbstractGene, Evolvable> elementsMap;
	
	
	List<AbstractGene> genesList;

	
	/**
	 * Class constructor: create a new parser engine
	 * @param fileName
	 * @param propertiesFilename
	 * @throws EvoCheckerException 
	 */
	public ParserEngine(String fileName, String propertiesFilename) throws EvoCheckerException {
		String modelString = Utility.readFile(fileName);

		this.propertiesFilename = propertiesFilename;
		elementsMap = new LinkedHashMap<AbstractGene, Evolvable>();

		runVisitor(modelString);
		
		if (evolvableList.isEmpty()){
			throw new EvoCheckerException("Now evolvable element found!");
		}	
	}

	
	/**
	 * Print the evolvable elements
	 */
	public void print() {
		for (Evolvable evolvable : evolvableList) {
			if (evolvable instanceof EvolvableDouble) {
				// modelString =
				// modelString.replaceFirst(evolvable.getIdentifier(), "2.5");
			} 
			else if (evolvable instanceof EvolvableInteger) {
				// modelString =
				// modelString.replaceFirst(evolvable.getIdentifier(), "2");
			} 
			else if (evolvable instanceof EvolvableDistribution) {
				// System.out.println(evolvable.toString());
			} 
			else if (evolvable instanceof EvolvableModule) {
				// System.out.println(evolvable.toString());
			} 
			else if (evolvable instanceof EvolvableModuleAlternative) {
				System.out.println(evolvable.toString());
			}

			// System.out.println(evolvable.toString());
		}

		System.out.println(internalModelRepresentation + evolvableList.size());
		//
	}

	
	/** run the listener*/
	@SuppressWarnings("unused")
	private void runListener(String inputString) {
		// create a CharStream that reads from standard input
		ANTLRInputStream input = new ANTLRInputStream(inputString);
		// create a lexer that feeds off of input CharStream
		PrismLexer lexer = new PrismLexer(input);
		// create a buffer of tokens pulled from the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// create a parser that feeds off the tokens buffer
		PrismParser parser = new PrismParser(tokens);
		// begin parsing at model rule
		ParseTree tree = parser.model();

		// Create a generic parse tree walker that can trigger callbacks
		ParseTreeWalker walker = new ParseTreeWalker();
		// Create a listener
		PrismListener listener = new PrismListener(parser);
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(listener, tree);

		listener.printMemory();
	}

	
	/**
	 * parse model template, generate an abstract syntax tree
	 * and generate a list with evolvavle elements
	 * @param inputString
	 */
	private void runVisitor(String inputString) {
		// create a CharStream that reads from standard input
		ANTLRInputStream input = new ANTLRInputStream(inputString);
		// create a lexer that feeds off of input CharStream
		PrismLexer lexer = new PrismLexer(input);
		// create a buffer of tokens pulled from the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// create a parser that feeds off the tokens buffer
		PrismParser parser = new PrismParser(tokens);
		// begin parsing at prog rule
		ParseTree tree = parser.model();
		// Create the visitor
		PrismVisitor visitor = new PrismVisitor();
		// and visit the nodes
		visitor.visit(tree);

		//generate list with evolvable elements
		List<Evolvable> evolvableList = visitor.getEvolvableList();
		setEvolvableList(evolvableList);

		//get internal model representation
		String modelString = visitor.getInternalModelRepresentation();
		setInternalModelRepresentation(modelString);
	}


	/**
	 * Set the list of evolvable elements
	 * @param evolvableList
	 */
	private void setEvolvableList(List<Evolvable> evolvableList) {
		this.evolvableList = evolvableList;
	}


	/** 
	 * Get the list of evolvable elements
	 * @return
	 */
	public List<Evolvable> getEvolvableList() {
		return this.evolvableList;
	}

	
	/**
	 * Set the internal model representation as string
	 * @param modelString
	 */
	private void setInternalModelRepresentation(String modelString) {
		this.internalModelRepresentation = modelString;
	}


	/**
	 * Return the internal model representation
	 * @return
	 */
	public String getInternalModelRepresentation() {
		return internalModelRepresentation;
	}

	
	/**
	 * Initialise the map of genes and evolvable elements pairs
	 */
	public void createMapping() {
		Map<AbstractGene, Evolvable> map = GenotypeFactory.getMapping();
		for (Map.Entry<AbstractGene, Evolvable> entry : map.entrySet()) {
			this.elementsMap.put(entry.getKey(), entry.getValue());
		}
	}

	
	/**
	 * Return a valid prism model instance
	 */
	@Override
	public String getValidModelInstance(List<AbstractGene> genes) {
		StringBuilder concreteModel = new StringBuilder(this.internalModelRepresentation);
		for (AbstractGene gene : genes) {
			if (gene instanceof IntegerGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			} 
			else if (gene instanceof DoubleGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			} 
			else if (gene instanceof DiscreteDistributionGene) {
				concreteModel.append(elementsMap.get(gene).getCommand((double[]) gene.getAllele()));
			} 
			else if (gene instanceof AlternativeModuleGene) {
				concreteModel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
			}
		}
		// System.err.println(concreteModel);
		return concreteModel.toString();
	}


	/**
	 * Return the name of properties file
	 */
	@Override
	public String getPrismPropertyFileName() {
		return this.propertiesFilename;
	}

	
	
//	@SuppressWarnings("unused")
//	private void generatedAntlrFiles(String inputFile) {
//		String[] arg0 = { "-visitor", inputFile, "-o",
//				"./src/org/spg/antlr/src/gen" };
//		// "-package", "org.spg.antlr.src.gen"};
//		org.antlr.v4.Tool.main(arg0);
//	}
	
	
	public ParserEngine (ParserEngine aParser) throws EvoCheckerException{
			ParserEngine parser = (ParserEngine)aParser;
			this.internalModelRepresentation	= parser.internalModelRepresentation;
			this.propertiesFilename				= parser.propertiesFilename;
			this.evolvableList					= new ArrayList<Evolvable>();
			for (Evolvable element : parser.evolvableList)
				if (element instanceof EvolvableInteger)
					this.evolvableList.add(new EvolvableInteger(element));
				else if (element instanceof EvolvableDouble)
					this.evolvableList.add(new EvolvableDouble(element));
			
			genesList = GenotypeFactory.createChromosome(evolvableList);
			
//			GenotypeFactory.createChromosome(evolvableList);
			this.elementsMap					= new LinkedHashMap<AbstractGene, Evolvable>();
			for (int i=0; i<genesList.size(); i++){
				this.elementsMap.put(genesList.get(i), evolvableList.get(i));
			}
	}
	
	
	
	public List<AbstractGene> getGeneList(){
		return (List<AbstractGene>)Arrays.asList(this.elementsMap.keySet().toArray(new AbstractGene[0]));				
	}
}
