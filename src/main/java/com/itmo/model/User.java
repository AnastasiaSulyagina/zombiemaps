package com.itmo.model;
import javax.persistence.*;

/**
 * Created by anastasia.sulyagina
 */

public class User{
    private Integer id;
    private String login;
    private String password;
    private String name;
    private String role = "user";

    public User(Integer newId, String newLogin, String newPassword, String newName, String newRole) {
        this.id = newId;
        this.login = newLogin;
        this.password = newPassword;
        this.name = newName;
        this.role = newRole;
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
