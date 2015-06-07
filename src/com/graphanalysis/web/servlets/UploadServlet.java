package com.graphanalysis.web.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.graphanalysis.web.com.UploadProcess;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet({ "/UploadServlet", "/upload" })
@MultipartConfig(
		location = "/home/young/tmp/", 
		maxFileSize = 1024L * 1024L, // 每一个文件的最大值
		maxRequestSize = 1024L * 1024L * 10L // 一次上传最大值，若每次只能上传一个文件，则设置maxRequestSize意义不大
)
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("upLoad");
		Enumeration<String> parameterNames = request.getParameterNames();//读取用户参数名称
		Vector<String> parameters = new Vector<String>();
		while (parameterNames.hasMoreElements()) {
		    parameters.add( parameterNames.nextElement());
		}
		if(parameters.size()!=1){
			response.sendError(response.SC_NO_CONTENT, "请正确选择上传文件");
			return;
		}
		String fileName = request.getParameter(parameters.firstElement());
		//request.setCharacterEncoding("UTF-8");
		//Part part = request.getPart("txt");
		//Collection<Part> parts = request.getParts();
		String filePath = GraphServlet.getLocation()+"/datasets/";
		//part.write(filePath);
/*		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream())); 
		   StringBuffer buffer = new StringBuffer(); 
		   String line = ""; 
		   while ((line = in.readLine()) != null){ 
		     buffer.append(line); 
		   } 
		   String result = buffer.toString();*/
		BufferedReader in = request.getReader();
        FileWriter fw = new FileWriter(filePath+fileName);
        PrintWriter out = new PrintWriter(fw);  
		String line = ""; 
		while ((line = in.readLine()) != null){ 
		    out.write(line);
		    out.println();
	 }   
        fw.close();  
        out.close(); 
        
        UploadProcess.Process(filePath,"datasets",fileName,0);
        System.out.println("写入成功");
		response.setStatus(HttpServletResponse.SC_OK);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
