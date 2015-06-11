/**
 * 
 */
package com.graphanalysis.algorithm.randomWalk;

import org.json.JSONArray;

import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;

/**
 * A Interface to access some method of random walk
 * 
 * @author  chenxx
 * @date	2015/5/30
 */
public interface RandomWalkInterface extends AlgorithmInterface{
	
	/**
	 * Walk from a random node with default step number.
	 * @return walk path
	 */
	//public List<Integer> walk();
	
	
	/**
	 * Walk from a random node with a given step.
	 * @param step
	 * @return
	 */
	//public List<Integer> walk(int step);
	
	/**
	 * Walk from given start node with a given step;
	 * @param startNode
	 * @param step
	 * @return
	 */
	//public List<Integer> walk(int startNode, int step);
	
	
	/**
	 * Walk from one node to another node with a given step threshold.
	 * If it suceess to walk to end node within threshold, return path,
	 * else return empty list.
	 * @param startNode
	 * @param endNode
	 * @param stepThreshold
	 * @return
	 */
	//public List<Integer> walk(int startNode, int endNode, int stepThreshold);
	public JSONArray exec();
	public JSONArray exec(int step);
	public JSONArray exec(int start, int step);
	public JSONArray exec(int start, int end,int step);
	

}
