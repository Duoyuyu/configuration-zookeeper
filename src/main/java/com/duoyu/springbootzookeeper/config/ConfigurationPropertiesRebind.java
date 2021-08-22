package com.duoyu.springbootzookeeper.config;

import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Description 收到变更通知，便利更改bean属性
 * @Author wangduoyu
 * @Date 2021/8/20
 */
@Component
public class ConfigurationPropertiesRebind implements ApplicationListener<EnvironmentChangeEvent> {
    private ConfigurationPropertiesBeans beans;
    private Environment environment;

    public ConfigurationPropertiesRebind(ConfigurationPropertiesBeans beans, Environment environment) {
        this.beans = beans;
        this.environment = environment;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent environmentChangeEvent) {
        System.out.println("收到environment变更事件,开始变更bean属性");
        rebind();
    }

    public void rebind(){
        this.beans.getFiledMapper().forEach((k,v) -> {
            v.forEach(filedPair -> {
                filedPair.resetValue(environment);
            });
        });
    }
}
