package com.graphanalysis.web.com;

public class PoolObject {
    Object objection = null;// 对象     
    boolean busy = false; // 此对象是否正在使用的标志，默认没有正在使用 

    // 构造函数，根据一个 Object 构告一个 PooledObject 对象     
    public PoolObject(Object objection) {
        this.objection = objection;     
    }     

    // 返回此对象中的对象     
    public Object getObject() {     
        return objection;     
    }     

    // 设置此对象 
    public void setObject(Object objection) {     
        this.objection = objection;     

    }     

    // 获得对象对象是否忙     
    public boolean isBusy() {     
        return busy;     
    }     

    // 设置对象的对象正在忙     
    public void setBusy(boolean busy) {     
        this.busy = busy;     
    } 
}
