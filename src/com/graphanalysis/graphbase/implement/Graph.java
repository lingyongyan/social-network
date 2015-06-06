package com.graphanalysis.graphbase.implement;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	protected HashMap<Integer,Node> nodeMap = new HashMap<Integer,Node>();
	protected Vector<Node> nodes = new Vector<Node>();//点集
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
			for(int j=0;j<columns;j++){
				if(adjMatrix[i][j]!=0){
					this.edges.add(new Edge(i,j));
				}
			}
		}
		constructAdjList();
		constructNodeSet();
		setNodeDegree();
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
		if(this.edges.size()==0)
			return;
		constructAdjList();
		constructAdjMatrix();
		constructNodeSet();
		setNodeDegree();
	}
	
	public Graph(Vector<Edge> edges,Vector<Node> nodes,Boolean t) {
		if(t)
			this.type = GraphType. DirectedGraph;
		else
			this.type = GraphType.UNDirectedGraph;
		setEdges(edges);
		setNodes(nodes);
		if(this.edges.size()==0 || this.nodes.size() == 0)
			return;
		constructAdjList();
		constructAdjMatrix();
		setNodeDegree();
	}
	
	/**
	 * @param edges
	 * 根据边集构建邻接链表
	 */
	protected void constructAdjList(){
		if(this.edges.size()<=0)
			return;
		int size = edges.size();
		for(int i=0;i<size;i++){
			Edge edge = edges.get(i);
			Node fromNode = edge.getFromNode();
			Node toNode = edge.getToNode();
			addLinkedList(fromNode.getID(),toNode.getID(),edge);
			if(this.type == GraphType.UNDirectedGraph){
				Edge edgeInverse = new Edge(toNode,fromNode,edge.getWeight());
				addLinkedList(toNode.getID(),fromNode.getID(),edgeInverse);
			}
		}
	}
	
	/**
	 * @param fromID:起始节点
	 * @param toID:终止节点
	 * @param edge:边
	 */
	protected void addLinkedList(int fromID, int toID,Edge edge){
		if(this.adjList.containsKey(fromID)){//如果邻接链表中已有该节点项则在该项后追加新的链接节点ID
			Vector<Integer> alreadyin = this.adjList.get(fromID);
			Vector<Edge> alEdge = this.adjEdgeList.get(fromID);
			
			alreadyin.add(toID);
			alEdge.add(edge);
			
		}else{
			Vector<Integer> newitem = new Vector<Integer>();
			Vector<Edge> edgeItem = new Vector<Edge>();
			
			newitem.add(toID);
			this.adjList.put(fromID, newitem);
			edgeItem.add(edge);
			this.adjEdgeList.put(fromID, edgeItem);
		}
	}
	
	/**
	 * 根据邻接链表构建邻接矩阵
	 */
	protected int constructAdjMatrix(){
		if(this.adjEdgeList.size()<=0)
			this.constructAdjList();
		if(this.nodes.size()<=0)
			this.constructNodeSet();
		int size = this.nodes.size();
		Iterator<Entry<Integer, Vector<Edge>>> it =  this.adjEdgeList.entrySet().iterator();
		this.adjMatrix = new double[size][];
	    for(int i=0;i<size;i++){
	    	this.adjMatrix[i] = new double[size];
	    	for(int j=0;j<size;j++){
	    		this.adjMatrix[i][j] = 0;
	    	}
	    }
	    while (it.hasNext()) {    
	    	Entry<Integer, Vector<Edge>> entry =    it.next();    
	        Integer key = (Integer) entry.getKey();
	        Vector<Edge> values = entry.getValue();
	       for(int i=0;i<values.size();i++){
	    	   this.adjMatrix[key][values.get(i).getToID()] = values.get(i).getWeight();
	       }
	    }
		return 0;
	}
	
	/**
	 * 根据边集构建点集,必须在adjList创建之前进行
	 */
	protected void constructNodeSet(){
		if(this.nodes.size()!=0)
			return;
		for(int i=0;i<this.edges.size();i++){
			Node fromNode =this.edges.get(i).getFromNode();
			Node toNode = this.edges.get(i).getToNode();
			if(!this.nodeMap.containsKey(fromNode.getID())){
				this.nodes.add(fromNode);
				this.nodeMap.put(fromNode.getID(), fromNode);
			}
			if(!this.nodeMap.containsKey(toNode.getID())){
				this.nodes.add(toNode);
				this.nodeMap.put(toNode.getID(), toNode);
			}
		}
	}
	/**
	 * @param matrix
	 * @return double[][]
	 */
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
	
	protected void setNodeDegree(){
		if(this.nodes.size()<=0)
			constructNodeSet();
		if(this.adjMatrix.length<=0)
			constructAdjMatrix();
		double[][] mt = transpose(this.adjMatrix);
		for(int i=0;i<mt.length;i++){
			int out = 0;
			int in = 0;
			for(int j=0;j<mt[i].length;j++){
				if(this.adjMatrix[i][j]>0)
					out++;
				if(mt[i][j]>0)
					in++;
			}
			if(this.nodeMap.containsKey(i)){
				this.nodeMap.get(i).setInDegree(in);
				this.nodeMap.get(i).setOutDegree(out);
			}
		}
	}
	
	public void setType(GraphType t ){
		this.type = t;
	}
	
	protected void setNodes(Vector<Node> nodes){
		this.nodes.addAll(nodes);
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

	public Vector<Integer> getAdjList(int nodeID){//获取某个节点邻接的全部节点
		Vector<Integer> res = null;
		res = this.adjList.get(nodeID);
		if(res==null)
			return new Vector<Integer>();
		return res;
	}
	
	public  Vector<Edge> getAdjEdgeList(int nodeID){
		Vector<Edge>  res  = null ;
		res = this.adjEdgeList.get(nodeID);
		if(res==null)
			return new Vector<Edge>();
		 return res;
	}
	
	public int getNodeGroup(int nodeId){
		return 1;
	}
	
	@Override
	public Vector<Node> getNodeSet() {
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


