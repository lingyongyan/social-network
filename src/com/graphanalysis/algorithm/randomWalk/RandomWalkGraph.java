package com.graphanalysis.algorithm.randomWalk;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.Node;
import com.graphanalysis.graphbase.interfaces.GraphInterface;

public class RandomWalkGraph extends Graph  implements GraphInterface{
	
	/*A adjacency list with weight*/
	private Map<Integer, Map<Integer, Double>> weightAdjList;
	
	public RandomWalkGraph(){
	}
	
	public RandomWalkGraph(Vector<Edge> edges) {
		super(edges,false);
		this.weightAdjList = new HashMap< Integer, Map<Integer, Double> >();
		constructWeightAdjList(this.edges);
	}
	
	public RandomWalkGraph(Vector<Edge> edges,Vector<Node> nodes) {
		super(edges,nodes,false);
		this.weightAdjList = new HashMap< Integer, Map<Integer, Double> >();
		constructWeightAdjList(this.edges);
	}
	
	/**
	 * Construct adjacency list with weight
	 * @param edges
	 */
	protected void constructWeightAdjList(Vector<Edge> edges){
		int size = edges.size();
		for(int i=0;i<size;i++){
			Edge edge = edges.get(i);
			int fromID = edge.getFromID();
			int toID = edge.getToID();
			if(this.weightAdjList.containsKey(fromID)){
				this.weightAdjList.get(fromID).put(toID, edge.getWeight());
			}else{
				HashMap<Integer,Double> tmp = new HashMap<Integer,Double>();
				tmp.put( toID, edge.getWeight() );
				this.weightAdjList.put( fromID, tmp);
			}
			
			if(this.weightAdjList.containsKey(toID)){
				this.weightAdjList.get(toID).put(fromID, edge.getWeight());
			}else{
				HashMap<Integer,Double> tmp = new HashMap<Integer,Double>();
				tmp.put( fromID, edge.getWeight() );
				this.weightAdjList.put( toID, tmp);
			}
		}
	}

	public Map<Integer, Map<Integer, Double>> getWeightAdjList() {
		return weightAdjList;
	}

	public void setWeightAdjList(
			Map<Integer, Map<Integer, Double>> weightAdjList) {
		this.weightAdjList = weightAdjList;
	}
	
	
}
