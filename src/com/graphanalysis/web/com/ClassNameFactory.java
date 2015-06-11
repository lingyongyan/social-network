package com.graphanalysis.web.com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ClassNameFactory {

	private static ClassNameFactory factory = null;
	private Map<String,Class<?>> classMap = new HashMap<String,Class<?>>();

	private ClassNameFactory(){
		
	}
	
	private ClassNameFactory(String fileName) {
		try {
			init(fileName);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 ***从文件中读入配置文件，建立请求的方法名称（String）与对应的处理类的映射
	 * @param fileName	配置文件名
	 * @throws FileNotFoundException
	 * @author Yan Lingyong
	 */
	private void init(String fileName) throws FileNotFoundException{
		FileInputStream fis = null;
		try {
			System.out.println("从文件中新建Class");
			fis = new FileInputStream(fileName);
			Properties props = new Properties();
			props.load(fis);
			for (String name : props.stringPropertyNames()) {
				String s = props.getProperty(name);
				String[] args = s.split(",");
				Class<?> className;
				className = Class.forName(name);
				for(int i=0;i<args.length;i++){
					this.classMap.put(args[i], className);
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static synchronized ClassNameFactory getInstance() {
		if (factory == null) {
			factory = new ClassNameFactory();
		}
		return factory;
	}
	
	public static synchronized ClassNameFactory getInstance(String fileName) {
		if (factory == null) {
			factory = new ClassNameFactory(fileName);
		}
		return factory;
	}
	
	/**
	 * 一下函数用于利用方法名得到对应的处理类
	 * */
	public Class<?> getClassFromName(String name) {
		if(this.classMap.containsKey(name)){
			Class<?> className = this.classMap.get(name);
			return className;
		}
		else
			return null;
	}
}
