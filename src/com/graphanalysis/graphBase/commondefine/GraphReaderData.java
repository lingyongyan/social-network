package com.graphanalysis.graphBase.commondefine;

import java.util.HashMap;
import java.util.Vector;

import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Node;

public class GraphReaderData {
	public Vector<Edge> edges = new Vector<Edge>();
	public Vector<Node> nodes = new Vector<Node>();
	HashMap<Integer,Node> nodeMap = new HashMap<Integer,Node>();
	public boolean type = false;
}
