package com.graphanalysis.web.com;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PoolObjectFactory {
	private static PoolObjectFactory factory;

    private PoolObjectFactory() {
    }

    public static synchronized PoolObjectFactory getInstance() {

       if (factory == null) {
    	   factory = new PoolObjectFactory();
       }
       return factory;
    }

    /**
     * 根据输入的类，以及构造所需的参数构造该类的一个对象
     * **/
    public Object createObject(Class<?> clsType,Object[] args) {
        Class<?> clazz = clsType;
        Class<?>[] argsClass = new Class[args.length];   
        for (int i = 0, j = args.length; i < j; i++) {   

            argsClass[i] = args[i].getClass();   

        }
		try {
			Constructor<?> cons = clazz.getConstructor(argsClass);
			return cons.newInstance(args);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
    }
}
