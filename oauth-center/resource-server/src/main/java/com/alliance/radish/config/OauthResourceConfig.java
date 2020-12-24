package com.alliance.radish.config;


import com.alliance.radish.service.LogoutSuccessHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OauthResourceConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Bean
    public PasswordEncoder myPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 指定token的持久化策略
     * 其下有   RedisTokenStore保存到redis中，
     * JdbcTokenStore保存到数据库中，
     * InMemoryTokenStore保存到内存中等实现类，
     * 这里我们选择保存在数据库中
     *
     * @return
     */
    @Bean
    public TokenStore jdbcTokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("product_api")//指定当前资源的id，非常重要！必须写！
                .tokenStore(jdbcTokenStore());//指定保存token的方式
    }

    public void configure(HttpSecurity http) throws Exception {
        http
                .logout()
                .logoutUrl("/logout")//虚拟的登出地址
                .logoutSuccessHandler(logoutSuccessHandler)//登出做的操作
                .and()
                .authorizeRequests()
                .antMatchers("/**").authenticated();
//                .antMatchers("/test/hello").permitAll()
//                .antMatchers("/test/**").authenticated();


//        http.authorizeRequests()
//                //指定不同请求方式访问资源所需要的权限，一般查询是read，其余是write。
//                .antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read')")
//                .antMatchers(HttpMethod.POST, "/**").access("#oauth2.hasScope('write')")
//                .antMatchers(HttpMethod.PATCH, "/**").access("#oauth2.hasScope('write')")
//                .antMatchers(HttpMethod.PUT, "/**").access("#oauth2.hasScope('write')")
//                .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write')")
//                .and()
//                .headers().addHeaderWriter((request, response) -> {
//            response.addHeader("Access-Control-Allow-Origin", "*");//允许跨域
//            if (request.getMethod().equals("OPTIONS")) {//如果是跨域的预检请求，则原封不动向下传达请求头信息
//                response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
//                response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
//            }
//        });
    }
}
