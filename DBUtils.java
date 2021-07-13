package com.banyuan.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {

    /**
     * 映射单个值到JavaBean中
     * 结果集的自动映射
     * @param rs
     * @param clazz
     * @param <E>
     * @return
     */
    //E代表泛型，clazz代表需要传进的是类的自解码（如：photoBean.class）
    public static <E> E getBean(ResultSet rs,Class<E> clazz){
        E e = null;
        try {
            if (rs.next()) {
                //创建一个javaBean对象
                e = clazz.newInstance();
                //javabean对象创建完毕之后,我们需要从rs中获取数据, 放到JavaBean对象中
                //获取结果集中的表头：ResultSetMetaData类
                ResultSetMetaData metaData = rs.getMetaData();
                //获取表头的数量
                int count = metaData.getColumnCount();
                //select userName from users;
                //select userName as un from users;
                //select * from users; id , userName ,password
                //循环映射
                for (int i = 1; i <=count ; i++) {
                    //获取到表头名称,根据表头名称去JavaBean中找对应名称的成员变量
                    //getColumnLabel获取的是field的SQL AS的值。
                    //getColumnName获取的是sql语句中field的原始名字。
                    //基本需要使用别名，用getColumnLabel方法
                    String columnName = metaData.getColumnLabel(i); //返回的结果就id
                    //根据遍历出来的表头的名称获取表头对应类的数据
                    Object obj = rs.getObject(i);
                    //根据遍历出来的字段名获取相同名称的JavaBean中的成员变量，由于是private需要使用getDeclareField方法
                    Field field = clazz.getDeclaredField(columnName);
                    //开启允许，针对private变量
                    field.setAccessible(true);
                    //将获取到的数据放到成员变量上
                    field.set(e,obj);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException instantiationException) {
            instantiationException.printStackTrace();
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
        return e;
    }

    /**
     * 多行映射数据库值到JavaBean中
     * @param rs
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> List<E> getBeanList(ResultSet rs, Class<E> clazz){
        List<E> list = new ArrayList<>();
        try {
            while (rs.next()) {
                //创建一个javaBean对象
                E e = clazz.newInstance();
                //javabean对象创建完毕之后,我们需要从rs中获取数据, 放到JavaBean对象中
                //获取结果集中的表头
                ResultSetMetaData metaData = rs.getMetaData();
                //获取表头的数量
                int count = metaData.getColumnCount();
                //select userName from users;
                //select userName as un from users;
                //select * from users; id , userName ,password
                for (int i = 1; i <=count ; i++) {
                    //获取到表头名称,根据表头名称去JavaBean中找对应名称的成员变量
                    String columnName = metaData.getColumnLabel(i); //返回的结果就id
                    //根据遍历出来的表头的名称获取表头对应类的数据
                    Object obj = rs.getObject(i);
                    //根据遍历出来的字段名获取相同名称的JavaBean中的成员变量
                    Field field = clazz.getDeclaredField(columnName);
                    //开启允许
                    field.setAccessible(true);
                    //将获取到的数据放到成员变量上
                    field.set(e,obj);
                }
                list.add(e);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException instantiationException) {
            instantiationException.printStackTrace();
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        } catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
        return list;
    }
}
