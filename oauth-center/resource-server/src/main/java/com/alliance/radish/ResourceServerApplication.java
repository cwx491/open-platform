package com.alliance.radish;

import com.alliance.radish.annotation.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 资源服务器（Resource Server）：资源服务器是提供给用户资源的服务器，例如头像、照片、视频等。
 * 资源服务器，即服务提供商存放用户生成的资源的服务器。
 * 存储资源的服务器，本例子为微信存储的用户信息。
 * 现在还有一个问题，服务提供商能允许随便一个客户端就接入到它的授权服务器吗？答案是否定的，服务提供商会
 * 给准入的接入方一个身份，用于接入时的凭据:
 * client_id：客户端标识
 * client_secret：客户端秘钥
 * 因此，准确来说，授权服务器对两种OAuth2.0中的两个角色进行认证授权，分别是资源拥有者、客户端。
 */
@EnableLogging
@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

}
