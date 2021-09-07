package com.gabriel.week05.db;

import com.sun.glass.ui.Application;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class JdbcDemo implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(JdbcDemo.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 原生JDBC
        jdbcTest();
        // PreparedStatement
        preparedStatementTest();
        // 批处理
        batch();
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

    /**
     * 原生JDBC实现增删改查
     */
    public static void jdbcTest() {
        Connection connection = JdbcUtils.getConnection();
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
            release(connection, statement, rs);
        }
    }


    /**
     * 使用PreparedStatement
     */
    public static void preparedStatementTest() {
        Connection connection = JdbcUtils.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            // 删
            statement = connection.prepareStatement(
                    "delete from students where id = ?"
            );
            statement.setInt(1, 1);
            statement.executeUpdate();
            // 增
            statement = connection.prepareStatement(
                    "insert into students(id, name) values ( ?,? )"
            );
            statement.setInt(1, 1);
            statement.setString(2, "Gabriel");
            statement.executeUpdate();
            // 改
            statement = connection.prepareStatement(
                    "update students set name = ? where id = ?"
            );
            statement.setString(1, "Johnson");
            statement.setInt(2, 1);
            statement.executeUpdate();
            // 查
            statement = connection.prepareStatement(
                    "select * from students"
            );
            rs = statement.executeQuery();
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
            release(connection, statement, rs);
        }
    }

    public static void batch() {
        Connection connection = JdbcUtils.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            for (int i = 10; i < 20; i++) {
                String sql = "insert into students(id, name) values ("
                        + i + ", 'name-" + i +"')";
                statement.addBatch(sql);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6、释放资源
            release(connection, statement, null);
        }
    }

    private static void release(Connection connection, Statement statement, ResultSet rs) {
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
