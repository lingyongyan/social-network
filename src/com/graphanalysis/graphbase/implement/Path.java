package com.graphanalysis.graphbase.implement;

import java.util.Set;

import com.graphanalysis.graphbase.interfaces.PathInteface;

public class Path implements PathInteface {
	private Set<Edge> path;
	
	public void addPath(Edge edge){
		this.path.add(edge);
	}
	@Override
	public Set<Edge> getPath() {
		return this.path;
	}

}
