package com.treeleaf.controller;

import com.google.protobuf.Descriptors;
import com.treeleaf.service.UserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserClientService userClientService;

    @GetMapping("/users/{user_id}")
    public Map<Descriptors.FieldDescriptor, Object> getUser(@PathVariable int user_id){
        return userClientService.getUser(user_id);
    }
}
