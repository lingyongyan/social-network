/**
 * 
 */
package com.graphanalysis.algorithm.bipartiteMatching;

import java.util.Map;

import org.json.JSONArray;

import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.implement.Graph;

/**
 * @author xiaoxu
 *
 */
public interface BipartiteMatchingInterface extends AlgorithmInterface {
	/**
	 * <p>
	 * Max Bipartite Algorithm method for weighted graph or unweighed graph
	 * 
	 * @param map
	 *            Store the match result on format <source,target>
	 * @return boolean. if matching succeed, then return true;else return false,
	 *         and at this time, it may be not a bipartite graph.
	 *         </p>
	 */
	public boolean matching(Map<Integer, Integer> map);

	public JSONArray exec();
}
