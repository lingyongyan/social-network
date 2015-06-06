package com.graphanalysis.web.com;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class ServletsPreProcess {
	ServletsPreProcess(){
	}
	public static String[] PreProcess(HttpServletRequest request,int num){
		Enumeration<String> parameterNames = request.getParameterNames();
		Vector<String> parameters = new Vector<String>();
		while (parameterNames.hasMoreElements()) {
		    parameters.add( parameterNames.nextElement());
		}
		
		String[] res = new String[num];
		String fileName = request.getParameter(parameters.get(1));
		String[] methods = fileName.split(".json");
		
		res[0] = methods[0];
		res[1] =request.getParameter(parameters.firstElement());
		res[2]  =request.getSession().getServletContext().getRealPath("/json/"+"graph.json").toString() ;//localFile Position 构造调用函数的值
		for(int i=3;i<num;i++)
			res[i] = request.getParameter(parameters.get(i-1));//id
		return res;
	}
}
