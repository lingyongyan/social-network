/*FileName:BridgeNode.java
 * Date:2015,05.13
 * Author:Yan Lingyong
 * Description: Class deal with Node
 * */
package com.graphanalysis.algorithm.bridgedetection;

public class NodeForDetection {
	private int ID;
	private int depth;
	private int father;
	private int ancestor;
	private boolean reached;
	private boolean lastSonNew;
	public int visitNum;
	
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
