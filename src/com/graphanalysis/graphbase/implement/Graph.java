package com.graphanalysis.graphbase.implement;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.graphBase.commondefine.GraphType;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Node;
import com.graphanalysis.graphbase.interfaces.GraphInterface;
import com.graphanalysis.web.json.JsonDeal;

public class Graph implements GraphInterface{
	protected GraphType type =GraphType. UNDirectedGraph;
	protected double [][] adjMatrix;//邻接矩阵
	protected HashMap<Integer,Vector<Integer>>adjList = new HashMap<Integer,Vector<Integer>>();
	protected Set<Node> nodes = new HashSet<Node>();
	protected Vector<Edge> edges = new Vector<Edge>();;
	
	public Graph(){
	}
	
	public Graph(double[][] adjMatrix) throws Exception{
		int rows = adjMatrix.length;
		int columns = 0;
		if(rows == 0)
			throw new Exception("size shouldn't be zero!");
		for(int i=0;i<rows;i++){
			columns = adjMatrix[i].length;
			this.nodes.add(new Node(i));
			Vector<Integer> mapTemp = new Vector<Integer>();
			for(int j=0;j<columns;j++){
				if(adjMatrix[i][j]!=0){
					mapTemp.add(j);
					this.edges.add(new Edge(i,j));
				}
			}
			this.adjList.put(i,mapTemp);
		}
		constructAdjList(this.edges);
		constructAdjMatrixFromAdjList();
	}
	
	public Graph(Vector<Edge> edges) {
		setEdges(edges);
		constructNodeSetFromEdges();
		constructAdjList(this.edges);
		constructAdjMatrixFromAdjList();
	}
	
	protected void constructAdjList(Vector<Edge> edges){
		int size = edges.size();
		for(int i=0;i<size;i++){
			Edge edge = edges.get(i);
			int fromID = edge.getFromID();
			if(this.adjList.containsKey(fromID)){
				Vector<Integer> hasin = this.adjList.get(fromID);
				hasin.add(edge.getToID());
			}else{
				Vector<Integer> newedge = new Vector<Integer>();
				newedge.add(edge.getToID());
				this.adjList.put(fromID, newedge);
			}
		}
	}
	
	protected int constructAdjMatrixFromAdjList(){
		if(this.adjList.size()<=0)
			return -1;
		int size = getBiggestNode()+1;
		Iterator it =  this.adjList.entrySet().iterator();
		this.adjMatrix = new double[size][];
	    for(int i=0;i<size;i++){
	    	this.adjMatrix[i] = new double[size];
	    	for(int j=0;j<size;j++){
	    		this.adjMatrix[i][j] = 0;
	    	}
	    }
	    while (it.hasNext()) {    
	    	Map.Entry entry =   (Entry) it.next();    
	        Integer key = (Integer) entry.getKey();    
	        Vector<Integer> value = (Vector<Integer>) entry.getValue();
	       for(int i=0;i<value.size();i++){
	    	   this.adjMatrix[key][value.get(i)] = 1;
	       }
	    }
	    this.type = autoCheckType();
		return 0;
	}
	
	protected void constructNodeSetFromEdges(){
		if(this.nodes.size()!=0)
			return;
		for(int i=0;i<this.edges.size();i++){
			int from =this.edges.get(i).getFromID();
			int to = this.edges.get(i).getToID();
			if(!this.nodes.contains(from)){
				Node node = new Node(from);
				this.nodes.add(node);
			}
			if(!this.nodes.contains(to)){
				Node node = new Node(to);
				this.nodes.add( node);
			}
		}
	}
	
	private int getBiggestNode(){
		int max = -1;
		Iterator<Edge> it = this.edges.iterator();
		while(it.hasNext()){
			 Edge edge=  it.next();
			 
			if(edge.getFromID()>max){
				max=edge.getFromID();
			}
			if(edge.getToID()>max){
				max=edge.getToID();
			}
		}
		return max;
	}
	
	public GraphType autoCheckType(){
		if(this.adjMatrix.length<=0)
			return GraphType.UNKnown;
		int size = this.adjMatrix.length;
		for(int i=0;i<size;i++){
			if(this.adjMatrix[i].length!=size)
				return GraphType.UNKnown;
			for(int j=0;j<=i;j++){
				if(i==j && this.adjMatrix[i][j]==1){
					return GraphType.selfCircle;
				}
				if(this.adjMatrix[i][j]!=this.adjMatrix[j][i]){
					return GraphType.DirectedGraph;
				}
			}
		}
		return GraphType.UNDirectedGraph;
	}
	
	public void setType(GraphType t ){
		this.type = t;
	}
	
	protected void setNodes(Set<Node> nodes){
		this.nodes = nodes;
	}
	
	protected void setEdges(Vector<Edge> edges){
		this.edges = edges;
	}
	
	public void printGraph(){
		int rows = adjMatrix.length;
		int columns = 0;
		for(int i=0;i<rows;i++){
			columns = adjMatrix[i].length;
			for(int j=0;j<columns;j++){
				System.out.print("\t"+this.adjMatrix[i][j]);
			}
			System.out.print("\n");
		}
	}
	
	public GraphType getType(){
		return this.type;
	}
	
	@Override
	public double[][] getAdjMatrix() {
		// TODO 自动生成的方法存根
		return this.adjMatrix;
	}

	public Vector<Integer> getAdjList(int node){//获取某个节点邻接的全部节点
		Vector<Integer> res = null;
		if(node>=this.adjList.size())
			return res;
		res = this.adjList.get(node);
		return res;
	}
	
	@Override
	public Set<Node> getNodeSet() {
		// TODO 自动生成的方法存根
		return this.nodes;
	}
	@Override
	public Vector<Edge> getEdgeSet() {
		// TODO 自动生成的方法存根
		return this.edges;
	}
	
	public int getNodeNum(){
		return this.adjMatrix.length;
	}

	@Override
	public void writeToJson(String fileName) throws JSONException {
		// TODO 自动生成的方法存根
		String gtype = (this.type==GraphType. UNDirectedGraph)? "false":"true";
		String json = "{'type':'"+gtype+"'}";
		JSONObject jsonObj = new JSONObject(json);
		jsonObj.put("weight", "true");
		jsonObj.put("N", this.adjMatrix.length);
		jsonObj.put("E", this.edges.size());
		Vector<String> nodesJs = new Vector <String>();
		Iterator<Node> nodeite = nodes.iterator();
		int i=0;
		while(nodeite.hasNext() && i<100){
			Node now = nodeite.next();
			Map <String, String> nodesJs2 = new HashMap <String, String>();
			nodesJs2.put("name", now.getName());
			nodesJs2.put("id", Integer.toString(now.getID()));
			nodesJs.add(nodesJs2.toString());
			i++;
		}
		jsonObj.put("nodes", nodesJs.toString());
		
		Vector<String> edgesJs = new Vector <String>();
		Iterator<Edge> edgesite = edges.iterator();
		i=0;
		while(nodeite.hasNext() && i<100){
			Edge now = edgesite.next();
			Map <String, String> edgesJs2 = new HashMap <String, String>();
			edgesJs2.put("source",Integer.toString( now.getFromID()));
			edgesJs2.put("target", Integer.toString(now.getToID()));
			edgesJs2.put("weight", Double.toString(now.getWeight()));
			edgesJs.add(edgesJs2.toString());
			i++;
		}
		jsonObj.put("edges", edgesJs.toString());
		try {
			JsonDeal.writeFile(fileName,jsonObj.toString() );
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//jsonObj.put(key, value)
	}
}
