package com.itmo;

import com.itmo.dao.AuthorisationService;
import com.itmo.dao.SOSService;
import com.itmo.model.SOS;
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

public class SOSServiceTest {
    private SOSService s = new SOSService();

    @After
    public void cleanTestData() {
        s.removeSOSByLogin("test");
    }

    @Test
    public void addSOSTest() {
        s.addSOS("test", 0.0, 0.0, "SOS");
        assertEquals(s.getSOSByLogin("test").size(), 1);
    }

    @Test
    public void changeSOSStateTest() {
        s.addSOS("test", 0.0, 0.0, "SOS");
        SOS sos = s.getSOSByLogin("test").get(0);
        s.changeSOSState(sos.getId(), "In progress");
        sos = s.getSOSByLogin("test").get(0);
        assertEquals(sos.getState(), "In progress");
    }
}

