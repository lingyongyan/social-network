/**
 * 
 */
package com.graphanalysis.algorithm.bipartiteMatching;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.algorithm.randomWalk.RandomWalkGraph;
import com.graphanalysis.graphbase.commondefine.GraphReader;
import com.graphanalysis.graphbase.commondefine.GraphType;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.interfaces.GraphInterface;

/**
 * @author xiaoxu
 *
 */
public class BipartiteMatching implements BipartiteMatchingInterface {

	private final GraphInterface graph;

	/**
	 * 
	 * @param graph
	 */
	public BipartiteMatching(GraphInterface graph) {
		if (graph == null) {
			throw new NullPointerException();
		}
		this.graph = graph;
	}

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
	public boolean matching(Map<Integer, Integer> map) {
		if (map == null) {
			throw new NullPointerException("Intial bipartite matching map");
		}
		double[][] adjMat = this.graph.getAdjMatrix();
		int size = adjMat.length;
		if (graph.getType() != GraphType.UNDirectedGraph) {
			return false;
		}
		if (IsBipartiteGraph.isBipartite(graph.getAdjMatrix()) == false) {
			return false;
		}
		HungaryAlgorithm hungary = new HungaryAlgorithm(size, size);
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++) {
				hungary.setWeight(i, j, Double.NEGATIVE_INFINITY);
			}
		Vector<Edge> edgeSet = graph.getEdgeSet();
		for (Edge e : edgeSet) {
			hungary.setWeight(e.getFromID(), e.getToID(), e.getWeight());
		}
		int[] result = hungary.getMatching();
		for (int i = 0; i < result.length / 2; i++) {
			if (result[i] != -1) {
				map.put(i, result[i]);
			}
		}
		return true;
	}

	@Override
	public int exec(String[] args) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JSONArray exec() {
		// TODO Auto-generated method stub
		JSONArray jsedges = new JSONArray();
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		if (this.matching(result) == true) {
			for (Map.Entry entry : result.entrySet()) {
				JSONObject jsedge = new JSONObject();
				try {
					jsedge.put("source", entry.getKey()).put("target",
							entry.getValue());
					jsedges.put(jsedge);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return jsedges;
	}

}
