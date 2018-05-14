package com.itmo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anastasia.sulyagina
 */
public class SOS {
    private Integer id;
    private String lat;
    private String lng;
    private String userLogin;
    private String description;
    private String state;

    public SOS(int newId, String newLogin, String newLat, String newLng, String newDescription, String newState) {
        this.id = newId;
        this.userLogin = newLogin;
        this.lat = newLat;
        this.lng = newLng;
        this.description = newDescription;
        this.state = newState;
    }

    public Map<String, String> serialize() {
        Map<String, String> m = new HashMap<>();
        m.put("id", this.id.toString());
        m.put("login", this.userLogin);
        m.put("lat", this.lat);
        m.put("lng", this.lng);
        m.put("description", this.description);
        m.put("state", this.state);
        return m;
    }

    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }
}
