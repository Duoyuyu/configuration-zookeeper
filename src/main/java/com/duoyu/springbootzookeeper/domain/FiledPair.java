package com.duoyu.springbootzookeeper.domain;

import org.springframework.core.env.Environment;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.Field;

/**
 * @Description 将需要变更的bean储存在该容器中
 * @Author wangduoyu
 * @Date 2021/8/20
 */
public class FiledPair {
    private PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}", ":", true);

    private Object bean;
    private Field filed;
    private String value;

    public FiledPair(Object bean, Field filed, String value) {
        this.bean = bean;
        this.filed = filed;
        this.value = value;
    }

    public void resetValue(Environment environment){
        // 属性值是否私有可访问，不可访问则修改
        boolean accessible = filed.isAccessible();
        if(!accessible){
            filed.setAccessible(true);
        }
        // 将environment中value的值替换
        String resetValue = propertyPlaceholderHelper.replacePlaceholders(value, environment::getProperty);
        try {
            // 反射修改bean中属性
            filed.set(bean, resetValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
