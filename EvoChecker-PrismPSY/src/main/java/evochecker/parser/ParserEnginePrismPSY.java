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

import java.util.List;
import java.util.Random;

import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.DoubleGene;
import evochecker.genetic.genes.IntegerGene;

/**
 * Class representing an evolvable template parser
 * for PRISM-PSY
 * @author sgerasimou
 *
 */
public class ParserEnginePrismPSY extends ParserEngine implements InstantiatorInterfacePrismPSY{
	/** PrismPSY parameters with ranges: <param_name>=min:max, <param_name>=min:max,... 
	 * c_fail=0.01:0.1,c_hw_repair_rate=0.5:0.6*/
	private StringBuilder paramsWithRanges;
	
	/** Decomposition type*/
	private String decompositionType;
	
	/** PrismPSY accuracy*/
	private String accuracy;
	
	
	Random rand = new Random(System.currentTimeMillis());
	
	/**
	 * Class constructor
	 * @param fileName
	 * @param propertiesFilename
	 */
	public ParserEnginePrismPSY(String fileName, String propertiesFilename) {
		super(fileName, propertiesFilename);
		this.paramsWithRanges 	= new StringBuilder(50);
		this.decompositionType	= null;
		this.accuracy			= "1000";
	}

	
	/**
	 * Return a model instance conforming to
	 * Prism-PSY semantics
	 * @param individual
	 * @return
	 */
	@Override
	public String getValidModelInstance(List<AbstractGene> genes) {
		StringBuilder prismPSYmodel = new StringBuilder(this.internalModelRepresentation);
		try{
			for (AbstractGene gene : genes) {
				if (gene instanceof IntegerGene) {
					prismPSYmodel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
				} 
				else if (gene instanceof AlternativeModuleGene) {
					prismPSYmodel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
				}
				else if (gene instanceof DoubleGene) {
					prismPSYmodel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
				}
			}
			//before ending prepare String of parameters with min and max
			prepareParamWithRanges(genes);
		}
		catch (EvoCheckerException e){
			e.toString();
			e.printStackTrace();
			System.exit(-1);
		}
		return prismPSYmodel.toString();
	}
	
	
	
	private void prepareParamWithRanges(List<AbstractGene> genes) throws EvoCheckerException{
		paramsWithRanges.setLength(0);
		
		for (AbstractGene gene : genes){
			if (gene instanceof DoubleGene) {
//				paramsWithRanges.append("");
				paramsWithRanges.append(gene.getName() 		+"=");
				if (gene.getName().equals("c_hw_repair_rate")){
					//dummy values
					double paramMarginError05 = 0.0159;
					double num1 = Math.max((double)gene.getAllele()-paramMarginError05, (double)gene.getMinValue()); 
					double num2 = Math.min((double)gene.getAllele()+paramMarginError05, (double)gene.getMaxValue()); 
					paramsWithRanges.append(num1 +":"+ num2 +",");
				}
				else if (gene.getName().equals("c_fail")){
					//dummy values
					double paramMarginError05 = 0.0027;
					double num1 = Math.max((double)gene.getAllele()-paramMarginError05, (double)gene.getMinValue()); 
					double num2 = Math.min((double)gene.getAllele()+paramMarginError05, (double)gene.getMaxValue()); 
					paramsWithRanges.append(num1 +":"+ num2 +",");				
				}				
			} 
			else if (gene instanceof DiscreteDistributionGene) {
				throw new EvoCheckerException("DiscreteDistributionGene not supported yet!");
//				prismPSYmodel.append(elementsMap.get(gene).getCommand((double[]) gene.getAllele()));
			} 
		}
		paramsWithRanges.deleteCharAt(paramsWithRanges.length()-1); //remove last ','
		System.out.println("\n"+paramsWithRanges.toString());
	}


	/**
	 * Get parameters with ranges as a string
	 *  <param_name>=min:max, <param_name>=min:max,... 
	 * @return
	 */
	public String getParamsWithRanges(){
		return this.paramsWithRanges.toString();
	}

	
	/**
	 * Get decomposition type
	 * @return
	 */
	public String getDecompositionType(){
		return this.decompositionType;
	}
	
	
	/** Get accuracy*/
	public String getAccuracy(){
		return this.accuracy;
	}
	
}
