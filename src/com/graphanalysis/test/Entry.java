/*FileName:Entry.java
 * Date:2015,05.13
 * Author:Yan Lingyong
 * Description: the entry of main function
 * */
package com.graphanalysis.test;
import java.util.Vector;

import com.graphanalysis.algorithm.bridgedetection.Bridge;
import com.graphanalysis.algorithm.bridgedetection.BridgeDetection;
import com.graphanalysis.algorithm.bridgedetection.BridgeDetectionInterface;
import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphBase.commondefine.GraphReader;
import com.graphanalysis.graphbase.implement.Edge;

public class Entry {
	public static void main(String[] args){
		try{
			long startTime=System.currentTimeMillis();
			//myGraph.printGraph();
			//System.out.println(myGraph.getType());
			Bridge br = new Bridge();
			BridgeDetectionInterface algInter;
			algInter = new BridgeDetection();
			int i = algInter.exec(br,"/tmp/facebook_combined.txt");
			long endTime=System.currentTimeMillis(); //获取结束时间
			System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
			if(br!=null)
				br.printBridge();
			//br.packToJSON();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}
