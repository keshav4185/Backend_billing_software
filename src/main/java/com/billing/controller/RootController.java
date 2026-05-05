package com.billing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/test")
    public String test() {
        return "Backend is ALIVE at ROOT";
    }
}
