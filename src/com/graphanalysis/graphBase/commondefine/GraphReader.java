package com.graphanalysis.graphBase.commondefine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.interfaces.GraphReaderInterface;

public class GraphReader{
	public static Vector<Edge> readFromFile(String fileName,boolean undirected){
		File file = new File(fileName);
		BufferedReader reader = null;
		int len = undirected?2:3;
		try{
			System.out.println("从文件中新建graph");
			reader = new BufferedReader(new FileReader(file));
			String tempStr = null;
			Vector<Edge> edges = new Vector<Edge>();
			while((tempStr=reader.readLine())!=null){
				String[] arr = tempStr.split(" ");
				if(arr.length!=len)
					continue;
				int from = Integer.parseInt(arr[0]);
				int to = Integer.parseInt(arr[1]);
				double weight = 1;
				if(!undirected)
					weight = Double.parseDouble(arr[2]);
				Edge edge = new Edge(from,to,weight);
				edges.add(edge);
				if(undirected){
					Edge edge2 = new Edge(to,from,weight);
					edges.add(edge2);
				}
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
		return null;
	}
}
