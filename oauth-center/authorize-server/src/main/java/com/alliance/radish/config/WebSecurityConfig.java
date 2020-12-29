package com.alliance.radish.config;

import com.alliance.radish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * WebSecurityConfig 对Web请求的配置
 * 1.设置密码编码验证器 PasswordEncoder
 * 2.设置认证server，public void configure(AuthenticationManagerBuilder auth)
 *      指定userDetailsService、passwordEncoder
 * 3.设置web config配置 ，protected void configure(HttpSecurity http)
 *      设置所有的请求，都需经过认证
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //TODO  prePostEnabled 待确认
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义UserDetailsService用来从数据库中根据用户名查询用户信息以及角色信息
     */
    @Autowired
    private UserService userService;

    /**
     * 密码编码验证器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证server ,密码处理
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    /**
     * AuthenticationManager对象在OAuth2认证服务中要使用，提取放入IOC容器中
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}