package com.graphanalysis.algorithm.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;

class Pair {
	private int dst;
	private double dist;

	public Pair(int dst, double dist) {
		super();
		this.dst = dst;
		this.dist = dist;
	}

	public int getDst() {
		return dst;
	}

	public void setDst(int dst) {
		this.dst = dst;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

}

public class DijkstraGraph extends Graph {

	private HashMap<Integer, ArrayList<Pair>> weiAdjList;

	public HashMap<Integer, ArrayList<Pair>> getWeiAdjList() {
		return weiAdjList;
	}

	public DijkstraGraph() {

	}

	public DijkstraGraph(Vector<Edge> edges) {
		super(edges);
		weiAdjList = new HashMap<Integer, ArrayList<Pair>>();
		for (int i = 0; i < edges.size(); i++) {
			int from = edges.get(i).getFromID();
			if (weiAdjList.get(from) == null) {
				ArrayList<Pair> pair = new ArrayList<Pair>();
				pair.add(new Pair(edges.get(i).getToID(), edges.get(i)
						.getWeight()));
				weiAdjList.put(from, pair);
			} else {
				weiAdjList.get(from).add(
						new Pair(edges.get(i).getToID(), edges.get(i)
								.getWeight()));
			}

		}

	}
}
