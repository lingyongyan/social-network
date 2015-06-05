package com.graphanalysis.algorithm.bfsANDdfs;

import org.json.JSONArray;

import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.Path;

public interface BFSImpl extends AlgorithmInterface {
	public Path bfs(Graph G, int s);
	public int count();
	public boolean marked(int v);
	public JSONArray exec(String fileName,int s);
	public JSONArray exec(Graph G,int s);
}
