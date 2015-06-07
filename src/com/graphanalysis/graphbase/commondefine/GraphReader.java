package com.graphanalysis.graphbase.commondefine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.Node;
import com.graphanalysis.graphbase.interfaces.GraphReaderInterface;
import com.graphanalysis.web.json.JsonDeal;

public class GraphReader{
	/**
	 * @param fileName
	 * @param gtype  gtype使用说明如下，如果gtype的二进制表示的最低位为1，则是有向图，否则为无向图；二进制表示的第二位为1是有权图
	 * @return
	 * 
	 */
	public static Vector<Edge> readFromFile(String filePath,int gtype){
		File file = new File(filePath);
		BufferedReader reader = null;
		boolean directed = (gtype&1)!=0?true:false;//表明图是否有向，true表示有向，如果是无向图，则默认将读入的边在逆向写一次
		boolean weighted = (gtype&2)!=0?true:false;//表明图是否有权，true表示有权，如果是有权图，则继续读入第三列的值
		String[] fileName = filePath.split("/");
		System.out.println("从文件"+fileName[fileName.length-1]+"中新建graph");
		try{
			reader = new BufferedReader(new FileReader(file));
			String tempStr = null;
			Vector<Edge> edges = new Vector<Edge>();
			HashMap<Integer,Node> nodeMap = new HashMap<Integer,Node>();
			while((tempStr=reader.readLine())!=null){
				String[] arr = tempStr.split(" ");
				int fromID = Integer.parseInt(arr[0]);
				int toID = Integer.parseInt(arr[1]);
				double weight = 1;
				if(weighted)//如果是有权图
					weight = Double.parseDouble(arr[2]);
				if(!nodeMap.containsKey(fromID))
				{
					nodeMap.put(fromID, new Node(fromID,String.valueOf(fromID)));
				}
				if(!nodeMap.containsKey(toID))
				{
					nodeMap.put(toID, new Node(toID,String.valueOf(toID)));
				}
				Edge edge = new Edge(nodeMap.get(fromID),nodeMap.get(toID),weight);
				edges.add(edge);
			}
			reader.close();
			return edges;
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try{
					reader.close();
				}catch(IOException e){
				}
			}
		}
		return new Vector<Edge>();
	}

	public static Graph readGraphFromJson(String fileName){
		Graph gra = null;
		try {
			String strJson = JsonDeal.ReadFile(fileName);
			JSONObject jsonObj;
			jsonObj = new JSONObject(strJson);

			//jsonObj =  new JSONObject(strJson);
			boolean weighted =Boolean.valueOf( jsonObj.get("weight").toString());
			boolean directed = Boolean.valueOf( jsonObj.get("type").toString());
			Vector<Edge> edges = new Vector<Edge>();
			JSONArray jsonAr = jsonObj.getJSONArray("edges");
			for(int i=0; i < jsonAr.length();i++){
				JSONObject jo = (JSONObject) jsonAr.get(i);
				int from =Integer.valueOf( jo.get("source").toString());
				int to =Integer.valueOf( jo.get("target").toString());
				double weight = Double.valueOf( jo.get("weight").toString());
				Edge edge = new Edge(from,to,weight);
				edges.add(edge);
				gra = new Graph(edges,directed);
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		System.out.println("Reader OK");
		return gra;
	}
	
	public static GraphReaderData readGraphFromFile(String filePath,int gtype){
		File file = new File(filePath);
		BufferedReader reader = null;
		boolean directed = (gtype&1)!=0?true:false;//表明图是否有向，true表示有向，如果是无向图，则默认将读入的边在逆向写一次
		boolean weighted = (gtype&2)!=0?true:false;//表明图是否有权，true表示有权，如果是有权图，则继续读入第三列的值
		GraphReaderData gData = new GraphReaderData();
		gData.setType(directed);
		String[] fileName = filePath.split("/");
		System.out.println("从文件"+fileName[fileName.length-1]+"中新建graph");
		try{
			reader = new BufferedReader(new FileReader(file));
			String tempStr = null;
			while((tempStr=reader.readLine())!=null){
				if(tempStr.charAt(0)=='#')
					continue;
				String[] arr = tempStr.trim().split("\\s+");
				if(weighted && arr.length!=3 || !weighted && arr.length!=2)
					return null;
				String from = arr[0];
				String to = arr[1];
				double weight = 1;
				if(weighted)//如果是有权图
					weight = Double.parseDouble(arr[2]);
				gData.addNode(from);
				gData.addNode(to);
				Edge edge = new Edge(gData.getNode(from),gData.getNode(to),weight);
				gData.addEdge(edge);
			}
			reader.close();
			return gData;
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try{
					reader.close();
				}catch(IOException e){
				}
			}
		}
		return gData;
	}
}
