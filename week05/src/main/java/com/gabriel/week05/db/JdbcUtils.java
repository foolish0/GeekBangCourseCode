package com.gabriel.week05.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author lizhenjiang
 */
public class JdbcUtils {
    private static HikariDataSource dataSource = null;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/practice");
        config.setUsername("tempo");
        config.setPassword("tempo123");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "300");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = dataSource.getConnection();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return con;
    }

    public static void release(Connection connection, Statement statement, ResultSet rs) {
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
