package com.duoyu.springbootzookeeper.service;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * 可扩展的加载配置接口
 * SPI扩展点
 */
public interface PropertySourceLocator {
    PropertySource<?> locate(Environment environment, ConfigurableApplicationContext context);

    default Collection<PropertySource<?>> locateCollection(Environment environment, ConfigurableApplicationContext context){
        return locateCollections(this, environment, context);
    }

    // 收集属性源列表
    static Collection<PropertySource<?>> locateCollections(PropertySourceLocator locator, Environment environment, ConfigurableApplicationContext context){
        PropertySource<?> propertySource = locator.locate(environment, context);
        if(null == propertySource){
            return Collections.emptyList();
        }
        return Arrays.asList(propertySource);
    }
}
