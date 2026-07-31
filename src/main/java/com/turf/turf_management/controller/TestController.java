package com.turf.turf_management.controller;

import com.turf.turf_management.model.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class TestController {

    @GetMapping("/welcome")
    public String getByContribution(@PathVariable String contributionId){
        return "Welcome to Maveric Cricket Club";
    }
}
