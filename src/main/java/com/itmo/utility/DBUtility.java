package com.itmo.utility;

/**
 * Created by anastasia.sulyagina
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtility {
    private static Connection connection = null;
    public static Connection getConnection() {
        if (connection != null)
            return connection;
        else {
            try {
                Properties prop = new Properties();
                InputStream inputStream = new java.io.FileInputStream("application.properties");
                prop.load(inputStream);
                String driver = prop.getProperty("jdbc.drivers");
                String url = prop.getProperty("jdbc.url");
                String user = prop.getProperty("user");
                String password = prop.getProperty("password");
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
                //Statement st = connection.createStatement();
                //st.executeUpdate("create table users(login VARCHAR(20), password VARCHAR(20))");
                //st.executeUpdate("insert into users(login, password) values ('admin', 'password')");
                //st.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return connection;
        }
    }
}
