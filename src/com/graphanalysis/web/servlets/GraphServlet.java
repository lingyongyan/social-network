package com.graphanalysis.web.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.graphanalysis.web.com.ClassNameFactory;
import com.graphanalysis.web.com.ObjectPool;
import com.graphanalysis.web.com.ObjectPoolFactory;
import com.graphanalysis.web.com.ParameterObject;
import com.graphanalysis.web.com.PoolObjectFactory;
import com.graphanalysis.web.com.ServletsPreProcess;

/**
 * Servlet implementation class graphJs
 */
@WebServlet(urlPatterns = { "/graphJs", "/json/graph.json","/json/degree.json","/json/Degree.json"},
name = "GraphServlet", //servlet名称
loadOnStartup = 1 )
public class GraphServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GraphServlet() {
        super();
        // TODO Auto-generated constructor stub
        System.out.println("Servlet开始实例化");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        String[] args = ServletsPreProcess.PreProcess(request,3);
		SolutionEntry.solve(args[0], args, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

	public void init() throws ServletException { 
	   super.init(); 
	   System.out.println("初始化Servlet.。。。。。");
		Logger log = Logger.getLogger("serverlog"); 
        log.setLevel(Level.INFO);
        log.info("开始监听!");
    	ParameterObject pObj = new ParameterObject(50,5);
    	fileRootPath = this.getServletContext().getRealPath("/");
    	System.out.println(fileRootPath);
    	try {
    		objectInstance = PoolObjectFactory.getInstance();
    		classInstance = ClassNameFactory.getInstance(fileRootPath+"datasets/className");
    		objectPoolFacInstance = ObjectPoolFactory.getInstance();
    		objectPoolInstance = objectPoolFacInstance.createPool(pObj, Class.forName("com.graphanalysis.graphbase.implement.Graph"));
    		objectPoolInstance.getInstance().initPool(fileRootPath+"/datasets/datasets",fileRootPath+"/datasets/");
    		 log.info("读图成功:D");
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			 log.info("读图失败( ˇˍˇ )|||");
			e.printStackTrace();
		}
	}
	private static ObjectPoolFactory objectPoolFacInstance;
	private static ObjectPool objectPoolInstance;
	private static String fileRootPath;
	private static PoolObjectFactory objectInstance;
	private static ClassNameFactory classInstance;
}
