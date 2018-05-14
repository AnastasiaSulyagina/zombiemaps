package com.itmo;

import com.itmo.dao.AuthorisationService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by anastasia.sulyagina
 */
public class AuthorisationServiceTest {
    private AuthorisationService s = new AuthorisationService();

    @After
    public void cleanTestData() {
        s.removeUser("login", "password");
    }


    @Test
    public void addUserTest() {
        s.addUser("login", "password", "Name");
        assertEquals(s.getUserName("login"), "Name");
    }

    @Test
    public void checkUserTest() {
        Map<String,String> myMap = new HashMap<String,String>();
        myMap.put("status", "complete");
        myMap.put("name", "Name");
        myMap.put("role", "user");
        myMap.put("message", "Log in successful");
        s.addUser("login", "password", "Name");
        assertEquals(s.checkUser("login", "password"), myMap);
    }

}
