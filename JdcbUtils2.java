package com.banyuan.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdcbUtils2 {
    private static MyDataSource dataSource;
    static{
        dataSource = new MyDataSource("com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/album","root","hanchun123");
    }
    //获取数据库连接
    public static Connection getConnecton() {

        return dataSource.getConnection();
    }

    //关闭数据库连接
    public static void release(PreparedStatement ps , Connection cn){

        release(null,ps,cn);
    }

    public static void release(ResultSet rs , PreparedStatement ps , Connection cn){

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (cn != null) {
            try {
                cn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
