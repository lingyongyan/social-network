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
	/**
	 * @param fileName
	 * @param gtype  gtype使用说明如下，如果gtype的二进制表示的最低位为1，则是有向图，否则为无向图；二进制表示的第二位为1是有权图
	 * @return
	 * 
	 */
	public static Vector<Edge> readFromFile(String fileName,int gtype){
		File file = new File(fileName);
		BufferedReader reader = null;
		int len = (gtype&1)!=0?3:2;
		boolean bodirect = (gtype&1)!=0?true:false;//表明图是否有向，true表示有向，如果是无向图，则默认将读入的边在逆向写一次
		boolean boweight = (gtype&2)!=0?true:false;//表明图是否有权，true表示有权，如果是有权图，则继续读入第三列的值
		try{
			System.out.println("从文件中新建graph");
			reader = new BufferedReader(new FileReader(file));
			String tempStr = null;
			Vector<Edge> edges = new Vector<Edge>();
			while((tempStr=reader.readLine())!=null){
				String[] arr = tempStr.split(" ");
				int from = Integer.parseInt(arr[0]);
				int to = Integer.parseInt(arr[1]);
				double weight = 1;
				if(boweight)//如果是有权图
					weight = Double.parseDouble(arr[2]);
				Edge edge = new Edge(from,to,weight);
				edges.add(edge);
				if(!bodirect){//如果是无向图
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
