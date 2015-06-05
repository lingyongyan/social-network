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

    public Object createObject(Class clsType,Object[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class clazz = clsType;
        Class[] argsClass = new Class[args.length];   

        for (int i = 0, j = args.length; i < j; i++) {   

            argsClass[i] = args[i].getClass();   

        }
        Constructor cons = clazz.getConstructor(argsClass);
        return cons.newInstance(args);
    }
}
