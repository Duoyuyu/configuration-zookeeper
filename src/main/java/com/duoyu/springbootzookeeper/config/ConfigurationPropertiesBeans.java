package com.duoyu.springbootzookeeper.config;

import com.duoyu.springbootzookeeper.annotation.RefreshScope;
import com.duoyu.springbootzookeeper.domain.FiledPair;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 在项目启动的时候获取需要更新属性值的bean,并储存起来
 * @Author wangduoyu
 * @Date 2021/8/20
 */
@Component
public class ConfigurationPropertiesBeans implements BeanPostProcessor {// bean的处理器

    // 储存所有需要变更的bean
    private Map<String, List<FiledPair>> filedMapper = new HashMap<>();

    // 重写bean的后置处理
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class cls = bean.getClass();
        // 如果类上有该注解则动态刷新
        if(cls.isAnnotationPresent(RefreshScope.class)){
            for(Field field : cls.getDeclaredFields()){
                // 得到带有value注解的字段
                Value value = field.getAnnotation(Value.class);
                if(null == value){
                    continue;
                }
                List<String> propertyKey = getPropertyKey(value.value(), 0);
                // 把用到该属性的bean保存起来
                for(String key : propertyKey){
                    filedMapper.computeIfAbsent(key, (k) -> new ArrayList<>())
                            .add(new FiledPair(bean, field, value.value()));
                }
            }
        }
        return bean;
    }

    private List<String> getPropertyKey(String value, int begin){
        int start = value.indexOf("${", begin) + 2;
        if (start < 2) {
            return new ArrayList<>();
        }
        int middle = value.indexOf(":", start);
        int end = value.indexOf("}", start);
        String key;
        if(middle > 0 && middle < end){
            key = value.substring(start, middle);
        }else{
            key = value.substring(start, end);
        }
        List<String> keys = getPropertyKey(value, end);// 可以注入多个，如@Value(${property:default_value})
        keys.add(key);
        return keys;
    }

    public Map<String, List<FiledPair>> getFiledMapper() {
        return filedMapper;
    }

    public void setFiledMapper(Map<String, List<FiledPair>> filedMapper) {
        this.filedMapper = filedMapper;
    }
}
