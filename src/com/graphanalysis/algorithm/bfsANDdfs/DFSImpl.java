package com.graphanalysis.algorithm.bfsANDdfs;

import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.Path;

public interface DFSImpl {
	public Path dfs(Graph G, int v);
	public boolean marked(int v);
	public int count();
}
