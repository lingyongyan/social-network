package com.graphanalysis.graphbase.interfaces;

import org.json.JSONArray;
import org.json.JSONException;

public interface NodeInterface {
	public int getInDegree();
	public int getOutDegree();
	public JSONArray packToJson() throws JSONException;
}
