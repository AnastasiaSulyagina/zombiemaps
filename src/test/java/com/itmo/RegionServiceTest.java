package com.itmo;

import com.itmo.dao.RegionService;
import com.itmo.dao.SOSService;
import com.itmo.model.Region;
import com.itmo.model.SOS;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by anastasia.sulyagina
 */
public class RegionServiceTest {
    private RegionService s = new RegionService();

    @After
    public void cleanTestData() {
        s.removeRegionByName("test");
    }

    @Test
    public void changeRegionTest() {
        s.addRegion("test", 1, "test", 0, 0, 0);
        Region r = s.getRegionByName("test");
        s.changeRegionState(r.getId(), 10, "Bad");
        r = s.getRegionByName("test");
        assertEquals(r.getDangerLevel().intValue(), 10);
    }
}
