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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import evochecker.exception.EvoCheckerException;
import evochecker.genetic.genes.AbstractGene;
import evochecker.genetic.genes.AlternativeModuleGene;
import evochecker.genetic.genes.DiscreteDistributionGene;
import evochecker.genetic.genes.IntegerGene;
import evochecker.genetic.genes.RegionGene;

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
	
		
	/**
	 * Class constructor
	 * @param fileName
	 * @param propertiesFilename
	 * @throws EvoCheckerException 
	 */
	public ParserEnginePrismPSY(String fileName, String propertiesFilename) throws EvoCheckerException {
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
					prismPSYmodel.append(elementsMap.get(gene).
														getCommand(
																gene.getAllele()));
				} 
				else if (gene instanceof AlternativeModuleGene) {
					prismPSYmodel.append(elementsMap.get(gene).getCommand(gene.getAllele()));
				}
				else if (gene instanceof RegionGene) {
					prismPSYmodel.append(elementsMap.get(gene).
														getCommand(
																gene.getAllele()));
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
			if (gene instanceof RegionGene) {
				double regionRadius = ((RegionGene)gene).getRegionRadius();
				
				double min 	  = (double)gene.getAllele()-(double)gene.getAllele()*regionRadius/2;
				double max 	  = (double)gene.getAllele()+(double)gene.getAllele()*regionRadius/2;	
				if (min < (double)gene.getMinValue()){
					min = (double)gene.getMinValue();
					max = (double)gene.getMinValue()+(double)gene.getAllele()*regionRadius;
				}
				else if (max > (double)gene.getMaxValue()){
					min = (double)gene.getMaxValue()-(double)gene.getAllele()*regionRadius;
					max = (double)gene.getMaxValue();
				}
				
				try {
					paramsWithRanges.append(gene.getName() 		+"=");
					paramsWithRanges.append(new BigDecimal(min).setScale(5, RoundingMode.HALF_DOWN).doubleValue() 
											+":"+ 
											new BigDecimal(max).setScale(5, RoundingMode.HALF_DOWN).doubleValue() 
											+",");
				}
				catch (NumberFormatException e){
					e.printStackTrace();
				}
				
			} 
			else if (gene instanceof DiscreteDistributionGene) {
				throw new EvoCheckerException("DiscreteDistributionGene not supported yet!");
//				prismPSYmodel.append(elementsMap.get(gene).getCommand((double[]) gene.getAllele()));
			} 
		}
		paramsWithRanges.deleteCharAt(paramsWithRanges.length()-1); //remove last ','
		System.out.println("\nParams/Ranges:\t"+paramsWithRanges.toString());
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
	
	
	
	public ParserEnginePrismPSY(ParserEnginePrismPSY aParser) throws EvoCheckerException {
		super(aParser);
		if (aParser instanceof ParserEnginePrismPSY){			
			this.accuracy 			= ((ParserEnginePrismPSY)aParser).accuracy;
			this.decompositionType	= ((ParserEnginePrismPSY)aParser).decompositionType;
			this.paramsWithRanges	= new StringBuilder(aParser.paramsWithRanges);//  ((ParserEnginePrismPSY)aParser). );
		}
	}
}
