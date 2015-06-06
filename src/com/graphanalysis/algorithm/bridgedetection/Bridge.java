/*FileName:Bridge.java
 * Date:2015.05.13
 * Author:Yan Lingyong
 * Description: Class used to store the bridge detected in a graph
 * */
package com.graphanalysis.algorithm.bridgedetection;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class  Bridge{
	private Vector<int[]> vbridge = new Vector<int[]>();//桥边集合
	public Bridge(){
	}
	
	/**
	 * 添加一个桥边,其中a,b分别是桥边连接节点的ID,按照ID大小从小到大依次放入数组中
	 */
	public void AddBridge(int a, int b){
		int[] s = new int[2];
		s[0] = a<=b?a:b;
		s[1] = a>b?a:b;
		vbridge.add(s);
	}
	
	public void set(Bridge other){
		this.vbridge = other.vbridge;
	}
	
	public void printBridge(){
		int size = this.vbridge.size();
		for(int i=0;i<size;i++){
			System.out.println("("+vbridge.get(i)[0]+","+vbridge.get(i)[1]+")");
		}
	}
	
	/**
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 * 将桥边打包成json对象
	 */
	public JSONArray packetToJSON(){
		JSONArray jsedges = new JSONArray();
		try {
			for(int i=0;i<this.vbridge.size();i++){
				int[] tmp = this.vbridge.get(i);
				JSONObject jsedge = new JSONObject();
				jsedge.put("source", tmp[0]).put("target", tmp[1]);
				jsedges.put(jsedge);
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return jsedges;
	}
}
