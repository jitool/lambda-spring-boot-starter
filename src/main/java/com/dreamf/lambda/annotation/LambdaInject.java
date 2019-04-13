package com.dreamf.lambda.annotation;

import java.lang.annotation.*;

/**
 * 函数注入
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LambdaInject {

    /**
     * 函数名
     * @return
     */
    String name() default "";

    /**
     * 函数版本号
     * @return
     */
    int version() default -1;

}
