package com.graphanalysis.algorithm.bfsANDdfs;

import com.graphanalysis.algorithm.bfsANDdfs.invokClass.Graph;

public interface DFSImpl {
	public void dfs(Graph G, int v);
	public boolean marked(int v);
	public int count();
}
