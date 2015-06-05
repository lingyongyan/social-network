package com.graphanalysis.graphbase.implement;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.util.Pair;

import com.graphanalysis.graphBase.commondefine.GraphType;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Node;
import com.graphanalysis.graphbase.interfaces.GraphInterface;
import com.graphanalysis.web.json.JsonDeal;

/**
 * @author young
 *
 */
public class Graph implements GraphInterface{
	protected GraphType type =GraphType. UNDirectedGraph;//图类型
	protected double [][] adjMatrix;//邻接矩阵
	protected HashMap<Integer,Vector<Integer>>adjList = new HashMap<Integer,Vector<Integer>>();//邻接链表
	protected HashMap<Integer,Vector<Edge>> adjEdgeList = new HashMap<Integer,Vector<Edge>>();//邻接链表
	protected Set<Node> nodes = new HashSet<Node>();//点集
	protected Vector<Edge> edges = new Vector<Edge>();//边集
	
	public Graph(){
	}
	
	/**
	 * @param adjMatrix
	 * @throws Exception
	 * 根据已有的邻接矩阵建立图
	 */
	public Graph(double[][] adjMatrix) throws Exception{
		int rows = adjMatrix.length;
		int columns = 0;
		if(rows == 0)
			throw new Exception("size shouldn't be zero!");
		for(int i=0;i<rows;i++){
			columns = adjMatrix[i].length;
			this.nodes.add(new Node(i));
			//Vector<Integer> mapTemp = new Vector<Integer>();
			for(int j=0;j<columns;j++){
				if(adjMatrix[i][j]!=0){
					//mapTemp.add(j);
					this.edges.add(new Edge(i,j));
				}
			}
			//this.adjList.put(i,mapTemp);
		}
		constructAdjList(this.edges);
		constructNodeSetFromEdges();
		//constructAdjMatrixFromAdjList();
	}
	
	/**
	 * @param edges
	 * 根据已有的边集构建图
	 */
	public Graph(Vector<Edge> edges) {
		this(edges,false);
	}
	
	public Graph(Vector<Edge> edges,Boolean t) {
		if(t)
			this.type = GraphType. DirectedGraph;
		else
			this.type = GraphType.UNDirectedGraph;
		setEdges(edges);
		constructAdjList(this.edges);
		constructAdjMatrixFromAdjList();
		constructNodeSetFromEdges();
	}
	
	protected void constructList(int fromID, int toID,Edge edge){
		if(this.adjList.containsKey(fromID)){//如果邻接链表中已有该节点项则在该项后追加新的链接节点ID
			Vector<Integer> alreadyin = this.adjList.get(fromID);
			alreadyin.add(toID);
			Vector<Edge> alEdge = this.adjEdgeList.get(fromID);
			alEdge.add(edge);
		}else{
			Vector<Integer> newitem = new Vector<Integer>();
			newitem.add(toID);
			this.adjList.put(fromID, newitem);
			Vector<Edge> edgeItem = new Vector<Edge>();
			edgeItem.add(edge);
			this.adjEdgeList.put(fromID, edgeItem);
		}
	}
	
	/**
	 * @param edges
	 * 根据边集构建邻接链表
	 */
	protected void constructAdjList(Vector<Edge> edges){
		int size = edges.size();
		for(int i=0;i<size;i++){
			Edge edge = edges.get(i);
			int fromID = edge.getFromID();
			int toID = edge.getToID();
			constructList(fromID, toID,edge);
			if(this.type == GraphType.UNDirectedGraph){
				Edge edgeT = new Edge(toID,fromID,edge.getWeight());
				constructList(toID,fromID,edge);
			}
		}
	}
	
	/**
	 * 根据邻接链表构建邻接矩阵
	 */
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
	
	/**
	 * 根据边集构建点集,必须在adjList创建之后进行
	 */
	protected void constructNodeSetFromEdges(){
		if(this.nodes.size()!=0)
			return;
		if(this.adjMatrix.length==0)
			constructAdjMatrixFromAdjList();
		double[][] mt = transpose(this.adjMatrix);
		Set<Integer> nodeIn = new HashSet<Integer>();
		for(int i=0;i<this.edges.size();i++){
			int from =this.edges.get(i).getFromID();
			int to = this.edges.get(i).getToID();
			if(!nodeIn.contains(from)){
				Node node = new Node(from);
				int in = 0;
				int out = 0;
				for(int pos=0;pos<this.adjMatrix.length;pos++){
					if(this.adjMatrix[from][pos]>0)
						out++;
					if(mt[from][pos]>0)
						in++;
				}
				node.setOutDegree(out);
				node.setInDegree(in);
				node.setName(String.valueOf(node.getID()));
				nodeIn.add(from);
				this.nodes.add(node);
			}
			if(!nodeIn.contains(to)){
				Node node = new Node(to);
				int in = 0;
				int out = 0;
				for(int pos=0;pos<this.adjMatrix.length;pos++){
					if(this.adjMatrix[to][pos]>0)
						out++;
					if(mt[to][pos]>0)
						in++;
				}
				node.setOutDegree(out);
				node.setInDegree(in);
				node.setName(String.valueOf(node.getID()));
				nodeIn.add(to);
				this.nodes.add( node);
			}
		}
	}
	
	private double[][] transpose(double[][] matrix){
		double[][] mt = new double[matrix.length][];
		int rows = matrix.length;
		if(rows==0)
			return mt;
		int cols = matrix[0].length;
		for(int i=0;i<rows;i++)
			mt[i] = new double[cols];
		
		for(int i=0;i<rows;i++)
			for(int j=0;j<cols;j++)
				mt[j][i] = matrix[i][j];
		return mt;
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
		res = this.adjList.get(node);
		if(res==null)
			return new Vector<Integer>();
		return res;
	}
	
	public  Vector<Edge> getAdjEdgeList(int node){
		Vector<Edge>  res  = null ;
		res = this.adjEdgeList.get(node);
		if(res==null)
			return new Vector<Edge>();
		 return res;
	}
	
	public int getNodeGroup(int nodeId){
		return 1;
	}
	
	@Override
	public Set<Node> getNodeSet() {
		return this.nodes;
	}
	@Override
	public Vector<Edge> getEdgeSet() {
		return this.edges;
	}
	
	public int getNodeNum(){
		return this.adjMatrix.length;
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
	
	@Override
	public JSONObject packToJson() throws JSONException{
		// TODO 自动生成的方法存根
		boolean gtype = (this.type==GraphType. UNDirectedGraph)? false:true;
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("type", gtype);
		jsonObj.put("weight", true);
		jsonObj.put("N", this.adjMatrix.length);
		jsonObj.put("E", this.edges.size());
		JSONArray nodesJs = new JSONArray();
		Iterator<Node> nodeite = nodes.iterator();
		while(nodeite.hasNext()){
			Node now = nodeite.next();
			JSONObject nodeObj = new JSONObject();
			nodeObj.put("name", now.getName());
			nodeObj.put("group",getNodeGroup(now.getID()));
			nodesJs.put(nodeObj);
		}
		jsonObj.put("nodes", nodesJs);
		
		JSONArray edgesJs = new JSONArray();
		Iterator<Edge> edgesite = edges.iterator();
		while(edgesite.hasNext()){
			Edge now = edgesite.next();
			JSONObject edgeObj = new JSONObject();
			edgeObj.put("source",now.getFromID());
			edgeObj.put("target",now.getToID());
			edgeObj.put("weight", now.getWeight());
			edgesJs.put(edgeObj);
		}
		jsonObj.put("edges", edgesJs);
		//jsonObj.put(key, value)
		return jsonObj;
	}

	@Override
	public void writeToJson(String filePath) throws JSONException {
		// TODO 自动生成的方法存根
		JSONObject jsonObj = packToJson();
		if(jsonObj != null){
			try {
				JsonDeal.writeFile(filePath, jsonObj.toString());
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	public void  writeNodeJson(String fileName){
		
	}
}


