package com.spring.batch.demo.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/home")
    public String returnHome() {
        return "Hello World";
    }
}
