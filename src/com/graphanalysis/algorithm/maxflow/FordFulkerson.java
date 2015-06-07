package com.graphanalysis.algorithm.maxflow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import com.graphanalysis.algorithm.dijkstra.DijkstraGraph;
import com.graphanalysis.graphbase.commondefine.GraphReader;
import com.graphanalysis.graphbase.implement.Edge;

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

public class FordFulkerson {

	double[][] graph = null;
	double[][] rGraph = null;
	ArrayList<Pair> flow = new ArrayList<Pair>();

	private boolean bfs(double[][] G, int src, int dst, int[] parent) {
		boolean[] visited = new boolean[G.length];
		Queue<Integer> q = new ArrayDeque<Integer>();
		q.add(src);
		visited[src] = true;
		parent[src] = -1;

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

	public FlowResult exec(String fileName, int src, int dst) {
		// TODO Auto-generated method stub
		Vector<Edge> edges = GraphReader.readFromFile(fileName, 1);
		// System.out.println(edges.size());
		DijkstraGraph g = new DijkstraGraph(edges);
		Set<Integer> vSet = new HashSet<Integer>();
		int maxId = -1000;
		for (int i = 0; i < edges.size(); i++) {
			vSet.add(edges.get(i).getFromID());
			vSet.add(edges.get(i).getToID());
			if (edges.get(i).getFromID() > maxId) {
				maxId = edges.get(i).getFromID();
			}
			if (edges.get(i).getToID() > maxId) {
				maxId = edges.get(i).getToID();
			}
		}
		graph = new double[maxId + 1][maxId + 1];
		rGraph = new double[maxId + 1][maxId + 1];

		for (int i = 0; i < edges.size(); i++) {
			int from = edges.get(i).getFromID();
			int to = edges.get(i).getToID();
			double dist = edges.get(i).getWeight();
			graph[from][to] = dist;
			rGraph[from][to] = dist;
		}

		int u, v;

		int[] parent = new int[graph.length];

		int maxFLow = 0;
		while (bfs(rGraph, src, dst, parent)) {

			double pathFlow = 9999999;
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
				}
			}

			for (v = dst; v != src; v = parent[v]) {
				u = parent[v];
				rGraph[u][v] -= pathFlow;
				rGraph[v][u] += pathFlow;
			}

			maxFLow += pathFlow;
		}
		return new FlowResult(flow,maxFLow);
	}
}
