package com.alliance.radish.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ImportSelector接口是至spring中导入外部配置的核心接口，在SpringBoot的自动化配置和@EnableXXX(功能性注解)都有它的存在
 *
 * 其主要作用是收集需要导入的配置类，如果该接口的实现类同时实现EnvironmentAware， BeanFactoryAware ，
 * BeanClassLoaderAware或者ResourceLoaderAware，那么在调用其selectImports方法之前先调用上述接口中对应的方法，如果需要在所有的@Configuration处理完在导入时,可以实现DeferredImportSelector接口
 *
 *
 */
public class LogImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[] {
                "com.alliance.radish.config.LogAutoConfig"
        };
    }
}
