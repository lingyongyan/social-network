package com.graphanalysis.web.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.graphanalysis.web.com.ObjectPool;
import com.graphanalysis.web.com.ObjectPoolFactory;
import com.graphanalysis.web.com.ParameterObject;
import com.graphanalysis.web.com.ServletsPreProcess;

/**
 * Servlet implementation class graphJs
 */
@WebServlet({ "/graphJs", "/json/graph.json" })
public class graphJs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public graphJs() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String deal = request.getProtocol()+"Request:";
		Logger log = Logger.getLogger("serverlog"); 
        log.setLevel(Level.INFO);
        
        String[] args = ServletsPreProcess.PreProcess(request,3);
		log.info(deal+args[0]);	
		log.info(deal+args[1]);
		SolutionEntry.solve(args[0], args, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}
	
	public void init(){
		System.out.println("开始^_^");
    	ParameterObject pObj = new ParameterObject(50,5);
    	fileRootPath = this.getServletContext().getRealPath("/");
    	try {
    		objectPoolFacInstance = ObjectPoolFactory.getInstance();
    		objectPoolInstance = objectPoolFacInstance.createPool(pObj, Class.forName("com.graphanalysis.graphbase.implement.Graph"));
    		objectPoolInstance.getInstance().initPool(fileRootPath+"/datasets/datasets",fileRootPath+"/datasets/");
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	private static ObjectPoolFactory objectPoolFacInstance;
	private static ObjectPool objectPoolInstance;
	private static String fileRootPath;
}
