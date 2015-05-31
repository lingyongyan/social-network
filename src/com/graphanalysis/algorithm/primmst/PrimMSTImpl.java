package com.graphanalysis.algorithm.primmst;
import com.graphanalysis.algorithm.bfsANDdfs.invokClass.EdgeWeightedGraph;

public interface PrimMSTImpl {
	public void prim(EdgeWeightedGraph G, int s);
	public double weight();
	public boolean check(EdgeWeightedGraph G);
}
