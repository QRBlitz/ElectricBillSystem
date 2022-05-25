package com.example.electricbillsystem.security;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin("http://localhost:4200")
public class CorsTestController {

    @GetMapping("/cors")
    public String user(Principal principal) {
        return principal.getName();
    }
}
