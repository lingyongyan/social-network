package com.graphanalysis.algorithm.maxflow;

import java.util.Vector;

import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;

public class FordFulkersonGraph extends Graph {

	private Vector<Edge> edges;

	public Vector<Edge> getEdges() {
		return edges;
	}

	public void setEdges(Vector<Edge> edges) {
		this.edges = edges;
	}

	public FordFulkersonGraph() {

	}

	public FordFulkersonGraph(Vector<Edge> edges) {
		super(edges);
		this.edges = edges;

	}
}
