package com.graphanalysis.algorithm.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.graphbase.commondefine.GraphReader;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Node;
import com.graphanalysis.graphbase.implement.Path;

public class Dijkstra {

	public JSONObject exec(String fileName, int startPoint) {
		JSONObject jo = new JSONObject();
		Vector<Edge> edges = GraphReader.readFromFile(fileName, 2);
		Set<Integer> vSet = new HashSet<Integer>();
		for (int i = 0; i < edges.size(); i++) {
			vSet.add(edges.get(i).getFromID());
			vSet.add(edges.get(i).getToID());
		}
		for (int i = 0; i < vSet.size(); i++) {
			if (i != startPoint) {
				int src = startPoint;
				int dst = i;
				Path path = exec(fileName, src, dst);
				try {
					jo.append(i+"", path);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return jo;
	}

	public Path exec(String fileName, int src, int dst) {
		Vector<Edge> edges = GraphReader.readFromFile(fileName, 2);
		DijkstraGraph graph = new DijkstraGraph(edges);
		HashMap<Integer, Double> dist = new HashMap<Integer, Double>();
		HashMap<Integer, Integer> prev = new HashMap<Integer, Integer>();

		Vector<Node> nodes = graph.getNodeSet();

		Iterator iter = nodes.iterator();
		while (iter.hasNext()) {
			Node node = (Node) iter.next();
			dist.put(node.getID(), (double) 9999999);
			prev.put(node.getID(), 9999999);
		}

		dist.put(src, (double) 0);
		Set<Integer> vSet = new HashSet<Integer>();

		while (true) {
			iter = dist.entrySet().iterator();
			double minVal = 99999999;
			int keyIdx = -1;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Integer key = (Integer) entry.getKey();
				double val = (double) entry.getValue();
				if (val < minVal) {
					minVal = val;
					keyIdx = key;
				}
			}
			// System.out.println(minVal);
			if (keyIdx == dst) {
				break;
			}

			vSet.add(keyIdx);

			double du = minVal;

			dist.remove(keyIdx);

			for (int i = 0; i < graph.getWeiAdjList().get(keyIdx).size(); i++) {
				int v = graph.getWeiAdjList().get(keyIdx).get(i).getDst();
				double mtr = graph.getWeiAdjList().get(keyIdx).get(i).getDist();
				if (!vSet.contains(v) && dist.get(v) > du + mtr) {
					dist.put(v, du + mtr);
					prev.put(v, keyIdx);
				}
			}
		}

		// Print path
		Path path = new Path();
		int tmp = dst;
		int tmp1 = dst;
		Vector<Edge> es = graph.getEdgeSet();
		while (tmp != src) {
			tmp1 = prev.get(tmp);
			// An edge from tmp1 to tmp
			for (int k = 0; k < es.size(); k++) {
				if (es.get(k).getFromID() == tmp1 && es.get(k).getToID() == tmp) {
					path.addPath(es.get(k));
				}
			}
			tmp = tmp1;
		}
		return path;
	}
}
