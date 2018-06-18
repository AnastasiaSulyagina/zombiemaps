package com.itmo.dao;

import com.itmo.model.Region;
import com.itmo.model.SOS;
import com.itmo.utility.DBUtility;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by anastasia.sulyagina
 */

@Service
public class RegionService {
    private Connection connection;
    public RegionService() {
        connection = DBUtility.getConnection();
    }
    private void createRegionsTable() {
        try {
            Statement st = connection.createStatement();
            try {
                st.executeQuery("drop table regions");
                st.executeQuery("drop sequence seq_regions");
            } catch (SQLException se) {
                //Игнорировать ошибку удаления таблицы
                if (se.getErrorCode() == 942) {
                    String msg = se.getMessage();
                    System.out.println("Ошибка при удалении таблицы: " + msg);
                }
            }
            st.addBatch("create table regions(id NUMBER(4) PRIMARY KEY, " +
                    "region_name VARCHAR(20), dangerLevel INTEGER, description VARCHAR(200), lat DOUBLE PRECISION, lng DOUBLE PRECISION, rad DOUBLE PRECISION)");
            st.addBatch("CREATE SEQUENCE seq_regions START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
            st.addBatch("CREATE OR REPLACE TRIGGER trg_regions_id BEFORE INSERT ON regions FOR EACH ROW BEGIN :NEW.id := seq_sos.NextVal; END;");

            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description, lat, lng, rad) " +
                    "VALUES('Admiralteysky', 1, 'Not dangerous', 59.9108957, 30.295336099999986, 1000)");
            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description, lat, lng, rad) " +
                    "VALUES('Sennoy', 10, 'Lots of zombies. Not safe', 59.92617899999999,30.318023999999923, 1000)");
            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description, lat, lng, rad) " +
                    "VALUES('Yekateringofsky', 8, 'Multiple groups seen', 59.88266460000001,30.2143413, 1500)");
            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description, lat, lng, rad) " +
                    "VALUES('Kirovsky', 4, 'Some zombies. Can avoid', 59.8512329,30.25335110000003, 1700)");
            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description, lat, lng, rad) " +
                    "VALUES('Nevsky', 6, 'Zombies seen. Careful', 59.93119429999999,30.362370000000055, 1100)");
            st.executeBatch();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Region> getRegionList() {
        List<Region> l = new ArrayList();
        try {
            //createRegionsTable();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from regions");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                l.add(new Region(rs.getInt("id"),
                        rs.getString("region_name"),
                        rs.getInt("dangerLevel"),
                        rs.getString("description"),
                        rs.getString("lat"),
                        rs.getString("lng"),
                        rs.getDouble("rad")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return l;
    }

    public Map<String, String> changeRegionState(Integer id, Integer dangerLevel, String description) {
        Map<String, String> m = new HashMap<>();
        try {
            Statement st = connection.createStatement();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("update regions set dangerLevel = ?, description = ? where id = ?");
            preparedStatement.setInt(1, dangerLevel);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            m.put("status", "complete");
            return m;
        } catch (SQLException e) {
            m.put("status", "error");
        }
        return m;
    }

    //helpers

    public void addRegion(String name, Integer dangerLevel, String description, double lat, double lng, double rad) {
        try {
            //createRegionsTable();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO regions (region_name, dangerLevel, description, lat, lng, rad) VALUES(?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, name);
            //  preparedStatement.setString(2, login);
            preparedStatement.setInt(2, dangerLevel);
            preparedStatement.setString(3, description);
            preparedStatement.setDouble(4, lat);
            preparedStatement.setDouble(5, lng);
            preparedStatement.setDouble(6, rad);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Region getRegionByName(String name) {
        List<SOS> l = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("select * from regions where region_name=?");
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            Region region = new Region(rs.getInt("id"),
                    rs.getString("region_name"),
                    rs.getInt("dangerLevel"),
                    rs.getString("description"),
                    ((Double)rs.getDouble("lat")).toString(),
                    ((Double)rs.getDouble("lng")).toString(),
                    rs.getDouble("rad"));
            rs.close();
            return region;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeRegionByName(String name) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("delete from regions where region_name=?");

            preparedStatement.setString(1, name);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

