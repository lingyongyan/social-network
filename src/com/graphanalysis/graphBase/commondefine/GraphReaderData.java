package com.graphanalysis.graphBase.commondefine;

import java.util.HashMap;
import java.util.Vector;

import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Node;

public class GraphReaderData {
	private Vector<Edge> edges = new Vector<Edge>();
	private Vector<Node> nodes = new Vector<Node>();
	private HashMap<Integer,Node> nodeMap = new HashMap<Integer,Node>();
	private HashMap<Integer,Integer> idMap = new HashMap<Integer,Integer>();
	private boolean type = false;
	private int id = 0;
	public GraphReaderData(){
	}
	
	public Vector<Edge> getEdges(){
		return edges;
	}
	public void addEdge(Edge e){
		edges.add(e);
	}
	public void setType(boolean newType){
		type = newType;
	}
	public boolean type(){
		return type;
	}
	
	public void addNode(int nodeID){
		if(!idMap.containsKey(nodeID)){
			idMap.put(nodeID, id);
			nodeMap.put(id, new Node(id,String.valueOf(nodeID)));
			id++;
			nodes.add(getNode(nodeID));
		}
	}
	
	public Node getNode(int nodeID){
		if(idMap.containsKey(nodeID)){
			return nodeMap.get(idMap.get(nodeID));
		}else{
			return null;
		}
	}
	
	public Vector<Node> getNodeSet(){
		return nodes;
	}
}
