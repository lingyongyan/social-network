package com.graphanalysis.graphbase.implement;

import java.util.Set;
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
	public JSONObject packetToJson() {
		// TODO 自动生成的方法存根
		JSONObject jsObj = new JSONObject();
		try {
			JSONArray jsedges = new JSONArray(); 
			for(int i=0;i<this.path.size();i++){
				Edge tmp = this.path.get(i);
				JSONObject jsedge = new JSONObject();
				jsedge.put("source", tmp.getFromID()).put("target", tmp.getToID()).put("weight", tmp.getWeight());
				jsedges.put(jsedge);
			}
			jsObj.put("edges", jsedges);
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return jsObj;
	}

}
