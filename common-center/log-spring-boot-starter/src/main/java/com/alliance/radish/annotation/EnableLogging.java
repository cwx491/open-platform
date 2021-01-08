package com.alliance.radish.annotation;

import com.alliance.radish.selector.LogImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用自定义日志
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogImportSelector.class)
public @interface EnableLogging {

}
