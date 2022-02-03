package com.cake.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicRestController {
    @GetMapping("jwt")
    public String main(@RequestHeader("Authorization") String auth){
        return auth;
    }
}
