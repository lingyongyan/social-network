package com.graphanalysis.algorithm.primmst;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import org.json.JSONArray;

import com.graphanalysis.graphBase.commondefine.GraphReader;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.GraphException;
import com.graphanalysis.graphbase.implement.Path;

public class PrimMST implements PrimMSTImpl {
    private Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;
    private static String content="";
   
    public PrimMST(int node_num) {
        edgeTo = new Edge[node_num];
        distTo = new double[node_num];
        marked = new boolean[node_num];
        pq = new IndexMinPQ<Double>(node_num);
        // check optimality conditions
        //assert check(G);
    }

    // run Prim's algorithm in graph G, starting from vertex s
    public void prim(Graph G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }
    public Path prim(Graph myGraph){
    	int node_num = myGraph.getNodeNum();
        for (int v = 0; v < node_num; v++) distTo[v] = Double.POSITIVE_INFINITY;
        for (int v = 0; v < node_num; v++)      // run from each vertex to find
            if (!marked[v]) prim(myGraph, v);      // minimum spanning forest
        check(myGraph);
        Path p = new Path();
        for (Edge e : edges()) {
        	p.addPath(e);
        }
        return p;
    }

    // scan vertex v
    public void scan(Graph G, int v) {
        marked[v] = true;
        Iterator<Edge> it = G.getAdjEdgeList(v).iterator();
        while (it.hasNext()) {
        	Edge e = it.next();
            int w = e.getToID();
            if(w==v) w = e.getFromID();
            double weight = e.getWeight();
            if (marked[w]) continue;         // v-w is obsolete edge
            if (weight < distTo[w]) {
                distTo[w] = weight;
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    public Iterable<Edge> edges() {
        Queue<Edge> mst = new LinkedList<Edge>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.offer(e);
            }
        }
        return mst;
    }

    public double weight() {
        double weight = 0.0;
        for (Edge e : edges())
            weight += e.getWeight();
        return weight;
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    public boolean check(Graph G) {

        // check weight
        double totalWeight = 0.0;
        for (Edge e : edges()) {
            totalWeight += e.getWeight();
        }
        double EPSILON = 1E-12;
        if (Math.abs(totalWeight - weight()) > EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(G.getNodeNum());
        for (Edge e : edges()) {
            int v = e.getFromID(), w = e.getToID();
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : G.getEdgeSet()) {
            int v = e.getFromID(), w = e.getToID();
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges()) {

            // all edges in MST except e
            uf = new UF(G.getNodeNum());
            for (Edge f : edges()) {
                int x = f.getFromID(), y = f.getToID();
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (Edge f : G.getEdgeSet()) {
                int x = f.getFromID(), y = f.getToID();
                if (!uf.connected(x, y)) {
                    if (f.getWeight() < e.getWeight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }

    public static void main(String[] args) {
		Vector<Edge> edges = GraphReader.readFromFile("/tmp/tinyGPrimMST.txt",2);
		Graph G = new Graph(edges);
        PrimMST mst = new PrimMST(G.getNodeNum());
        mst.prim(G);
        for (Edge e : mst.edges()) {
        	content += e.getFromID()+" "+e.getToID()+"\r\n";
        }
        try {
      	   File file = new File("PrimMSTResult2.txt");
      	   if (!file.exists()) {
      	    file.createNewFile();
      	   }

      	   FileWriter fw = new FileWriter(file.getAbsoluteFile());
      	   BufferedWriter bw = new BufferedWriter(fw);
      	   bw.write(content);
      	   bw.close();
      	  } catch (IOException e) {
      	   e.printStackTrace();
      	  }     
        //System.out.println("success!");
    }

	@Override
	public int exec(String[] args) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public JSONArray exec(String fileName, int s) {
		// TODO 自动生成的方法存根
		JSONArray res = null;
		Graph myGraph = GraphReader.readGraphFromJson(fileName);
		try{
		if(myGraph == null)
			throw new GraphException("Graph Should Be Null!");
		Path bres = this.prim(myGraph);
		res  = bres.packetToJson();
		}catch(GraphException e){
			System.out.println(e);
		}
		return res;
	}

	@Override
	public JSONArray exec(Graph myGraph, int s) {
		// TODO 自动生成的方法存根
		JSONArray res = null;
		try{
		if(myGraph == null)
			throw new GraphException("Graph Should Be Null!");
		Path bres = this.prim(myGraph);
		res  = bres.packetToJson();
		}catch(GraphException e){
			System.out.println(e);
		}
		return res;
	}
}

