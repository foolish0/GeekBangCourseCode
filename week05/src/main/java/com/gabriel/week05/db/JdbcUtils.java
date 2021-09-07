package com.gabriel.week05.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lizhenjiang
 */
public class JdbcUtils {
    @Autowired
    private static final DataSource dataSource = new HikariDataSource();

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = dataSource.getConnection();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return con;
    }
}
