
package com.graphanalysis.web.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.graphanalysis.algorithm.bfsANDdfs.BFSImpl;
import com.graphanalysis.algorithm.bfsANDdfs.BreadFirstSearch;
import com.graphanalysis.algorithm.bfsANDdfs.DFSImpl;
import com.graphanalysis.algorithm.bfsANDdfs.DepthFirstSearch;
import com.graphanalysis.algorithm.bridgedetection.BridgeDetection;
import com.graphanalysis.algorithm.bridgedetection.BridgeDetectionInterface;
import com.graphanalysis.algorithm.implement.ExecParameter;
import com.graphanalysis.algorithm.implement.ExecReturn;
import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.algorithm.primmst.PrimMST;
import com.graphanalysis.algorithm.primmst.PrimMSTImpl;
import com.graphanalysis.algorithm.randomWalk.RandomWalkGraph;
import com.graphanalysis.graphbase.implement.Graph;
import com.graphanalysis.graphbase.implement.GraphException;
import com.graphanalysis.graphbase.interfaces.GraphInterface;
import com.graphanalysis.web.com.ClassNameFactory;
import com.graphanalysis.web.com.ObjectPool;
import com.graphanalysis.web.com.PoolObjectFactory;

public class SolutionEntry {

	/**
	 * @param method	处理方法的名称
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
			method = method.toUpperCase();
			String dataSets = args[1];
			String localFile = args[2];
			Graph myGraph =  (Graph)ObjectPool.getInstance().getObject(dataSets, localFile);
			ExecParameter paras = new ExecParameter();
			paras.addParameter(myGraph);
			Object[] obParameters = null;
			boolean nondefault = true;
			if(myGraph == null)
				throw new GraphException("Graph Should Be Null!");
			switch(method){
			case "DFS":
			case "BFS":
				int id=0;
				if(args.length>3)
					id= Integer.valueOf(args[3]);
				paras.addParameter(Integer.valueOf(id));//DFS,BFS 都需要一个起点,将该参数放在参数Vector中
			case "PRIM":
				obParameters = new Object[1];
				obParameters[0] = Integer.valueOf(myGraph.getNodeNum());//上面的处理逻辑在新建一个对象的时候都需要参数nodeNumber;
				break;
			case	"BRIDGE":
				obParameters = new Object[0];//BridgeDetection不需要参数
				break;
			case "P2P":
				obParameters = new Object[1];
				paras.clear();
				GraphInterface graphInterfP2P  = myGraph;
				obParameters[0] = graphInterfP2P;//P2P需要参数GraphInterface
				break;
			case "RANDOM":
				obParameters = new Object[1];
				paras.clear();
				
				GraphInterface graphInterf = new RandomWalkGraph(myGraph.getEdgeSet());
				obParameters[0] = graphInterf;//Randomwalk需要参数GraphInterface
				int steps =5;
				if(args.length>4)
					steps= Integer.valueOf(args[4]);
				paras.addParameter(Integer.valueOf(steps));//执行时需要参数:执行步数(steps)
				break;

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
			
			if(nondefault){
				ExecReturn dealResult  = doSolve(method,obParameters,paras);
				response.getOutputStream().write(dealResult.get(0).toString().getBytes("UTF-8"));
			}
		} catch (IOException | GraphException | JSONException e ) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
