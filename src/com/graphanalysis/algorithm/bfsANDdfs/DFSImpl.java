package com.graphanalysis.algorithm.bfsANDdfs;

import org.json.JSONArray;

import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.Path;

public interface DFSImpl extends AlgorithmInterface {
	public Path dfs(Graph G, int v);
	public boolean marked(int v);
	public int count();
	public JSONArray exec(String fileName,int s);
	public JSONArray exec(Graph G,int s);
}
