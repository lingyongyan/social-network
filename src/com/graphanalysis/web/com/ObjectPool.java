/**
 * 
 */
package com.graphanalysis.web.com;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.graphanalysis.graphBase.commondefine.GraphReader;
import com.graphanalysis.graphBase.commondefine.GraphReaderData;
import com.graphanalysis.graphbase.implement.Edge;
import com.graphanalysis.graphbase.implement.Graph;

/**
 * @author Yan Lingyong
 *
 */
public class ObjectPool {
	private ParameterObject paraObj; // 该对象池的属性参数对象
	private Class<?> clsType = null; // 该对象池中所存放对象的类型
	private int currentNum = 0; // 该对象池当前已创建的对象数目
	private Map<String, Object> objectPool = new HashMap<String, Object>();//管理对象的结构
	private static ObjectPool factory = null;

	public static ObjectPool getInstance(){
		return factory;
	}

	public ObjectPool(ParameterObject paraObj, Class<?> clsType) {
		if(factory!=null)
			return;
		this.paraObj = paraObj;
		this.clsType = clsType;
		factory = this;
	}

	private void addFromFile(String name,String filePath,int graphType){
		PoolObjectFactory objFactory = PoolObjectFactory.getInstance();
		Object objs[]= new Object[3];

		GraphReaderData gData = GraphReader.readGraphFromFile(filePath,graphType);
		boolean directed = (graphType & 1)>0?true:false;
		objs[0] = gData.edges;
		objs[1] = gData.nodes;
		objs[2] = directed;
		try {
			objectPool.put(name, objFactory.createObject(clsType,objs));
		} catch (NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		this.currentNum++;
	}

	//初始化对象池
	public void initPool(String fileName,String fileP) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			Properties props = new Properties();
			props.load(fis);
			for (String name : props.stringPropertyNames()) {
				String s = props.getProperty(name);
				String[] args = s.split(" ");
				String filePath = fileP+args[0];
				//String fileType = args[1];
				int paraMore = Integer.valueOf(args[2]);
				addFromFile(name,filePath,paraMore);
			}
		} 
		catch (IOException ex) {
			Logger.getLogger(ObjectPool.class.getName()).log(Level.SEVERE, null, ex);
		}  catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	//根据名字找对象
	public Object getObject(String name,String filePath){
		if(objectPool.containsKey(name))
			return objectPool.get(name);
		else if(this.currentNum<paraObj.getMaxCount()){
			addFromFile(name,filePath,0);
			return objectPool.get(name);
		}
		else
			return null;
	}

	public static void main(String[] args) throws ClassNotFoundException{
		//a.properties
		ParameterObject pObj = new ParameterObject(50,5);
		ObjectPool ob = new ObjectPool(pObj,Class.forName("com.graphanalysis.graphbase.implement.Graph"));
		ob.initPool("./WebContent/datasets/datasets","./WebContent/datasets/");
		ob.getObject("userdata0","/");
		System.out.println(ob.getObject("userdata0","/")+"=========");
		Graph o = (Graph)ob.getObject("userdata0","/");
		System.out.println(o.getNodeNum());

	}
}
