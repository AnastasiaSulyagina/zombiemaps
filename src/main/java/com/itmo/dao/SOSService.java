package com.itmo.dao;

import com.itmo.model.SOS;
import com.itmo.utility.DBUtility;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anastasia.sulyagina
 */

@Service
public class SOSService {
    private Connection connection;
    public SOSService() {
        connection = DBUtility.getConnection();
    }

    private void createSOSTable() {
        try {
            Statement st = connection.createStatement();
            try {
                st.executeQuery("drop table SOS");
                st.executeQuery("drop sequence seq_sos");
            } catch (SQLException se) {
                //Игнорировать ошибку удаления таблицы
                if (se.getErrorCode() == 942) {
                    String msg = se.getMessage();
                    System.out.println("Ошибка при удалении таблицы: " + msg);
                }
            }
            st.addBatch("create table SOS(id NUMBER(4) PRIMARY KEY, " +
                    "login VARCHAR(20), lat DOUBLE PRECISION, lng DOUBLE PRECISION, description VARCHAR(200), state VARCHAR(20))");
            st.addBatch("CREATE SEQUENCE seq_sos START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
            st.addBatch("CREATE OR REPLACE TRIGGER trg_sos_id BEFORE INSERT ON SOS FOR EACH ROW BEGIN :NEW.id := seq_sos.NextVal; END;");

            st.addBatch("INSERT INTO SOS (login, lat, lng, description, state) " +
                    "VALUES('user1', 59.906958, 30.302391100000023, 'HELP! 20 zombies attacking the supermarket, will not hold long', 'Filed')");
            st.addBatch("INSERT INTO SOS (login, lat, lng, description, state) " +
                    "VALUES('user2', 59.9286248, 30.360498399999983, 'Barricaded in the trade center, group of 50 people, help!', 'Filed')");
            st.addBatch("INSERT INTO SOS (login, lat, lng, description, state) " +
                    "VALUES('user3', 59.80291259999999, 30.267839099999946, '', 'Filed')");
            st.executeBatch();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Map<String, String> addSOS(String login, double lat, double lng, String description) {
        Map<String, String> m = new HashMap<>();
        Boolean sosExists;
        try {
            //createSOSTable();
            PreparedStatement preparedStatement1 = connection.prepareStatement("select COUNT(*) from SOS where lat=? and lng=?");
            preparedStatement1.setDouble(1, lat);
            preparedStatement1.setDouble(2, lng);
            ResultSet rs = preparedStatement1.executeQuery();
            rs.next();
            sosExists = rs.getInt(1) > 0;
            rs.close();
            m.put("status", (!sosExists?"complete": "error"));
            if (!sosExists) {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("insert into SOS(login, lat, lng, description, state) values (?, ?, ?, ?, ?)");
                preparedStatement.setString(1, login);
                //  preparedStatement.setString(2, login);
                preparedStatement.setDouble(2, lat);
                preparedStatement.setDouble(3, lng);

                preparedStatement.setString(4, description);
                preparedStatement.setString(5, "Filed");
                preparedStatement.executeUpdate();
                m.put("message", "SOS filed. Please wait.");

            } else {
                m.put("message", "SOS already exists. Please wait.");
            }
            return m;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public List<SOS> getSOSList() {
        List<SOS> l = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from sos where state='Filed'");
            ResultSet rs = preparedStatement.executeQuery();

            // Fetch each row from the result set
            while (rs.next()) {
                SOS sos = new SOS(rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("lat"),
                        rs.getString("lng"),
                        rs.getString("description"),
                        rs.getString("state"));
                l.add(sos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public Map<String, String> changeSOSState(Integer id, String state) {
        Map<String, String> m = new HashMap<>();
        try {
            Statement st = connection.createStatement();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update SOS set state = ? where id = ?");
            preparedStatement.setString(1, state);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            m.put("status", "complete");
            return m;
        } catch (SQLException e) {
            m.put("status", "error");
        }
        return m;
    }

    public List<SOS> getSOSByLogin(String login) {
        List<SOS> l = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from sos where login=?");
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            // Fetch each row from the result set
            while (rs.next()) {
                SOS sos = new SOS(rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("lat"),
                        rs.getString("lng"),
                        rs.getString("description"),
                        rs.getString("state"));
                l.add(sos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public void removeSOSByLogin(String login) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from SOS where login=?");

            preparedStatement.setString(1, login);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
