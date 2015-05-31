package com.graphanalysis.graphbase.implement;

import com.graphanalysis.graphBase.commondefine.NodeColor;
import com.graphanalysis.graphbase.interfaces.NodeInterface;

public class Node implements NodeInterface{
	private int ID;
	private NodeColor color;
	private int indegree = 0;
	private int outdegree = 0;
	private String name = "";
	
	public Node(int ID,NodeColor color){
		this.ID = ID;
		this.color = color;
	}
	
	public Node(int ID){
		this(ID,NodeColor.unknown);
	}
	
	public String getName(){
		return this.name;
	}
	
	
	public void setID(int ID){
		this.ID = ID;
	}
	public void setColor(NodeColor color){
		this.color = color;
	}
	public void setInDegree(int degree){
		this.indegree = degree;
		this.outdegree = degree;
	}
	public void setOutDegree(int degree){
		this.outdegree = degree;
	}
	public void setDegree(int degree){
		this.indegree = degree;
		this.outdegree = degree;
	}
	
	public int getID(){
		return this.ID;
	}
	public NodeColor getColor(){
		return this.color;
	}

	@Override
	public int getInDegree() {
		return this.indegree;
	}

	@Override
	public int getOutDegree() {
		return this.outdegree;
	}
}
