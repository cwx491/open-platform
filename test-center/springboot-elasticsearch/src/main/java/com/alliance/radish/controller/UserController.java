package com.alliance.radish.controller;

import com.alibaba.fastjson.JSONObject;
import com.alliance.radish.domain.User;
import com.alliance.radish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    public UserService userService;

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public void addUser(@RequestBody User user){
        userService.save(user);
    }

    @GetMapping("/getUsers")
    public String getUsers(){
        return JSONObject.toJSONString(userService.findAll());
    }

}
