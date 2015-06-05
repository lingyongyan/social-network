
package com.graphanalysis.web.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.*;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.algorithm.bfsANDdfs.BFSImpl;
import com.graphanalysis.algorithm.bfsANDdfs.BreadFirstSearch;
import com.graphanalysis.algorithm.bfsANDdfs.DFSImpl;
import com.graphanalysis.algorithm.bfsANDdfs.DepthFirstSearch;
import com.graphanalysis.algorithm.bridgedetection.Bridge;
import com.graphanalysis.algorithm.bridgedetection.BridgeDetection;
import com.graphanalysis.algorithm.bridgedetection.BridgeDetectionInterface;
import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.algorithm.primmst.PrimMST;
import com.graphanalysis.algorithm.primmst.PrimMSTImpl;
import com.graphanalysis.graphBase.commondefine.GraphReader;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.GraphException;
import com.graphanalysis.web.com.ObjectPool;

public class SolutionEntry {
	public static void solve(String method, String[] args,HttpServletResponse response) {//处理函数入口
		if(method ==null)
			return;
		method = method.toUpperCase();
		String dataSets = args[1];
		String localFile = args[2];
		int id=0;
		if(args.length>3)
			id= Integer.valueOf(args[3]);
		AlgorithmInterface algorithm = null;
		try {
			Graph myGraph = null;
			myGraph = (Graph)ObjectPool.getInstance().getObject(dataSets, localFile);
			//myGraph = GraphReader.readGraphFromJson(localFile);
			if(myGraph == null)
				throw new GraphException("Graph Should Be Null!");
			switch(method){
			case "DFS":
				algorithm = new DepthFirstSearch(myGraph.getNodeNum());
				JSONArray dr = ((DFSImpl)algorithm).exec(myGraph, id);
				response.getOutputStream().write(dr.toString().getBytes("UTF-8"));
				break;
			case "BFS":
				algorithm = new BreadFirstSearch(myGraph.getNodeNum());
				JSONArray br = ((BFSImpl)algorithm).exec(myGraph, id);
				response.getOutputStream().write(br.toString().getBytes("UTF-8"));
				break;
			case"PRIM":
				algorithm = new PrimMST(myGraph.getNodeNum());
				JSONArray pr = ((PrimMSTImpl)algorithm).exec(myGraph, id);
				response.getOutputStream().write(pr.toString().getBytes("UTF-8"));
				break;
			case "BRIDGE":
				algorithm = new BridgeDetection();
				JSONArray bridgeJSON =  ((BridgeDetectionInterface)algorithm).exec(myGraph);
				response.getOutputStream().write(bridgeJSON.toString().getBytes("UTF-8"));
				break;
			case "GRAPH":
				JSONObject graphJson =  myGraph.packToJson();
				response.getOutputStream().write(graphJson.toString().getBytes("UTF-8"));
				break;
			default:;
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}catch(GraphException e){
			System.out.println(e);
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		response.setContentType("text/json; charset=UTF-8");
	}
}
