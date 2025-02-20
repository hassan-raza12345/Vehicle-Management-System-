package com.example.Security.Controller;

import com.example.Security.Model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class helloController {

    @GetMapping("/api/hello")
    public String sayHello() {
        System.out.println("hello");
        return "Hello, World!";
    }
}
