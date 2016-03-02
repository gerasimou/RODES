package evochecker.genetic.genes;

import java.util.ArrayList;
import java.util.List;

public class RegionGene extends AbstractGene {

	public RegionGene(String name, Number minValue, Number maxValue) {
		super(name, minValue, maxValue);
	}

	
	/** Get the region represented by this region gene 
	 * Region is simply an array [min,max]
	 */
	 public double[] getRegion(){
		return new double[]{(double) this.getMinValue(), (double) this.getMaxValue()};
	}
	 
	
	 /** 
	  * Decompose this region into <b>subRegionsNum</b> regions
	  * @param subRegionsNum
	  * @return
	  */
	 public List<RegionGene> decompose(int subRegionsNum){
		 List<RegionGene> regionGenesList = new ArrayList<RegionGene>();
		 //TODO: decomposing logic, maybe uniform for start?
		 return regionGenesList;
	 }
}
