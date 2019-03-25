package com.xuecheng.framework.domain.cms.response;
//线程安全的单例
public class SingleTest {
    private static SingleTest s = null;
    public static SingleTest getInstence(){
        if(s==null){
            synchronized (SingleTest.class){
                if(s==null){
                    s= new SingleTest();
                }
            }
        }
        return s;
    }
}
