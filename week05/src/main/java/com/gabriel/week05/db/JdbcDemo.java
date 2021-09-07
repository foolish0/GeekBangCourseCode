package com.gabriel.week05.db;

import java.sql.*;

public class JdbcDemo {
    public static void main(String[] args) {
        jdbcTest();
    }

    private static Connection getConnection() {
        String driver = "com.mysql.cj.jdbc.Driver";
        Connection con = null;
        try {
            // 1、加载驱动程序
            Class.forName(driver).newInstance();
            // 2、通过DriverManager建立数据库连接Driver驱动
            String url = "jdbc:mysql://127.0.0.1:3306/practice";
            String user = "tempo";
            String pwd = "tempo123";
            con = DriverManager.getConnection(url, user, pwd);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void jdbcTest() {
        Connection connection = getConnection();
        Statement statement = null;
        ResultSet rs = null;
        try {
            // 3、使用Connection获取statement
            statement = connection.createStatement();
            // 4、使用statement执行sql
            // 删
            statement.executeUpdate(
                    "delete from students where id = 1"
            );
            // 增
            statement.executeUpdate(
                    "insert into students(id, name) values ( 1, 'Gabriel' ) "
            );
            // 改
            statement.executeUpdate(
                    "update students set name = 'Johnson' where id = 1"
            );
            // 查
            rs = statement.executeQuery(
                    "select * from students"
            );
            // 5、遍历结果
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("[Student-" + id + ": " + name + "]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6、释放资源
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                connection = null;
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                statement = null;
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                rs = null;
            }
        }
    }
}
