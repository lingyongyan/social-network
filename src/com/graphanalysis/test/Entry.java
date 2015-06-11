/*FileName:Entry.java
 * Date:2015,05.13
 * Author:Yan Lingyong
 * Description: the entry of main function
 * */
package com.graphanalysis.test;

public class Entry {
	public static void main(String[] args){
		try{
			//Bridge br = new Bridge();
			//BridgeDetectionInterface algInter;
			//algInter = new BridgeDetection();
			//int i = algInter.exec(br,"/tmp/facebook_combined.txt");

			//if(br!=null)
				//br.printBridge();
			//br.packetToJSON();
/*			Vector<Edge> edges = GraphReader.readFromFile("/tmp/facebook_combined.txt", 0);
			Graph myGraph =  new Graph(edges);
			myGraph.writeToJson("./WebContent/json/graph3.json");*/
/*			Graph myGraph = GraphReader.readGraphFromJson("./WebContent/json/graphL.json");
			AlgorithmInterface algInter;
			algInter = new BridgeDetection();
			Bridge br= algInter.exec(myGraph);

			if(br!=null)
				br.printBridge();*/
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}
