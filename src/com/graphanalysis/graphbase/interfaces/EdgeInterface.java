package com.graphanalysis.graphbase.interfaces;

import org.json.JSONException;
import org.json.JSONArray;

public interface EdgeInterface {
	
	public int getFromID();
	public int getToID();
	public double getWeight();
	public double getFlow();
	public JSONArray packToJson() throws JSONException;
}
