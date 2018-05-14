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

    public Region(Integer newId, String newName, Integer newDL, String newDescription) {
        this.id = newId;
        this.name = newName;
        this.dangerLevel = newDL;
        this.description = newDescription;
    }

    public Map<String, String> serialize() {
        Map<String, String> m = new HashMap<>();
        m.put("id", this.id.toString());
        m.put("name", this.name);
        m.put("dangerLevel", this.dangerLevel.toString());
        m.put("description", this.description);
        return m;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDangerLevel() {
        return dangerLevel;
    }
}
