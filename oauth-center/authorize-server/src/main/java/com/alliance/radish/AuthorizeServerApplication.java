package com.alliance.radish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 授权服务器（Authorization Server）：授权服务器用来验证用户提供的信息是否正确，并返回一个令牌给第三方应用
 * 认证服务器，即服务提供商专门用来处理认证的服务器。
 * 授权服务器（也称认证服务器）
 * 用于服务提供商对资源拥有的身份进行认证、对访问资源进行授权，认证成功后会给客户端发放令牌
 * （access_token），作为客户端访问资源服务器的凭据。
 */
//  @EnableDiscoveryClient和@EnableEurekaClient共同点就是：都是能够让注册中心能够发现，扫描到该服务。
//  不同点：@EnableEurekaClient只适用于Eureka作为注册中心，@EnableDiscoveryClient 可以是其他注册中心。
@EnableEurekaClient
@SpringBootApplication
public class AuthorizeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizeServerApplication.class, args);
    }

}
