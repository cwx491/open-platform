package com.alliance.radish.config;

import com.alliance.radish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * 认证中心配置
 */
@Configuration
@EnableAuthorizationServer  //注解开启了验证服务器
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * 在WebSecurityConfig中已经定义过该Bean
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 该对象用来将令牌信息存储到Redis中
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }

    //token保存策略
    @Bean
    public TokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource);
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //设置redis token存储中的前缀
        redisTokenStore.setPrefix("auth-token:");
        return redisTokenStore;
    }

    /**
     * 配置 token 节点的安全策略
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients()    //允许form表单客户端认证,允许客户端使用client_id和client_secret获取token
                .checkTokenAccess("isAuthenticated()")     //通过验证返回token信息
                .tokenKeyAccess("permitAll()");            // 获取token请求不进行拦截
    }

    //从数据库表oauth_client_details中查询出客户端信息
    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    //指定客户端登录信息来源
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //从数据库取数据
        clients.withClientDetails(clientDetailsService());// 设置客户端的配置从数据库中读取，存储在oauth_client_details表
    }


    //授权信息保存策略
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 授权码模式专用对象
     * 已有实现类 InMemoryAuthorizationCodeServices，JdbcAuthorizationCodeServices【return new JdbcAuthorizationCodeServices(dataSource)】
     * 默认使用InMemoryAuthorizationCodeServices
     * AuthorizationCodeServices以保存authorization_code的授权码code
     *
     * 改造：使用redis存储code
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        RedisTemplate<String, OAuth2Authentication> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return new RandomValueAuthorizationCodeServices(){

            @Override
            protected void store(String code, OAuth2Authentication authentication) {
                redisTemplate.boundValueOps(code).set(authentication, 10, TimeUnit.MINUTES);
            }

            @Override
            protected OAuth2Authentication remove(String code) {
                OAuth2Authentication authentication = redisTemplate.boundValueOps(code).get();
                redisTemplate.delete(code);
                return authentication;
            }
        };
    }

    //用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .approvalStore(approvalStore())
                .authenticationManager(authenticationManager)// 开启密码验证，来源于 WebSecurityConfigurerAdapter
                .authorizationCodeServices(authorizationCodeServices())
                .tokenStore(tokenStore())
                .userDetailsService(userService);// 读取验证用户的信息
    }
}
