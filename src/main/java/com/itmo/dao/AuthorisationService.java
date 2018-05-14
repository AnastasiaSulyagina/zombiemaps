package com.itmo.dao;

/**
 * Created by anastasia.sulyagina
 */
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.itmo.model.User;
import com.itmo.utility.DBUtility;
import com.itmo.utility.EncryptionUtility;
import org.springframework.stereotype.Service;

@Service
public class AuthorisationService {
    private Connection connection;
    public AuthorisationService() {
        connection = DBUtility.getConnection();
    }

    private void createUsersTable() {

        try {
            Statement st = connection.createStatement();
            try {
                st.executeQuery("drop table users");
                st.executeQuery("drop sequence seq_users");

            } catch (SQLException se) {
                //Игнорировать ошибку удаления таблицы
                if (se.getErrorCode() == 942) {
                    String msg = se.getMessage();
                    System.out.println("Ошибка при удалении таблицы: " + msg);
                }
            }
            st.addBatch("create table users(id NUMBER(4) PRIMARY KEY, " +//GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1)
                    "login VARCHAR(20) UNIQUE, password VARCHAR(100), user_name VARCHAR(20), role VARCHAR(20))");
            st.addBatch("CREATE SEQUENCE seq_users START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
            st.addBatch("CREATE OR REPLACE TRIGGER trg_user_id BEFORE INSERT ON users FOR EACH ROW BEGIN :NEW.id := seq_users.NextVal; END;");

            st.addBatch("INSERT INTO users (login, password, user_name, role) " +
                    "VALUES('lam', '" + EncryptionUtility.encrypt("password_lam") + "', 'Lambert Kendal', 'user')");
            st.addBatch("INSERT INTO users (login, password, user_name, role) " +
                    "VALUES('morgan', '"+ EncryptionUtility.encrypt("password_morgan") +"', 'Morgan Jones', 'user')");

            st.addBatch("INSERT INTO users (login, password, user_name, role) " +
                    "VALUES('shane', '"+ EncryptionUtility.encrypt("password_shane") +"', 'Shane Walsh', 'rick')");
            st.addBatch("INSERT INTO users (login, password, user_name, role) " +
                    "VALUES('rick', '"+ EncryptionUtility.encrypt("password_rick") +"', 'Rick Grimes', 'rick')");
            st.executeBatch();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> addUser(String login, String password, String name) {
        Map<String, String> m = new HashMap<>();
        try {
            createUsersTable();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into users(login,password,user_name,role) values (?, ?, ?, ?)");
            System.out.println("User: "+ login);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, EncryptionUtility.encrypt(password));
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, "user");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1)
                m.put("message", "Login already exists");
            else
                m.put("message", "Something went wrong");
            e.printStackTrace();
        }
        return m;
    }

    public Map<String, String> checkUser(String login, String password) {
        Map<String, String> m = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from users where login=? and password=?");

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, EncryptionUtility.encrypt(password));
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            User user = new User(rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("user_name"),
                    rs.getString("role"));
            rs.close();
            m.put("status", "complete");
            m.put("name", user.getName());
            m.put("role", user.getRole());
            m.put("message", "Log in successful");
            return m;
        } catch (SQLException e) {
            e.printStackTrace();
            m.put("status", "error");
            m.put("message", "Invalid email and/or password. Please try again.");
        }
        return m;
    }

    public String getUserName(String login) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select user_name from users where login=?");

            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            String name = rs.getString("user_name");
            rs.close();
            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeUser(String login, String password) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from users where login=? and password=?");

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, EncryptionUtility.encrypt(password));
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}