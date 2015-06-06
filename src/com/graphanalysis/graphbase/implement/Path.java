package com.graphanalysis.graphbase.implement;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.graphbase.interfaces.PathInteface;

public class Path implements PathInteface {
	private Vector<Edge> path;
	
	public Path(){
		this.path = new Vector<Edge>();
	}
	
	public void addPath(Edge edge){
		this.path.add(edge);
	}
	
	public void append(Path other){
		for(int i=0;i<other.path.size();i++){
			Edge e = other.path.get(i);
			this.path.add(e);
		}
	}
	
	@Override
	public Vector<Edge> getPath() {
		return this.path;
	}
	@Override
	public JSONArray packetToJson() {
		// TODO 自动生成的方法存根
		JSONArray jsedges = new JSONArray();
		try {
			for(int i=0;i<this.path.size();i++){
				Edge tmp = this.path.get(i);
				JSONObject jsedge = new JSONObject();
				jsedge.put("source", tmp.getFromID());
				jsedge.put("target", tmp.getToID());
				jsedge.put("weight", tmp.getWeight());
				jsedges.put(jsedge);
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return jsedges;
	}

}
