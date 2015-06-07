package com.graphanalysis.algorithm.randomWalk;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.algorithm.implement.ExecParameter;
import com.graphanalysis.algorithm.implement.ExecReturn;
import com.graphanalysis.graphbase.commondefine.GraphReader;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.Node;
import com.graphanalysis.graphbase.interfaces.GraphInterface;

/**
 * Random walk Algorithm
 *
 *The algorithm starts at a node v0 and visits its adjacent nodes based on 
 *the transition probability (weight) assigned to edges connecting them. 
 *This procedure is performed for t steps (provided to the algorithm); 
 *therefore, a walk of length t is generated by the random walk.
 *
 * @author chenxx
 * @date   2015/5/29
 */
public class RandomWalk implements RandomWalkInterface {
	
	/*Biased value. a node goto a non-adjacent node by probability alpha*/
	private final double alpha;
	
	//A random seed node to start random walk.
	private int seed;     
	
	//Default Step number of random walk, default step = 10000;
	private final int defaultStep;     
	
	/*transition probability matrix used to store probabilities of one
	node goto another node*/
	private Map<Integer, HashMap<Integer, Double>> TPM;
	
	HashMap<Integer, RandomSelect<Integer>> randomList;
	
	private GraphInterface gragh; 
	
	//private double[][] adjMatrix;
	
	//@SuppressWarnings("unchecked")
	public RandomWalk(RandomWalkGraph graph) {
		this.gragh = graph;
		if(this.gragh==null)
			throw new NullPointerException("Graph must be not null.");
		this.alpha = 0;
		this.defaultStep = 10000;		
		
		TPM = new HashMap<Integer, HashMap<Integer,Double>>();
		this.computeTPM();

		this.seed = new RandomSelect<Integer>( new ArrayList<Integer>( TPM.keySet() ) ).next();
		
		this.randomList = new HashMap<Integer,RandomSelect<Integer> >();
		Set<Entry<Integer, HashMap<Integer, Double>>> entrySet = TPM.entrySet();
		for(Entry<Integer, HashMap<Integer, Double>> e : entrySet) {
			randomList.put(e.getKey(),
					new RandomSelect<Integer>( new ArrayList<Integer>(e.getValue().keySet()),
							new ArrayList<Double>(e.getValue().values()))  );
		}
	}
	
	
	/**
	 * Walk from a random node with default step number.
	 * @return walk path
	 */
	public List<Integer> walk() {
		List<Integer> walkPath = new ArrayList<Integer>();
		int state = 1;
		int v = this.seed;
		walkPath.add(v);
		while(state<this.defaultStep) {
			state = state+1;
			v = randomList.get(v).next();
			walkPath.add(v);
		}
		return walkPath;
	}
	
	/**
	 * Walk from a random node with a given step.
	 * @param step
	 * @return
	 */
	public List<Integer> walk(int step) {
		List<Integer> walkPath = new ArrayList<Integer>();
		int state = 1;
		int v = this.seed;
		walkPath.add(v);
		while(state<step) {
			state = state+1;
			v = randomList.get(v).next();
			walkPath.add(v);
		}
		return walkPath;
	}
	
	/**
	 * Walk from given start node with a given step;
	 * @param startNode
	 * @param step
	 * @return
	 */
	public List<Integer> walk(int startNode, int step) {
		List<Integer> walkPath = new ArrayList<Integer>();
		int state = 1;
		int v = startNode;
		walkPath.add(v);
		while(state<step) {
			state = state+1;
			v = randomList.get(v).next();
			walkPath.add(v);
		}
		return walkPath;
	}
	
	/**
	 * Walk from one node to another node with a given step threshold.
	 * If it suceess to walk to end node within threshold, return path,
	 * else return empty list.
	 * @param startNode
	 * @param endNode
	 * @param stepThreshold
	 * @return
	 */
	public List<Integer> walk(int startNode, int endNode, int stepThreshold) {
		List<Integer> walkPath = new ArrayList<Integer>();
		int state = 1;
		int v = startNode;
		walkPath.add(v);
		while(state<stepThreshold) {
			state = state+1;
			v = randomList.get(v).next();
			if(v==endNode) break;
			walkPath.add(v);
		}
		walkPath.add(endNode);
		if(state>stepThreshold) return null;
		else return walkPath;
	}
	
//	public void alg() {
//		RandomSelect<Integer> randomSelect[] = new RandomSelect[this.size];
//		for(int i=0; i<this.size; i++) {
//			randomSelect[i] = new RandomSelect(Arrays.asList(0,1,2), Arrays.asList(TPM[i]));
//		}
//		int state = 0;
//		int v = this.seed;
//		this.walkPath.add(v);
//		while(state<this.defaultStep) {
//			state = state+1;
//			v = randomSelect[v].next();
//			this.walkPath.add(v);
//		}
//	}
	
	/**
	 * compute transition probability matrix
	 * 
	 * The probability of node i to node j is 
	 * compute by the follow equation:
	 * 
	 * TPM[i][j] = ( weight[i][j]/Sum(weight of edge adjacent to i) ) * (1-alpha) + this.alpha/n
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void computeTPM() {
		List<Integer> nodeList = new ArrayList<Integer>();
		for(Node node:this.gragh.getNodeSet()) {
			nodeList.add(node.getID());
		}
		
		Map<Integer, Map<Integer, Double>> adj = ((RandomWalkGraph) this.gragh).getWeightAdjList(); 
		Map<Integer, Double> tmpmap = new HashMap<Integer, Double>();
		for(int k : nodeList) {
			tmpmap.put(k, 0.0);
		}
		for(int k : nodeList) {
			if(!adj.containsKey(k)) {
				adj.put(k,tmpmap);
			}
		}
		
		Set<Entry<Integer,Map<Integer,Double>>> entrySet = adj.entrySet();
		Set<Entry<Integer,Double>> interEntrySet;
		Entry<Integer,Map<Integer,Double>> entry;
		Iterator<Entry<Integer, Map<Integer, Double>>> iter = entrySet.iterator();
		
		
		double sum = 0;
		while(iter.hasNext()){
			sum = 0;
			entry = iter.next();
			interEntrySet = entry.getValue().entrySet();
			
			Set<Integer> nodeSet = new HashSet<Integer>(entry.getValue().keySet());
			for(int k : nodeList) {
				if(!nodeSet.contains(k)){
					adj.get(entry.getKey()).put(k, 0.0);
				}
			}
			
		    for(Entry<Integer,Double> e:interEntrySet){
		    	sum += e.getValue();
		    }
		    HashMap<Integer,Double> tmp = new HashMap<Integer,Double>();
		    int n = interEntrySet.size();
		    for(Entry<Integer,Double> e:interEntrySet){
		    	tmp.put(e.getKey(), (e.getValue()/sum) * (1-this.alpha) + this.alpha/n);
		    }
		    this.TPM.put(entry.getKey(), tmp);
		}		
	}

	@Override
	public JSONArray exec(int step) {
		// TODO Auto-generated method stub
		JSONArray jsonArray = new JSONArray();
		List<Integer> path = this.walk(step);
		for(int i=0; i<path.size()-1; i++) {
			JSONObject object = new JSONObject();
			try {
				object.put("source", path.get(i)).put("target", path.get(i+1));
				jsonArray.put(object);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonArray;
	}
	
	@Override
	public JSONArray exec(int start,int step) {
		// TODO Auto-generated method stub
		JSONArray jsonArray = new JSONArray();
		List<Integer> path = this.walk(start,step);
		for(int i=0; i<path.size()-1; i++) {
			JSONObject object = new JSONObject();
			try {
				object.put("source", path.get(i)).put("target", path.get(i+1));
				jsonArray.put(object);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonArray;
	}
	
	@Override
	public JSONArray exec() {
		// TODO 自动生成的方法存根
		JSONArray jsonArray = new JSONArray();
		List<Integer> path = this.walk();
		for(int i=0; i<path.size()-1; i++) {
			JSONObject object = new JSONObject();
			try {
				object.put("source", path.get(i)).put("target", path.get(i+1));
				jsonArray.put(object);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonArray;
	}


	@Override
	public JSONArray exec(int start, int end, int step) {
		// TODO 自动生成的方法存根
		JSONArray jsonArray = new JSONArray();
		List<Integer> path = this.walk(start,end,step);
		for(int i=0; i<path.size()-1; i++) {
			JSONObject object = new JSONObject();
			try {
				object.put("source", path.get(i)).put("target", path.get(i+1));
				jsonArray.put(object);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	@Override
	public ExecReturn exec(ExecParameter args) {
		// TODO 自动生成的方法存根
		if(args.size()!=2 ||  args.get(0).getClass()!=Integer.class ||args.get(1).getClass()!=Integer.class)
			return null;
		int start = (int) args.get(0);
		int step = (int)args.get(1);
		JSONArray jArray = this.exec(start,step);
		ExecReturn res = new ExecReturn();
		res.addResult(jArray);
		return res;
	}
}
