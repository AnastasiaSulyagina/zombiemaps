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
                    "region_name VARCHAR(20), dangerLevel INTEGER, description VARCHAR(200))");
            st.addBatch("CREATE SEQUENCE seq_regions START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
            st.addBatch("CREATE OR REPLACE TRIGGER trg_regions_id BEFORE INSERT ON regions FOR EACH ROW BEGIN :NEW.id := seq_sos.NextVal; END;");

            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description) " +
                    "VALUES('Admiralteysky', 1, 'Not dangerous')");
            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description) " +
                    "VALUES('Sennoy', 10, 'Zombies settled in locations a, b, c. Lots of them. Not safe to visit')");
            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description) " +
                    "VALUES('Yekateringofsky', 8, 'Multiple packs of zombies seen, go in big groups only')");
            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description) " +
                    "VALUES('Kirovsky', 4, 'Couple of zompie packs seen, easy to avoid')");
            st.addBatch("INSERT INTO regions (region_name, dangerLevel, description) " +
                    "VALUES('Nevsky', 6, 'Zombies seen in location a, tend to stalk around a lot. Go in small groups')");
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
                        rs.getString("description")));
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

    public void addRegion(String name, Integer dangerLevel, String description) {
        try {
            //createRegionsTable();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO regions (region_name, dangerLevel, description) VALUES(?, ?, ?)");
            preparedStatement.setString(1, name);
            //  preparedStatement.setString(2, login);
            preparedStatement.setInt(2, dangerLevel);
            preparedStatement.setString(3, description);
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
                    rs.getString("description"));
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

