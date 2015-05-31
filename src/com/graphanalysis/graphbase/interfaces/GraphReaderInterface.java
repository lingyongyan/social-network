package com.graphanalysis.graphbase.interfaces;

import java.util.Vector;

import com.graphanalysis.graphbase.implement.Edge;

public interface GraphReaderInterface {
	public Vector<Edge> readFromFile(String fileName,boolean undirected);
}
