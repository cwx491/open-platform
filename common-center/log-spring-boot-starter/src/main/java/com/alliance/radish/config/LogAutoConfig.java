package com.alliance.radish.config;

import cn.hutool.core.date.SystemClock;
import com.alliance.radish.interceptor.LogInterceptor;
import com.alliance.radish.utils.IPUtils;
import com.github.structlog4j.StructLog4J;
import com.github.structlog4j.json.JsonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

/**
 * 日志拦截器
 * @ConditionalOnClass(WebMvcConfigurer.class) 排除对spring cloud gateway的影响
 */
@ConditionalOnClass(WebMvcConfigurer.class)
public class LogAutoConfig implements WebMvcConfigurer{

    @Value("${spring.application.name:NA}")
    private String appName;
    @Autowired
    private LogInterceptor logInterceptor;


    @PostConstruct
    /*
     * 初始化StructLog4J配置
     */
    public void init(){
        StructLog4J.setFormatter(JsonFormatter.getInstance());
        StructLog4J.setMandatoryContextSupplier(()-> new Object[]{
//                "host",	IPUtils.getHostIp(),
                "APP_NAME",appName
//                "logTime", SystemClock.nowDate()
        });
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /**
         * 自定义拦截器，添加拦截路径和排除拦截路径
         * addPathPatterns():添加需要拦截的路径
         * excludePathPatterns():添加不需要拦截的路径
         * 在括号中还可以使用集合的形式，如注释部分代码所示
         */
        registry.addInterceptor(logInterceptor).addPathPatterns("/**") ;


    }
}
