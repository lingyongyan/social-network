package com.graphanalysis.graphbase.implement;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.graphanalysis.graphBase.commondefine.NodeColor;
import com.graphanalysis.graphbase.interfaces.NodeInterface;

public class Node implements NodeInterface{
	private int ID;//点ID
	private NodeColor color;//点的颜色
	private int indegree = 0;//点的入度
	private int outdegree = 0;//点的出度
	private String name = "";//点的名称
	
	public Node(int ID, NodeColor color, String name){
		this.ID = ID;
		this.color = color;
		this.name = name;
	}
	
	public Node(int ID,NodeColor color){
		this(ID,color,"");
	}
	
	public Node(int ID,String name){
		this(ID,NodeColor.unknown,name);
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
	}
	public void setOutDegree(int degree){
		this.outdegree = degree;
	}
	public void setDegree(int degree){
		this.indegree = degree;
		this.outdegree = degree;
	}
	public void setName(String name){
		this.name = name;
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
