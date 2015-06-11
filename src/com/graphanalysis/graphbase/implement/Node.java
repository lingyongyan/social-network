package com.graphanalysis.graphbase.implement;

import org.json.JSONArray;
import org.json.JSONException;

import com.graphanalysis.graphbase.commondefine.NodeColor;
import com.graphanalysis.graphbase.interfaces.NodeInterface;

public class Node implements NodeInterface{
	private Integer ID;//点ID
	private NodeColor color;//点的颜色
	private Integer indegree = 0;//点的入度
	private Integer outdegree = 0;//点的出度
	private String name = "";//点的名称
	private Integer group = 1;
	
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
	
	public void setGroup(Integer newGroup){
		this.group = newGroup;
	}
	
	public Integer getGroup(){
		return this.group;
	}
	
	@Override
	public int getInDegree() {
		return this.indegree;
	}

	@Override
	public int getOutDegree() {
		return this.outdegree;
	}

	@Override
	public JSONArray packToJson() throws JSONException {
		// TODO 自动生成的方法存根
		return null;
	}
}
