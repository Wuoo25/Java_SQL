package com.banyuan.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class MyDataSource {

    private LinkedList<Connection> list = new LinkedList<>();
    private Connection cn2 = null;
    public MyDataSource(String driverClassName, String url, String userName, String password) {
        try {
            Class.forName(driverClassName);
            for (int i = 0; i < 8; i++) {
                //这种方式获取的连接 当用户调用close方法的时候, 会直接关闭
                //这个对象是原生的Connection对象, 我们可以根据这个对象生成一个代理对象,
                //其他方法都正常使用,只有close方法被改写掉
                //改写原有方法可以使用动态代理对象
                Connection cn = DriverManager.getConnection(url, userName, password);
                //cn2 是一个代理对象 cn.getClass :获取当前对象的类的字节码 getClassLoader():获取当前类的类加载器
                //newProxyInstance三个参数 : 类加载器  原始类的接口
                //原始对象的类实现了哪些接口, 代理对象也会实现哪些接口
                cn2 = (Connection) Proxy.newProxyInstance(cn.getClass().getClassLoader(),
                        cn.getClass().getInterfaces(),
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                Object obj = null;
                                if (method.getName().equals("close")) {
                                    System.out.println("还回到数据库连接池中了");
                                    list.add(cn2);
                                }else{
                                    //调用原始方法
                                     obj = method.invoke(cn, args);
                                }
                                return obj;
                            }
                        });
                list.add(cn2);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection getConnection() {

        return list.poll();
    }

}
