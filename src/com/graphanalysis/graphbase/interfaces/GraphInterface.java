package com.graphanalysis.graphbase.interfaces;

import java.util.Set;
import java.util.Vector;

import org.json.JSONException;

import com.graphanalysis.graphBase.commondefine.GraphType;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Node;

public interface GraphInterface {
	public GraphType getType();
	public double[][] getAdjMatrix();
	public Vector<Integer>  getAdjList(int nodeID);
	public Set<Node> getNodeSet();
	public Vector<Edge> getEdgeSet();
	public void writeToJson(String fileName) throws JSONException;
}
