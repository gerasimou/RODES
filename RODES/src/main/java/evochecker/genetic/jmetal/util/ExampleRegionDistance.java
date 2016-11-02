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
package evochecker.genetic.jmetal.util;

import jmetal.core.SolutionSet;
import jmetal.util.comparators.ObjectiveComparator;


/**
 * 
 * @author sgerasimou
 *
 */
public class ExampleRegionDistance extends RegionDistance {

	public ExampleRegionDistance() {
		super();
	}

	
	/**
	 * Assigns crowding distances to all solutions in a <code>SolutionSet</code>
	 * @param solutionSet
	 *            The <code>SolutionSet</code>.
	 * @param nObjs
	 *            Number of objectives.
	 */
	@Override
	public void crowdingDistanceAssignment(SolutionSet solutionSet, int nObjs) {
		int size = solutionSet.size();

		if (size == 0)
			return;

		if (size == 1) {
			solutionSet.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
			return;
		} // if

		if (size == 2) {
			solutionSet.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
			solutionSet.get(1).setCrowdingDistance(Double.POSITIVE_INFINITY);
			return;
		} // if

		// Use a new SolutionSet to evite alter original solutionSet
		SolutionSet front = new SolutionSet(size);
		for (int i = 0; i < size; i++) {
			front.add(solutionSet.get(i));
		}

		for (int i = 0; i < size; i++)
			front.get(i).setCrowdingDistance(0.0);

		double objetiveMaxn;
		double objetiveMinn;
		double distance;

		for (int i = 0; i < nObjs; i++) {
			// Sort the population by Obj n
			front.sort(new ObjectiveComparator(i));
			objetiveMinn = front.get(0).getObjective(i);
			objetiveMaxn = front.get(front.size() - 1).getObjective(i);

			// Set de crowding distance
			front.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
			front.get(size - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);

			for (int j = 1; j < size - 1; j++) {
				distance = front.get(j + 1).getObjective(i) - front.get(j - 1).getObjective(i);
				distance = distance / (objetiveMaxn - objetiveMinn);
				distance += front.get(j).getCrowdingDistance();
				front.get(j).setCrowdingDistance(distance);
			} // for
		} // for
	} // crowdingDistanceAssing

}
