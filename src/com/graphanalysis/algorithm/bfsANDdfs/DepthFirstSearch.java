package com.graphanalysis.algorithm.bfsANDdfs;
import java.io.*;

import com.graphanalysis.algorithm.bfsANDdfs.invokClass.Graph;
import com.graphanalysis.algorithm.bfsANDdfs.invokClass.In;

public class DepthFirstSearch {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int count;           // number of vertices connected to s
    File file = new File("dfsResult");
    String content="";
    public DepthFirstSearch(Graph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // depth first search from v
    public void dfs(Graph G, int v) {
        count++;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
            		   content =content + v + " "+ w + "\r\n";
                       dfs(G, w);
            	}
            	//System.out.print(v + " "+ w + "\n");
            }
        try {
        	   File file = new File("dfsResult.txt");
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
        int s = Integer.parseInt("1");
        DepthFirstSearch search = new DepthFirstSearch(G, s);
        //System.out.print("success!");
    }
}

