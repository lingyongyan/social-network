package com.graphanalysis.algorithm.bfsANDdfs;
import java.io.*;
import java.util.Iterator;
import java.util.Vector;

import org.json.JSONArray;

import com.graphAnalysis.algorithm.implement.ExecParameter;
import com.graphAnalysis.algorithm.implement.ExecReturn;
import com.graphanalysis.graphBase.commondefine.GraphReader;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.GraphException;
import com.graphanalysis.graphbase.implement.Path;

public class DepthFirstSearch  implements DFSImpl{
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int count;           // number of vertices connected to s
    File file = new File("dfsResult");
    String content="";
    public DepthFirstSearch(int num) {
        marked = new boolean[num];
    }

    // depth first search from v
    public Path dfs(Graph G, int v) {
        Path result = new Path();
    	count++;
        marked[v] = true;
        Iterator<Integer> it = G.getAdjList(v).iterator();
        while(it.hasNext()){
        	int w = it.next();
            if (!marked[w]) {
            		   result.addPath(new Edge(v,w));
                       result.append(dfs(G, w));
            	}
            	//System.out.print(v + " "+ w + "\n");
            }
        return result;
        }

    public boolean marked(int v) {
        return marked[v];
    }

    public int count() {
        return count;
    }

    public static void main(String[] args) {
        //In in = new In(args[0]);
		Vector<Edge> edges = GraphReader.readFromFile("/tmp/tinyG.txt",2);
		Graph G = new Graph(edges);
        int s = Integer.parseInt("1");
        DepthFirstSearch search = new DepthFirstSearch(G.getNodeNum());
        Path res = search.dfs(G, s);
        res.packetToJson();
        //System.out.print("success!");
    }

    public JSONArray exec(Graph myGraph, int s) {
		// TODO 自动生成的方法存根
		JSONArray res = null;
		try{
		if(myGraph == null)
			throw new GraphException("Graph Should Be Null!");
		Path bres = this.dfs(myGraph,s);
		res  = bres.packetToJson();
		}catch(GraphException e){
			System.out.println(e);
		}
		//String fJson = JsonDeal.ReadFile(request.getSession().getServletContext().getRealPath(localFile));
		return res;
	}

	@Override
	public ExecReturn exec(ExecParameter args) {
		// TODO 自动生成的方法存根
		if(args.size()!=2 || args.get(0).getClass()!=Graph.class || args.get(1).getClass() != Integer.class)
			return null;
		Graph myGraph = (Graph) args.get(0);
		int s = (int) args.get(1);
		JSONArray jArray = this.exec(myGraph,s);
		ExecReturn res = new ExecReturn();
		res.addResult(jArray);
		return res;
	}

}

