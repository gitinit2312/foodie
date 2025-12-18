package com.jspider.foodiesapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jspider.foodiesapi.io.UserRequest;
import com.jspider.foodiesapi.io.UserResponse;
import com.jspider.foodiesapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping ("/user")
public class UserController {
    private final UserService usrSer;
@PostMapping("/register")
@ResponseStatus(HttpStatus.CREATED)
    public UserResponse addUser(@RequestBody UserRequest user){
    return usrSer.registerUser(user);
}
}
