package com.dreamf.lambda.annotation;


import java.lang.annotation.*;

/**
 * 函数声明
 */
@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Lambda {

    /**
     * 函数名
     * @return
     */
    String name() default "";

    /**
     * 函数版本号
     * @return
     */
    int version() default 1;

}
