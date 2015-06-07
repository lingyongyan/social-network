package com.graphanalysis.web.com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UploadProcess {
	public static void Process(String filePath,String dataSets,String fileName,int type) throws IOException{
		 Properties prop = new Properties();// 属性集合对象  
	        FileInputStream fis = new FileInputStream(filePath+dataSets);// 属性文件输入流  
	        prop.load(fis);// 将属性文件流装载到Properties对象中  
	        fis.close();// 关闭流  

	        String fileType = "none";
			String[] args = fileName.split("\\.");
			String fileSet = "userdata0";
	        if(args.length>1){
	        	fileType = args[args.length-1];
	        }
        	fileSet = args[0];
	        String comment = fileSet+" "+fileType+" "+String.valueOf(type);
	        // 添加一个新的属性studio  
	        prop.setProperty(fileName, comment);  
	        // 文件输出流  
	        FileOutputStream fos = new FileOutputStream(filePath+dataSets);  
	        // 将Properties集合保存到流中  
	        prop.store(fos, "Copyright (c) YanLingyong");  
	        fos.close();// 关闭流  
	}
}
