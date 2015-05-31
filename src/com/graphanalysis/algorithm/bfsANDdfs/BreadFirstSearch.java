package com.graphanalysis.algorithm.bfsANDdfs;
import java.io.*;

import com.graphanalysis.algorithm.bfsANDdfs.invokClass.Graph;
import com.graphanalysis.algorithm.bfsANDdfs.invokClass.In;
import com.graphanalysis.algorithm.bfsANDdfs.invokClass.Queue;
public class BreadFirstSearch {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int count;           // number of vertices connected to s
    String content="";
    private static final int INFINITY = Integer.MAX_VALUE;
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path

    public BreadFirstSearch(Graph G, int s) {
    	 marked = new boolean[G.V()];
         distTo = new int[G.V()];
         edgeTo = new int[G.V()];
         bfs(G, s);
    }

    // depth first search from v
    public void bfs(Graph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                    content=content + v+ " " + w + "\r\n";
                }
            }
        }
        try {
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
     	  }     
     }

    public boolean marked(int v) {
        return marked[v];
    }

    public int count() {
        return count;
    }

    public static void main(String[] args) {
        //In in = new In(args[0]);
    	In in = new In("tinyG.txt");
        Graph G = new Graph(in);
        //int s = Integer.parseInt(args[1]);
        int s = Integer.parseInt("1");
        BreadFirstSearch search = new BreadFirstSearch(G, s);
        //System.out.print("success!");
    }
}