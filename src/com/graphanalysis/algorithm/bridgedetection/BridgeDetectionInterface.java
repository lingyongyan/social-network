package com.graphanalysis.algorithm.bridgedetection;

import org.json.JSONArray;

import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.implement.Graph;

public interface BridgeDetectionInterface extends AlgorithmInterface {
	public  JSONArray exec(Graph myGraph);
	public  Bridge execB(Graph myGraph);
}
