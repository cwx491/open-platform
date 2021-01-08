package com.alliance.radish.utils.mask;

import java.lang.annotation.*;

/**
 * 标注于 Controller 方法的参数中， 被标注的参数在进行日志输出会进行掩码处理
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@Inherited
public @interface LogMask {
    MaskType value() default MaskType.DEFAULT;
}
