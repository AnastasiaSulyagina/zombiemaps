package com.itmo.controller;

import com.itmo.dao.RegionService;
import com.itmo.dao.SOSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Created by anastasia.sulyagina
 */
@RestController
public class RegionController {
    @Autowired
    private RegionService RegionService;

    @RequestMapping(
            value = "/regions/update",
            params = { "id", "dangerLevel", "description"},
            method = RequestMethod.GET,
            headers="Accept=application/json")
    @ResponseBody
    public ResponseEntity updateRegion(
            @RequestParam("id") Integer regionId,
            @RequestParam("dangerLevel") Integer dangerLevel,
            @RequestParam("description") String description

    ) {
        return new ResponseEntity<>(RegionService.changeRegionState(regionId, dangerLevel, description), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/regions/list",
            method = RequestMethod.GET,
            headers="Accept=application/json")
    @ResponseBody
    public ResponseEntity getAllRegions() {
        return new ResponseEntity<>(RegionService
                .getRegionList()
                .stream()
                .map(r -> r.serialize())
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
