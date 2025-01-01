package com.example.excel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutoScaling {

    @GetMapping("/auto-scaling")
    public String autoScaling() throws InterruptedException {
        Thread.sleep(1000L);
        return "Auto Scaling";
    }
}
