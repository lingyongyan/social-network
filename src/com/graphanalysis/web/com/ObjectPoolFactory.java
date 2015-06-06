package com.graphanalysis.web.com;

public class ObjectPoolFactory {
	   

    /**

     * 饿汉式单例类

     */

    private static final ObjectPoolFactory factory = new ObjectPoolFactory();
    ObjectPool objPool = null;

    /**

     * 私有默认的构造子

     */

    private ObjectPoolFactory() {
    }

   

    /**

     * 静态工厂方法

    * @return  池工厂

     */

    public static ObjectPoolFactory getInstance() {
       return factory;
    }

    /**

     *

     * @param paraObj  对象池参数对象

     * @param clsType   所创建对象类型

     * @return   对象池

     */

    public ObjectPool createPool(ParameterObject paraObj, Class<?> clsType) {
       return new ObjectPool(paraObj, clsType);
    }
}
