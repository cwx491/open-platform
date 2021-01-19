package com.alliance.radish.controller;

import com.alliance.radish.service.testService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private testService ts;

    @GetMapping("/hello")
    public String hello(){
        logger.trace("log-trace日志测试");
        logger.debug("log-debug日志测试");
        logger.info("log-info日志测试");
        logger.warn("log-warn日志测试");
        logger.error("log-error日志测试");
        ts.test();
        return "Hello";
    }

//    @GetMapping("/meet")
//    public String meet(){
//        return "I meet you";
//    }
//
//    @GetMapping("/welcome")
//    public String welcome(){
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return "Welcome " + authentication.getName();
//    }
//
//    @GetMapping("/project")
//    @PreAuthorize("hasRole('ROLE_PROJECT_ADMIN')")  //具有此角色
//    public String project(){
//        return "This is my project";
//    }


}