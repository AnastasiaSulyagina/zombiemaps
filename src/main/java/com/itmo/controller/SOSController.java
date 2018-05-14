package com.itmo.controller;

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
public class SOSController {

    @Autowired
    private SOSService SOSService;

    @RequestMapping(
            value = "/sos/create",
            params = { "login", "lat", "lng", "description"},
            method = RequestMethod.GET,
            headers="Accept=application/json")
    @ResponseBody
    public ResponseEntity addNewSOS(
            @RequestParam("login") String login,
            @RequestParam("lat") Double lat,
            @RequestParam("lng") Double lng,
            @RequestParam("lng") String description
    ) {
        return new ResponseEntity<>(SOSService.addSOS(login, lat, lng, description), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/sos/update",
            params = { "id", "state"},
            method = RequestMethod.GET,
            headers="Accept=application/json")
    @ResponseBody
    public ResponseEntity changeSOSState(
            @RequestParam("id") Integer id,
            @RequestParam("state") String state
    ) {
        return new ResponseEntity<>(SOSService.changeSOSState(id, state), HttpStatus.OK);
    }

    @RequestMapping(
            value = "/sos/list",
            method = RequestMethod.GET,
            headers="Accept=application/json")
    @ResponseBody
    public ResponseEntity getAllSOS() {
        return new ResponseEntity<>(SOSService.getSOSList()
                .stream()
                .map(r -> r.serialize())
                .collect(Collectors.toList()), HttpStatus.OK);
    }


    @RequestMapping(
            value = "/sos/my",
            params = { "login"},
            method = RequestMethod.GET,
            headers="Accept=application/json")
    @ResponseBody
    public ResponseEntity getSOSByLogin(
            @RequestParam("login") String login
    ) {
        return new ResponseEntity<>(SOSService.getSOSByLogin(login)
                .stream()
                .map(r -> r.serialize())
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
