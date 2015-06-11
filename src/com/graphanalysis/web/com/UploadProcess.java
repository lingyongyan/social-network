package com.graphanalysis.web.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UploadProcess {
	public static boolean Process(String filePath,String dataSets,String fileName,int type) throws IOException{   
		String fileType = "none";
		String[] args = fileName.split("\\.");
		String fileSet = "userdata0";
		if(args.length>1){
			fileType = args[args.length-1];
		}
		fileSet = args[0];       
		Object newGraph = ObjectPool.getInstance().getObject(fileSet, filePath+fileName);     
		if(newGraph==null){
			return false;
		}  

		Properties prop = new Properties();// 属性集合对象  
		FileInputStream fis = new FileInputStream(filePath+dataSets);// 属性文件输入流  
		prop.load(fis);// 将属性文件流装载到Properties对象中  
		fis.close();// 关闭流 

		String comment = fileName+" "+fileType+" "+String.valueOf(type);
		prop.setProperty(fileSet, comment);  

		FileOutputStream fos = new FileOutputStream(filePath+dataSets);  
		prop.store(fos, "Copyright (c) UCAS Graph Analysis Group");// 将Properties集合保存到流中 
		fos.close();// 关闭流 
		return true;
	}
	
	public static boolean delete(String fileName){     
        File file = new File(fileName);     
        if(!file.exists()){     
            System.out.println("删除文件失败："+fileName+"文件不存在");     
            return false;     
        }else{     
            if(file.isFile()){     
                     
                return file.delete();   
            }
            return false;
        } 
	}
}
