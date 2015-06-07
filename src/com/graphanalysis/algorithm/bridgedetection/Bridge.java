/*FileName:Bridge.java
 * Date:2015.05.13
 * Author:Yan Lingyong
 * Description: Class used to store the bridge detected in a graph
 * */
package com.graphanalysis.algorithm.bridgedetection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Path;

public class  Bridge extends Path{
	public JSONArray packetToJSON(){
		JSONArray jsedges = new JSONArray();
		try {
			for(int i=0;i<this.path.size();i++){
				Edge edge = this.path.get(i);
				JSONObject jsedge = new JSONObject();
				jsedge.put("source", edge.getFromID()).put("target", edge.getToID());
				jsedges.put(jsedge);
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return jsedges;
	}
}
