package com.itmo.dao;

/**
 * Created by anastasia.sulyagina
 */
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.itmo.utility.DBUtility;
import org.springframework.stereotype.Service;

@Service
public class AuthorisationService {
    private Connection connection;
    public AuthorisationService() {
        connection = DBUtility.getConnection();
    }
//    public void addUser(User user) {
//        try {
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("insert into users(login,password,user_type) values (?, ?, ?)");
//            System.out.println("User:"+user.getLogin());
//            preparedStatement.setString(1, user.getPassword());
//            preparedStatement.setString(2, user.getType());
//            preparedStatement.setString(3, user.getUserPriority());
//            preparedStatement.setString(4, user.getUserStatus());
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public Map<String, String> checkUser(String login, String password) {
        try {
//            Statement st = connection.createStatement();
//            st.executeUpdate("insert into users(login, password) values ('admin', 'password')");
//            st.close();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select COUNT(*) from users where login=? and password=?");
            // Parameters start with 1
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Boolean userExists = rs.getInt(1) > 0;
            rs.close();
            Map<String, String> m = new HashMap<>();
            m.put("status", (userExists?"complete": "error"));
            if (userExists) {
                m.put("name", login);
            } else {
                m.put("message", "Invalid email and/or password. Please try again.");
            }
            return m;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Map<String, String> m = new HashMap<>();
        m.put("status", "error");
        m.put("message", "Something went wrong. Please try again.");

        return m;
    }
}