package com.itmo.controller;

/**
 * Created by anastasia.sulyagina
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itmo.dao.AuthorisationService;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@RestController
public class AuthorisationController {
    @Autowired
    private AuthorisationService authorisationService;// = new AuthorisationService();

//    @RequestMapping(value="/users",method = RequestMethod.GET,headers="Accept=application/json")
//    public List<task> getAllTasks() {
//        List<task> tasks=taskmanagerservice.getAllTasks();
//        return tasks;
//    }

    @RequestMapping(
            value = "/login",
            params = { "login", "password" },
            method = RequestMethod.GET,
            headers="Accept=application/json")
    @ResponseBody
    public ResponseEntity getBarBySimplePathWithExplicitRequestParams(
            @RequestParam("login") String login,
            @RequestParam("password") String password
    ) {
        return new ResponseEntity<>(authorisationService.checkUser(login, password), HttpStatus.OK);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
