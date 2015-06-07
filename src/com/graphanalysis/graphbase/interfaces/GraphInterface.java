package com.graphanalysis.graphbase.interfaces;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.graphbase.commondefine.GraphType;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Node;

public interface GraphInterface {
	public GraphType getType();
	public double[][] getAdjMatrix();
	public Vector<Integer>  getAdjList(int nodeID);
	public Vector<Node> getNodeSet();
	public Vector<Edge> getEdgeSet();
	public JSONObject packToJson() throws JSONException;
	public void writeToJson(String fileName) throws JSONException;
}
