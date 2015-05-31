/*FileName:Bridge.java
 * Date:2015,05.13
 * Author:Yan Lingyong
 * Description: Class used to store the bridge detected in a graph
 * */
package com.graphanalysis.algorithm.bridgedetection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONException;

import com.graphanalysis.web.json.JsonDeal;

public class  Bridge{
	private Vector<int[]> vbridge = new Vector<int[]>();;
	public Bridge(){
		init();
	}
	
	private void init(){
		this.vbridge = new Vector<int[]>();
	}
	
	public void AddBridge(int a, int b){
		int[] s = new int[2];
		s[0] = a;
		s[1] = b;
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
	
	public int packToJSON() throws JSONException, IOException{
		String json = "{'name':'reiz'}";
		JSONObject jsonObj = new JSONObject(json);
		String name = jsonObj.getString("name");
		 
		System.out.println(jsonObj);
		 
		jsonObj.put("initial", name.substring(0, 1).toUpperCase());
		 
		String[] likes = new String[] { "JavaScript", "Skiing", "Apple Pie" };
		jsonObj.put("likes", likes);
		 
		System.out.println(jsonObj);
		 
		Map <String, String> ingredients = new HashMap <String, String>();
		ingredients.put("apples", "3kg");
		ingredients.put("sugar", "1kg");
		ingredients.put("pastry", "2.4kg");
		ingredients.put("bestEaten", "outdoors");
		jsonObj.put("ingredients", ingredients);
		JsonDeal.writeFile("/tmp/jason.txt",jsonObj.toString() );
		System.out.println(jsonObj);
		return 0;
		 
		}
}
