package com.graphanalysis.algorithm.bfsANDdfs;

import com.graphanalysis.algorithm.bfsANDdfs.invokClass.Graph;

public interface BFSImpl {
	public void bfs(Graph G, int s);
	public int count();
	public boolean marked(int v);
}
