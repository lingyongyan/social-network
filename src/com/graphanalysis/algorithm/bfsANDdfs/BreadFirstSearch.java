package com.graphanalysis.algorithm.bfsANDdfs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import org.json.JSONArray;

import com.graphanalysis.algorithm.implement.ExecParameter;
import com.graphanalysis.algorithm.implement.ExecReturn;
import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.commondefine.GraphReader;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.GraphException;
import com.graphanalysis.graphbase.implement.Path;

public class BreadFirstSearch implements AlgorithmInterface {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int count;           // number of vertices connected to s
    private static final int INFINITY = Integer.MAX_VALUE;
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path

    public BreadFirstSearch(Integer num) {
    	 marked = new boolean[num];
         distTo = new int[num];
         edgeTo = new int[num];
    }

    // depth first search from v
    /**
     * @param G
     * @param s
     * @return
     */
    public Path bfs(Graph G, int s) {
    	Path result = new Path();
        Queue<Integer> q = new LinkedList<Integer>();
        for (int v = 0; v < G.getNodeNum(); v++) {
        	distTo[v] = INFINITY;
        	marked[v] = false;
        }
        distTo[s] = 0;
        marked[s] = true;
        q.offer(s);

        while (!q.isEmpty()) {
            int v = q.poll();
            Iterator<Integer> it = G.getAdjList(v).iterator();
            while(it.hasNext()){
            	int w = it.next();
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.offer(w);
                    //content=content + v+ " " + w + "\r\n";
                    Edge tmp = new Edge(v,w);
                    result.addPath(tmp);
                }
            }
        }
/*        try {
     	   File file = new File("bfsResult.txt");
     	   // if file doesnt exists, then create it
     	   if (!file.exists()) {
     	    file.createNewFile();
     	   }

     	   FileWriter fw = new FileWriter(file.getAbsoluteFile());
     	   BufferedWriter bw = new BufferedWriter(fw);
     	   bw.write(content);
     	   bw.close();
     	  } catch (IOException e) {
     	   e.printStackTrace();
     	  }   */  
        return result;
     }

    public boolean marked(int v) {
        return marked[v];
    }

    public int count() {
        return count;
    }
    
	public JSONArray exec(Graph myGraph, int s) {
		// TODO 自动生成的方法存根
		JSONArray res = null;
		try{
		if(myGraph == null)
			throw new GraphException("Graph Should Be Null!");
		Path bres = this.bfs(myGraph,s);
		res  = bres.packetToJson();
		}catch(GraphException e){
			System.out.println(e);
		}
		//String fJson = JsonDeal.ReadFile(request.getSession().getServletContext().getRealPath(localFile));
		return res;
	}
	
    public static void main(String[] args) {
        //In in = new In(args[0]);
    	//In in = new In("tinyG.txt");
		Vector<Edge> edges = GraphReader.readFromFile("/tmp/tinyG.txt",2);
		Graph G = new Graph(edges);
        //Graph G = new Graph(in);
        //int s = Integer.parseInt(args[1]);
        int s = Integer.parseInt("1");
        BreadFirstSearch search = new BreadFirstSearch(G.getNodeNum());
       Path res = search.bfs(G, s);
       res.packetToJson();
        //System.out.print("success!");
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