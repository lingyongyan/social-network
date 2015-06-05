package com.graphanalysis.graphbase.interfaces;

import java.util.Vector;

import org.json.JSONArray;

import com.graphanalysis.graphbase.implement.Edge;
public interface PathInteface {
	public Vector<Edge> getPath();
	public JSONArray packetToJson();
}
