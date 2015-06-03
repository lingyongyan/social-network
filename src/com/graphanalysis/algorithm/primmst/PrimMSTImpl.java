package com.graphanalysis.algorithm.primmst;
import com.graphanalysis.graphbase.implement.Graph;

public interface PrimMSTImpl {
	public void prim(Graph G, int s);
	public double weight();
	public boolean check(Graph G);
}
