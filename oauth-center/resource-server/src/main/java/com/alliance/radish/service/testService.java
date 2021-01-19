package com.alliance.radish.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class testService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void test(){
        logger.info("测试---------------");
        int a= 1;
        int b = 0;
        int c =a/b;
    }
}
