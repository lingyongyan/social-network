package com.graphanalysis.graphbase.implement;

import com.graphanalysis.graphbase.interfaces.EdgeInterface;

public class Edge implements EdgeInterface{
	private int fromID;
	private int toID;
	private double weight = 0;
	private double flow = 0;
	/**
	 * @param fromID
	 * @param toID
	 * @param weight
	 */
	public Edge(int fromID, int toID, double weight) {
		super();
		this.fromID = fromID;
		this.toID = toID;
		this.weight = weight;
	}
	public Edge(int fromID, int toID) {
		this(fromID,toID,1);
	}
	/**
	 * @return fromID
	 */
	public int getFromID() {
		return fromID;
	}
	/**
	 * @param fromID 要设置的 fromID
	 */
	public void setFromID(int fromID) {
		this.fromID = fromID;
	}
	/**
	 * @return toID
	 */
	public int getToID() {
		return toID;
	}
	/**
	 * @param toID 要设置的 toID
	 */
	public void setToID(int toID) {
		this.toID = toID;
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
}
