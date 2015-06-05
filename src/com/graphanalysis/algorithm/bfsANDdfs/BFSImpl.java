package com.graphanalysis.algorithm.bfsANDdfs;

import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.Path;

public interface BFSImpl {
	public Path bfs(Graph G, int s);
	public int count();
	public boolean marked(int v);
}
