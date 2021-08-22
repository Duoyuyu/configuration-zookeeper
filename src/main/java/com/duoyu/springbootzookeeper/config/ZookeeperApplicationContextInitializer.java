package com.duoyu.springbootzookeeper.config;

import com.duoyu.springbootzookeeper.service.PropertySourceLocator;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description 容器初始化时加载所有的扩展点
 * @Author wangduoyu
 * @Date 2021/8/20
 */
public class ZookeeperApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private List<PropertySourceLocator> propertySourceLocators;

    public ZookeeperApplicationContextInitializer(){
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        // 加载所有的PropertySourceLocator扩展点
        propertySourceLocators=new ArrayList<>(SpringFactoriesLoader.loadFactories(PropertySourceLocator.class, classLoader));

    }
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        // 动态加载扩展的机制到Environment中
        ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        for(PropertySourceLocator locator : propertySourceLocators) {
            Collection<PropertySource<?>> sources = locator.locateCollection(environment, configurableApplicationContext);
            if (null == sources || sources.size() == 0) {
                continue;
            }
            sources.forEach(p -> {
                propertySources.addLast(p);// 将远程数据源加载到Environment
            });
        }
    }
}
