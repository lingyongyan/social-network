package com.graphanalysis.algorithm.maxflow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.algorithm.implement.ExecParameter;
import com.graphanalysis.algorithm.implement.ExecReturn;
import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.implement.Graph;

class Pair {
	public Pair(int src, int dst, double flow) {
		super();
		this.src = src;
		this.dst = dst;
		this.flow = flow;
	}

	private int src;
	private int dst;
	private double flow;

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getDst() {
		return dst;
	}

	public void setDst(int dst) {
		this.dst = dst;
	}

	public double getFlow() {
		return flow;
	}

	public void setFlow(double flow) {
		this.flow = flow;
	}

}

class FlowResult {
	private ArrayList<Pair> flow;
	private double maxFlow;

	public ArrayList<Pair> getFlow() {
		return flow;
	}

	public void setFlow(ArrayList<Pair> flow) {
		this.flow = flow;
	}

	public double getMaxFlow() {
		return maxFlow;
	}

	public void setMaxFlow(double maxFlow) {
		this.maxFlow = maxFlow;
	}

	public FlowResult(ArrayList<Pair> flow, double maxFlow) {
		super();
		this.flow = flow;
		this.maxFlow = maxFlow;
	}
}

public class FordFulkerson implements AlgorithmInterface{

	private double[][] graph = null;
	private double[][] rGraph = null;
	ArrayList<Pair> flow = new ArrayList<Pair>();
	
	public FordFulkerson(){
	}

	private boolean bfs(double[][] G, int src, int dst, int[] parent) {
		boolean[] visited = new boolean[G.length];
		Queue<Integer> q = new ArrayDeque<Integer>();
		q.add(src);
		visited[src] = true;
		parent[src] = -1;
		for(int i=0;i<parent.length;i++)
			parent[i] = -1;
		while (q.size() != 0) {
			int u = q.peek();
			q.poll();
			for (int v = 0; v < G.length; v++) {
				if (visited[v] == false && rGraph[u][v] > 0) {
					q.add(v);
					parent[v] = u;
					visited[v] = true;
				}
			}
		}

		return (visited[dst] == true);
	}

	public JSONArray exec(Graph tGraph, int src, int dst) {
		JSONArray result = new JSONArray();
		// TODO Auto-generated method stub
		int len = tGraph.getAdjMatrix().length;
		graph = new double[len][len];
		rGraph = new double[len][len];
		for (int i = 0; i < len; i++) {
			for(int j=0;j<len;j++){
			graph[i][j] = tGraph.getAdjMatrix()[i][j];
			rGraph[i][j] = tGraph.getAdjMatrix()[i][j];
			}
		}

		int u, v;
		int[] parent = new int[graph.length];
		int maxFLow = 0;
		while (bfs(rGraph, src, dst, parent)) {

			double pathFlow = Double.MAX_VALUE;
			ArrayList<Integer> from = new ArrayList<Integer>();
			ArrayList<Integer> to = new ArrayList<Integer>();
			for (v = dst; v != src; v = parent[v]) {
				u = parent[v];
				from.add(u);
				to.add(v);
				pathFlow = Math.min(pathFlow, rGraph[u][v]);
			}

			for (int i = 0; i < from.size(); i++) {
				int f = from.get(i);
				int t = to.get(i);
				boolean isFind = false;
				for (int j = 0; j < flow.size(); j++) {
					if (flow.get(j).getSrc() == f && flow.get(j).getDst() == t) {
						isFind = true;
						double tmpFlow = flow.get(j).getFlow();
						flow.get(j).setFlow(pathFlow + tmpFlow);
					}
				}
				if (isFind == false) {
					flow.add(new Pair(f, t, pathFlow));
					JSONObject tmpJson = new JSONObject();
					try {
						tmpJson.putOpt("source", f);
						tmpJson.putOpt("target", t);
						tmpJson.putOpt("weight", pathFlow);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					result.put(tmpJson);
				}
			}

			for (v = dst; v != src; v = parent[v]) {
				u = parent[v];
				rGraph[u][v] -= pathFlow;
				rGraph[v][u] += pathFlow;
			}

			maxFLow += pathFlow;
		}
		return result;
	}

	@Override
	public ExecReturn exec(ExecParameter args) {
		// TODO 自动生成的方法存根
		if(args.size()!=3 || args.get(0).getClass()!=Graph.class || args.get(1).getClass()!=Integer.class || args.get(2).getClass()!=Integer.class)
			return null;
		Graph myGraph =  ((Graph) args.get(0));
		int startNode = (int) args.get(1);
		int endNode = (int) args.get(2);
		JSONArray jArray = this.exec(myGraph,startNode,endNode);
		ExecReturn res = new ExecReturn();
		res.addResult(jArray);
		return res;
	}
}
