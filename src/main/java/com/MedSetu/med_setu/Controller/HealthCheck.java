package com.MedSetu.med_setu.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("/healthcheck")
    public String healthcheck(){
        return "I am working fine.";
    }
}
