/**
 * 
 */
package com.graphanalysis.algorithm.bipartiteMatching;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * @author xiaoxu
 * @date 2015/6/6
 */
public class IsBipartiteGraph {
	 private int numberOfVertices;
	 private Queue<Integer> queue;
	 int[] colored;
	public static final int NO_COLOR = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	public IsBipartiteGraph(int numberOfVertices)
	 {
	 this.numberOfVertices = numberOfVertices;
	 queue = new LinkedList<Integer>();
	 colored = new int[numberOfVertices];
	 }

	public  boolean isBipartite(double[][] adjMat) {
		//int numberOfVertices = adjMat.length;
		int source = new Random().nextInt(numberOfVertices);
		//Queue<Integer> queue = new LinkedList<Integer>();
		for (int vertex = 0; vertex < numberOfVertices; vertex++) {
			colored[vertex] = NO_COLOR;
		}
		colored[source] = RED;
		queue.add(source);

		int element, neighbour;
		while (!queue.isEmpty()) {
			element = queue.remove();
			neighbour = 0;
			while (neighbour < numberOfVertices) {
				if (adjMat[element][neighbour] == 1
						&& colored[element] == colored[neighbour]) {
					return false;
				}
				if (adjMat[element][neighbour] == 1
						&& colored[neighbour] == NO_COLOR) {
					colored[neighbour] = (colored[element] == RED) ? BLUE : RED;
					queue.add(neighbour);
				}
				neighbour++;
			}
		}
		for(int i=0;i<numberOfVertices;i++){
			if(colored[i]==NO_COLOR)
				return false;
		}
		return true;
	}

}