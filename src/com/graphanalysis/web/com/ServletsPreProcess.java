/*FileName:Bridge.java
 * Date:2015.06.03
 * Author:Yan Lingyong
 * Description: Class to solve pre-process user request(get users input parameters)
 * */
package com.graphanalysis.web.com;

import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lingyong Yan
 *
 */
public class ServletsPreProcess {
	ServletsPreProcess(){
	}
	/**
	 * @param request	用户请求
	 * @param num	需要解析出的参数数目
	 * @return String[] 参数数组
	 */
	public static String[] PreProcess(HttpServletRequest request,int num){
		String deal = request.getProtocol()+"Request:";
		Logger log = Logger.getLogger("serverlog"); 
        log.setLevel(Level.INFO);//建立日志
        
		Enumeration<String> parameterNames = request.getParameterNames();//读取用户参数名称
		Vector<String> parameters = new Vector<String>();
		while (parameterNames.hasMoreElements()) {
		    parameters.add( parameterNames.nextElement());
		}
		
		String[] res = new String[num<(parameters.size()+1)?num:(parameters.size()+1)];
		String fileName = request.getParameter(parameters.get(1));//第二个参数是用户请求的json文件全名
		String[] methods = fileName.split(".json");//取出上述文件名（去除json，定义为用户请求的处理方法，如DFS，BFS等
		
		res[0] = methods[0];//处理过的第一个参数是方法名
		res[1] =request.getParameter(parameters.firstElement());//处理过的第二个参数是用户请求的进行处理的数据集
		res[2]  =request.getSession().getServletContext().getRealPath("/json/"+"graph.json").toString() ;//若找不到对应的graph对象则读入这个图
		for(int i=3;i<num && i<parameters.size()+1;i++)
			res[i] = request.getParameter(parameters.get(i-1));//id,randomwalk steps等参数
		
		//写日志
		log.info(deal+res[0]);
		log.info(deal+res[1]);
		return res;
	}
}
