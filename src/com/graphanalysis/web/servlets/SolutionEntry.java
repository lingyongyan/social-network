/*FileName:Bridge.java
 * Date:2015.06.01
 * Author:Yan Lingyong
 * Description: Class to solve users request
 * */
package com.graphanalysis.web.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.algorithm.implement.ExecParameter;
import com.graphanalysis.algorithm.implement.ExecReturn;
import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.web.com.ClassNameFactory;
import com.graphanalysis.web.com.ObjectPool;
import com.graphanalysis.web.com.PoolObjectFactory;

public class SolutionEntry {
	/**
	 * @param method	处理方法的名称
	 * @param args	用户请求处理过得到的参数
	 * @param response	
	 */
	public static void solve(String method, String[] args,HttpServletResponse response) {//处理函数入口

		if(method ==null)
			return;
		response.setContentType("text/json; charset=UTF-8");
		try {
			method = method.toUpperCase();//将方法名全部转成大写
			String dataSets= args[1];//获取数据集名称
			String	localFile=args[2];//如果找不到数据集，则根据这个地址读入新图
			Graph myGraph =  (Graph)ObjectPool.getInstance().getObject(dataSets, localFile);//从池中得到对应的graph类
			ExecParameter paras = new ExecParameter();//用于保存执行时的参数
			paras.addParameter(myGraph);
			Object[] obParameters = null;//在新建处理类的对象的时候所需要的构造函数参数
			boolean nondefault = true;
			int id=0;
			if(args.length>3)
				id= Integer.valueOf(args[3]);
			
			if(myGraph == null){
				response.sendError(HttpServletResponse.SC_NO_CONTENT, "WE DON'T HAVE THIS DATASET");
				return;
			}
			
switch(method){
			case "DFS":
			case "BFS":
				paras.addParameter(Integer.valueOf(id));//DFS,BFS 都需要一个起点，如果用户不输入则从0开始
			case "PRIM":
				obParameters = new Object[1];
				obParameters[0] = Integer.valueOf(myGraph.getNodeNum());//DFS,BFS,PRIM对应的处理类在新建一个对象的时候都需要参数nodeNumber;
				break;
			case	"BRIDGE":
				obParameters = new Object[0];//BridgeDetection不需要参数
				break;
			case "P2P":
				obParameters = new Object[1];
				paras.clear();
				obParameters[0] = myGraph;//P2P建立新对象的时候需要参数GraphInterface
				break;
			case "RANDOM":
				obParameters = new Object[1];
				paras.clear();				
				//GraphInterface graphInterf = new RandomWalkGraph(myGraph.getEdgeSet());
				obParameters[0] = myGraph;//Randomwalk建立新对象的时候需要参数GraphInterface
				int steps =8;
				if(args.length>4)
					steps= Integer.valueOf(args[4]);
				paras.addParameter(Integer.valueOf(id));//Randomwalk需要一个起点
				paras.addParameter(Integer.valueOf(steps));//Randomwalk执行时需要参数:执行步数(steps)
				break;
			case "DIJKSTRA":
				obParameters = new Object[0];
				paras.addParameter(Integer.valueOf(id));
				break;
			case	"FORDFULKERSON":
			case "MAXFLOW":
				obParameters = new Object[0];
				paras.addParameter(Integer.valueOf(id));
				int endID =0;
				if(args.length>4)
					endID= Integer.valueOf(args[4]);
				paras.addParameter(Integer.valueOf(endID));
				break;
/*如果是获取图信息以及相应的度信息，则直接从graph中获取
 * */
			case "GRAPH":
				JSONObject graphJson =  myGraph.packToJson();
				response.getOutputStream().write(graphJson.toString().getBytes("UTF-8"));
				return;

			case "DEGREE":
				JSONArray degreeJson =  myGraph.getDegreeJson();
				response.getOutputStream().write(degreeJson.toString().getBytes("UTF-8"));
				return;
			default: nondefault = false;break;
			}
			
/*
 * 如果是需要处理请求的则执行doSolve
 **/
			if(nondefault){
				ExecReturn dealResult  = doSolve(method,obParameters,paras);
				response.getOutputStream().write(dealResult.get(0).toString().getBytes("UTF-8"));
			}
		} catch (IOException | JSONException e ) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	

	/**
	 * @param method	处理请求方法的名称
	 * @param obParameters	新建处理请求对象所需的参数
	 * @param paras	对象处理请求(exec()函数)所需的参数
	 * @return
	 */
	private static ExecReturn doSolve(String method,Object[] obParameters,ExecParameter paras){
		ExecReturn res = null;
		Class<?> className = ClassNameFactory.getInstance().getClassFromName(method);//查找对应的class
		AlgorithmInterface algorithm = (AlgorithmInterface) PoolObjectFactory.getInstance().createObject(className, obParameters);//用对应的class建立相应的对象
		res =algorithm.exec(paras);//执行对象的方法
		return res;
	}
}
