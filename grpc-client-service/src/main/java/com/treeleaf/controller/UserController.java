package com.treeleaf.controller;

import com.google.protobuf.Descriptors;
import com.treeleaf.service.UserClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserClientService userClientService;

    @GetMapping("/users/{user_id}")
    public Map<Descriptors.FieldDescriptor, Object> getUser(@PathVariable int user_id){
        return userClientService.getUser(user_id);
    }

    @GetMapping("/users/details/{gender}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getUsersByGender(@PathVariable String gender) throws InterruptedException {
        return  userClientService.getAllUsersByGender(gender);
    }

    @GetMapping("/users/find")
    public Map<String,Map<Descriptors.FieldDescriptor, Object>> findFirstAdminUserRole(@RequestBody List<Integer> userIdList) throws InterruptedException {
        return userClientService.findFirstAdminUserRole(userIdList);
    }

    @GetMapping("users/ages")
    public List<Map<Descriptors.FieldDescriptor, Object>> getUsersByAges(@RequestBody int[] ages) throws InterruptedException {
        return userClientService.getUserByAge(ages);
    }
}
