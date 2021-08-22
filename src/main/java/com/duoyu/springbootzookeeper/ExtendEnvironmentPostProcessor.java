package com.duoyu.springbootzookeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * @Description 加载工程内部的配置文件
 * @Author wangduoyu
 * @Date 2021/8/20
 */
public class ExtendEnvironmentPostProcessor implements EnvironmentPostProcessor {
    Properties properties = new Properties();
    String propertiesFile = "extend.properties";
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Resource resource = new ClassPathResource(propertiesFile);
        environment.getPropertySources().addLast(loadProperties(resource));
    }

    private PropertySource<?> loadProperties(Resource resource){
        if(!resource.exists()){
            throw new RuntimeException("file not exist");
        }
        try {
            properties.load(resource.getInputStream());
            return new PropertiesPropertySource(resource.getFilename(), properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
