package com.graphanalysis.algorithm.dijkstra;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.algorithm.implement.ExecParameter;
import com.graphanalysis.algorithm.implement.ExecReturn;
import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.Path;

public class Dijkstra implements AlgorithmInterface{
	private class Results{
		public double[] distTo;
		public int[] edgeTo;
		public boolean state = false;
	}
	private Results result = new Results();

	public JSONObject exec(Graph graph, int startPoint) {
		JSONObject jObj = new JSONObject();
		if(!this.result.state)
			bfs(graph, startPoint);

		for (int i = 0; i < graph.getNodeNum(); i++) {
			if (i != startPoint) {
				try {
					jObj.put(String.valueOf(i), exec(graph, startPoint, i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return jObj;
	}
	/**
	 * 修改说明：Dijkstra算法用bfs的方式进行遍历，然后不断更新节点的信息，执行完后得到的就是各点的最短路径长度
	 * 而在中途记录下的到达该点途径的上一个节点（edgeTo保存）就是最短路径中到达该点经过的最后一个节点
	 * 所以只需要执行一次算法即可
	 * **/
	public void bfs(Graph G, int s) {
		int nodeNum =  G.getNodeNum();
		result.distTo = new double[nodeNum];
		result.edgeTo = new int[nodeNum];
		Queue<Integer> q = new LinkedList<Integer>();
		for (int v = 0; v < G.getNodeNum(); v++) {
			result.distTo[v] = Double.MAX_VALUE;
			result.edgeTo[v] = -1;
		}
		result.distTo[s] = 0;

		q.offer(s);
		while (!q.isEmpty()) {
			int v = q.poll();
			//Iterator<Pair> it = G.getWeiAdjList().get(v).iterator();
			Iterator<Edge> it = G.getAdjEdgeList(v).iterator();
			while(it.hasNext()){
				Edge edge = it.next();
				int w = edge.getToID();
				if (result.distTo[v] + edge.getWeight()< result.distTo[w]) {
					result.edgeTo[w] = v;
					result. distTo[w] = result.distTo[v] + edge.getWeight();
					q.offer(w);
				}
			}
		}
		result.state = true;
	}


	/**
	 * 直接从保存的路径长度以及路径信息恢复路径
	 * **/
	private JSONArray exec(Graph G,int src, int dst){
		if(!result.state)
			this.bfs(G,src);
		Path path = new Path();
		int v = dst;
		int u = result.edgeTo[v];
		Vector<Edge> res = new Vector<Edge>();
		while(v!=src){
			if(u==-1)
				return path.packetToJson();
			res.add(new Edge(u,v,G.getAdjMatrix()[u][v]));
			v = u;
			u = result.edgeTo[u];
		}
		for(int i=res.size()-1;i>=0;i--){
			path.addPath(res.get(i));
		}
		return path.packetToJson();
	}

	@Override
	public ExecReturn exec(ExecParameter args) {
		// TODO 自动生成的方法存根
		if(args.size()!=2 || args.get(0).getClass()!=Graph.class || args.get(1).getClass()!=Integer.class)
			return null;
		Graph myGraph =  ((Graph) args.get(0));
		int startNode = (int) args.get(1);
		JSONObject jObject = this.exec(myGraph,startNode);
		ExecReturn res = new ExecReturn();
		res.addResult(jObject);
		return res;
	}
}
