/*FileName:BridgeNode.java
 * Date:2015,05.13
 * Author:Yan Lingyong
 * Description: Class deal with Node
 * */
package com.graphanalysis.algorithm.bridgedetection;

public class NodeForDetection {
	private int ID;//节点ID
	private int depth;//节点深度
	private int father;//节点的父节点ID
	private int ancestor;//节点的祖先节点
	private boolean reached;//表明节点是否已被访问
	private boolean lastSonNew;//表明上次访问的节点是不是新节点
	public int visitNum;//子节点的访问量
	
	public NodeForDetection(){
	}
	
	public NodeForDetection(int ID){
		this(ID,0);
	}
	
	public NodeForDetection(int ID,int depth){
		this(ID,depth,-1);
	}
	
	public NodeForDetection(int ID,int depth,int father){
		this(ID,depth,father,depth);
	}
	
	public NodeForDetection(int ID,int depth,int father,int ancestor){
		this.ID = ID;
		this.depth = depth;
		this.ancestor = ancestor;
		this.father = father;
		this.visitNum = 0;
		this.reached = false;
		this.lastSonNew = false;
	}
	
	public void set(int depth,int father,int ancestor){
		this.depth = depth;
		this.father = father;
		this.ancestor = ancestor;
	}
	
	public void reach(){
		this.reached = true;
	}
	
	public boolean getState(){
		return this.reached;
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	public void setDepth(int depth){
		this.depth = depth;
	}
	public void setFather(int father){
		this.father = father;
	}
	public void setAncestor(int ancestor){
		this.ancestor = ancestor;
	}
	
	public void setLastSonState(boolean state){
		this.lastSonNew = state;
	}
	
	public boolean getLastSonState(){
		return this.lastSonNew;
	}
	
	public int getID(){
		return this.ID;
	}
	public int getDepth(){
		return this.depth;
	}
	
	public int getFather(){
		return this.father;
	}
	public int getAncestor(){
		return this.ancestor;
	}
}
