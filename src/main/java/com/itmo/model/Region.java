package com.itmo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anastasia.sulyagina
 */
public class Region {
    private Integer id;
    private String name;
    private Integer dangerLevel;
    private String description;
    private String lat;
    private String lng;
    private Double rad;

    public Region(Integer newId, String newName, Integer newDL, String newDescription, String newLat, String newLng, Double newRad) {
        this.id = newId;
        this.name = newName;
        this.dangerLevel = newDL;
        this.description = newDescription;
        this.lat = newLat;
        this.lng = newLng;
        this.rad = newRad;
    }

    public Map<String, String> serialize() {
        Map<String, String> m = new HashMap<>();
        m.put("id", this.id.toString());
        m.put("name", this.name);
        m.put("dangerLevel", this.dangerLevel.toString());
        m.put("description", this.description);
        m.put("lat", this.lat);
        m.put("lng", this.lng);
        m.put("rad", this.rad.toString());
        return m;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDangerLevel() {
        return dangerLevel;
    }
}
