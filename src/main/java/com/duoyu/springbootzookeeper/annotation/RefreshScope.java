package com.duoyu.springbootzookeeper.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefreshScope {
    /**
     * 在需要动态刷新的类上增加此标记，以便在bean加载时进行处理
     * Target，可以使用在类上和属性上
     * Retention 运行时加载
     */
}
