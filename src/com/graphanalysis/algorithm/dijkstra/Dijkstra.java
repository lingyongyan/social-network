package com.graphanalysis.algorithm.dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.graphanalysis.graphBase.commondefine.GraphReader;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Node;

public class Dijkstra {

	public ArrayList<Integer> exec(String fileName, int src, int dst) {
		Vector<Edge> edges = GraphReader.readFromFile(fileName, false);
		// System.out.println(edges.size());
		DijkstraGraph graph = new DijkstraGraph(edges);
		HashMap<Integer, Double> dist = new HashMap<Integer, Double>();
		HashMap<Integer, Integer> prev = new HashMap<Integer, Integer>();

		Set<Node> nodes = graph.getNodeSet();

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
		ArrayList<Integer> path = new ArrayList<Integer>();
		int tmp = dst;
		while (tmp != src) {
			path.add(tmp);
			tmp = prev.get(tmp);
		}

		path.add(src);

		ArrayList<Integer> ret = new ArrayList<Integer>();

		for (int i = 0; i < path.size(); i++) {
			ret.add(path.get(path.size() - i - 1));
			System.out.println(path.get(path.size() - i - 1));
		}

		return ret;
	}
}
