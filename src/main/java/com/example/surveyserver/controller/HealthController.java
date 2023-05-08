package com.example.surveyserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/.well-known/health")
    public String health() {
        return "OK";
    }

}