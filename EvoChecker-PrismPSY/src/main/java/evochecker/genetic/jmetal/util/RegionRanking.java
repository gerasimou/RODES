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

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jmetal.core.SolutionSet;

/**
 * Abstract class associated with a ranking utility
 * This class implements some facilities for ranking solutions.
 * Given a <code>SolutionSet</code> object, their solutions are ranked.
 * This process yields a set of subsets. The subsets are numbered starting 
 * from 0 (in NSGA-II, the numbering starts from 1); thus, subset 0 contains 
 * the non-dominated solutions, subset 1 contains the non-dominated solutions 
 * after removing those belonging to subset 0, and so on.
 * 
 * @author sgerasimou
 *
 */
public class RegionRanking{

	  /**The <code>SolutionSet</code> to rank*/
	  protected SolutionSet   solutionSet ;
	  
	  /**An array containing all the fronts found during the search*/
	  protected SolutionSet[] ranking  ;
	  
	  /**stores a <code>Comparator</code> for dominance checking*/
	  protected static Comparator dominanceComparator;// = new DominanceComparator();
	  

	  
	/**
	 * Class constructor: create a new region ranking
     * @param solutionSet The <code>SolutionSet</code> to be ranked.
	 */
	public RegionRanking(SolutionSet solutionSet, Comparator dominanceComparator) {
	    this.solutionSet 			= solutionSet ;
	    this.dominanceComparator	= dominanceComparator;
	    doRanking();
	}
	
	
	/**
	 * Function that ranks the solution set 
	 */
	protected void doRanking(){
	    // dominateMe[i] contains the number of solutions dominating i        
	    int [] dominateMe = new int[solutionSet.size()];

	    // iDominate[k] contains the list of solutions dominated by k
	    List<Integer> [] iDominate = new List[solutionSet.size()];

	    // front[i] contains the list of individuals belonging to the front i
	    List<Integer> [] front = new List[solutionSet.size()+1];
	        
	    // flagDominate is an auxiliar encodings.variable
	    int flagDominate;    

	    // Initialize the fronts 
	    for (int i = 0; i < front.length; i++)
	      front[i] = new LinkedList<Integer>();
	    
	    
	    //-> Fast non dominated sorting algorithm
	    // Contribution of Guillaume Jacquenot
	    for (int p = 0; p < solutionSet.size(); p++) {
	    // Initialize the list of individuals that i dominate and the number
	    // of individuals that dominate me
	      iDominate[p] = new LinkedList<Integer>();
	      dominateMe[p] = 0;
	    }
	    for (int p = 0; p < (solutionSet.size()-1); p++) {
	      // For all q individuals , calculate if p dominates q or vice versa
	      for (int q = p+1; q < solutionSet.size(); q++) {
	        flagDominate = 0;//ConstraintComparator (not used yet) constraint_.compare(solutionSet.get(p),solutionSet.get(q));
	        if (flagDominate == 0) {
	          flagDominate =dominanceComparator.compare(solutionSet.get(p),solutionSet.get(q));
	        }
	        if (flagDominate == -1)
	        {
	          iDominate[p].add(q);
	          dominateMe[q]++;
	        }
	        else if (flagDominate == 1)
	        {
	          iDominate[q].add(p);
	          dominateMe[p]++;
	        }
	      }
	      // If nobody dominates p, p belongs to the first front
	    }
	    for (int p = 0; p < solutionSet.size(); p++) {
	      if (dominateMe[p] == 0) {
	        front[0].add(p);
	        solutionSet.get(p).setRank(0);
	      }
	    }    
	    
	    //Obtain the rest of fronts
	    int i = 0;
	    Iterator<Integer> it1, it2 ; // Iterators
	    while (front[i].size()!= 0) {
	      i++;
	      it1 = front[i-1].iterator();
	      while (it1.hasNext()) {
	        it2 = iDominate[it1.next()].iterator();
	        while (it2.hasNext()) {
	          int index = it2.next();
	          dominateMe[index]--;
	          if (dominateMe[index]==0) {
	            front[i].add(index);
	            solutionSet.get(index).setRank(i);
	          }
	        }
	      }
	    }
	    //<-
	        
	    ranking = new SolutionSet[i];
	    //0,1,2,....,i-1 are front, then i fronts
	    for (int j = 0; j < i; j++) {
	      ranking[j] = new SolutionSet(front[j].size());
	      it1 = front[j].iterator();
	      while (it1.hasNext()) {
	                ranking[j].add(solutionSet.get(it1.next()));
	      }
	    }
	}

  
	/**
	 * Return a <code>SolutionSet</code> containing the solutions of a given rank. 
	 * @param rank the rank
	 * @return the <code>SolutionSet</code> for that rank.
	 */
	public SolutionSet getSubfront(int rank) {
		return this.ranking[rank];
	}//getSubfront 
	
}
