package com.duoyu.springbootzookeeper.config;

import org.springframework.context.ApplicationEvent;

/**
 * @Description
 * @Author wangduoyu
 * @Date 2021/8/20
 */
public class EnvironmentChangeEvent extends ApplicationEvent {

    public EnvironmentChangeEvent(Object source) {
        super(source);
    }
}
