/**
 * 
 */
package com.graphanalysis.algorithm.bipartiteMatching;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.algorithm.implement.ExecParameter;
import com.graphanalysis.algorithm.implement.ExecReturn;
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
	public BipartiteMatching(Graph graph) {
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
		IsBipartiteGraph isBiGraph = new IsBipartiteGraph(graph.getAdjMatrix().length);
		if (isBiGraph.isBipartite(graph.getAdjMatrix()) == false) {
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
			if(graph.getType() == GraphType.UNDirectedGraph)
				hungary.setWeight(e.getToID(), e.getFromID(), e.getWeight());
		}
		int[] result = hungary.getMatching();
		
/*		for (int i = 0; i < result.length / 2; i++) {
			if (result[i] != -1) {
				map.put(i, result[i]);
			}
			上述代码有误，建议改成一下形式（Yan）
		}*/
		for (int i = 0; i < result.length; i++) {
			if (result[i] != -1 && isBiGraph.colored[i] == IsBipartiteGraph.RED) {
				map.put(i, result[i]);
			}
	}
		return true;
	}

	@Override
	public JSONArray exec() {
		// TODO Auto-generated method stub
		JSONArray jsedges = new JSONArray();
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		if (this.matching(result) == true) {
			for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
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

	/**
	 * 实现顶层接口的方法
	 * **/
	@Override
	public ExecReturn exec(ExecParameter args) {
		// TODO 自动生成的方法存根
		if(args.size()!=0)
			return null;
		JSONArray jArray = this.exec();
		ExecReturn res = new ExecReturn();
		res.addResult(jArray);
		return res;
	}

}
