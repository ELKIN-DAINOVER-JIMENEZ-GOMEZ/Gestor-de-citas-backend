package com.GestorDeCitas.Backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagesControler {
    @GetMapping("/messages")
    public String getMessage(){
        return "Hello World";
    }
}
