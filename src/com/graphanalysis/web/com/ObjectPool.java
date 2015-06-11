/**
 * 
 */
package com.graphanalysis.web.com;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;

import com.graphanalysis.graphbase.commondefine.GraphReader;
import com.graphanalysis.graphbase.commondefine.GraphReaderData;
import com.graphanalysis.graphbase.implement.Graph;

/**
 * @author Yan Lingyong
 *存放处理所需的某些对象
 *在本应用中，由于默认会有一些数据集，在这里会新建一些Graph对象，在启动的时候就建立，在处理的时候直接从这里获取该对象
 */
public class ObjectPool {
	private ParameterObject paraObj; // 该对象池的属性参数对象
	private Class<?> clsType = null; // 该对象池中所存放对象的类型
	private int currentNum = 0; // 该对象池当前已创建的对象数目
	private Map<String, Object> objectPool = new HashMap<String, Object>();//管理对象的结构
	private static ObjectPool factory = null;
	private Vector<String> dataSets = new  Vector<String>();

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
	//从文件中读入图，并建立相应的对象
	private boolean addFromFile(String name,String filePath,int graphType){
		PoolObjectFactory objFactory = PoolObjectFactory.getInstance();
		Object objs[]= new Object[4];

		GraphReaderData gData = GraphReader.readGraphFromFile(filePath,graphType);
		if(gData==null || gData.getNodeSet().size()==0){
			System.out.println(gData.getError());
			return false;
		}
		objs[0] = gData.getEdges();
		objs[1] = gData.getNodeSet();
		objs[2] = gData.type();
		objs[3] = gData.weight();
		objectPool.put(name, objFactory.createObject(clsType,objs));
		dataSets.add(name);
		this.currentNum++;
		return true;
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
				String[] args = s.split("\\s+");
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
			if(addFromFile(name,filePath,0))
				return objectPool.get(name);
			else
				return null;
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
	
	public JSONArray packetToJson(){
		JSONArray ret = new JSONArray();
		Iterator<String> it = this.dataSets.iterator();
		while(it.hasNext()){
			String data = it.next();
			ret.put(data);
		}
		return ret;
	}
}
