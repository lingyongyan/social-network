package com.graphanalysis.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.graphanalysis.web.com.ObjectPool;
import com.graphanalysis.web.com.ServletsPreProcess;

/**
 * Servlet implementation class DataSetServlet
 */
@WebServlet({ "/DataSetServlet", "/json/dataset.json","/json/dataSets.json"  })
public class DataSetServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataSetServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
       // String[] args = ServletsPreProcess.PreProcess(request,3);
		//String[] args = new String[1];
		//args[0]="dataset";
		//SolutionEntry.solve(args[0], args, response);
		response.setContentType("text/json; charset=UTF-8");
		JSONArray datas = ObjectPool.getInstance().packetToJson();
		response.getOutputStream().write(datas.toString().getBytes("UTF-8"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
