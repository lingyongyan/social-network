package com.graphanalysis.graphbase.implement;

import org.json.JSONArray;
import org.json.JSONException;

import com.graphanalysis.graphbase.interfaces.EdgeInterface;

public class Edge implements EdgeInterface{
	private Node fromNode;//边的始节点
	private Node toNode;//边的终节点
	private double weight = 0;//边的权重
	private double flow = 0;//边的流
	/**
	 * @param fromID
	 * @param toID
	 * @param weight
	 */
	public Edge(Node fromNode, Node toNode, double weight) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.weight = weight;
	}
	public Edge(Node fromNode, Node toNode) {
		this(toNode,toNode,1);
	}
	
	public Edge(int fromID, int toID, double weight) {
		this.fromNode = new Node(fromID);
		this.toNode = new Node(toID);
		this.weight = weight;
	}
	public Edge(int fromID, int toID) {
		this(fromID,toID,1);
	}
	
	public Node getFromNode(){
		return this.fromNode;
	}
	public Node getToNode(){
		return this.toNode;
	}
	
	public void setFlow(double flow) {
		// TODO 自动生成的方法存根
		this.flow = flow;
	}
	
	/**
	 * @return fromID
	 */
	public int getFromID() {
		return fromNode.getID();
	}
	/**
	 * @param fromID 要设置的 fromID
	 */
	public void setFromID(int fromID) {
		this.fromNode.setID(fromID);
	}
	/**
	 * @return toID
	 */
	public int getToID() {
		return toNode.getID();
	}
	/**
	 * @param toID 要设置的 toID
	 */
	public void setToID(int toID) {
		this.toNode.setID(toID);
	}
	/**
	 * @return weight
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * @param weight 要设置的 weight
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	@Override
	public double getFlow() {
		// TODO 自动生成的方法存根
		return flow;
	}
	@Override
	public JSONArray packToJson() throws JSONException {
		// TODO 自动生成的方法存根
		return null;
	}
}
