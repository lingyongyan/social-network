package com.graphanalysis.algorithm.primmst;
import org.json.JSONArray;

import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.implement.Graph;

public interface PrimMSTImpl extends AlgorithmInterface{
	public JSONArray exec(String fileName,int s);
	public JSONArray exec(Graph myGraph,int s);
}
