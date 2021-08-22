package com.duoyu.springbootzookeeper.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListenerBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * @Description watch监听zookeeper数据变更
 * @Author wangduoyu
 * @Date 2021/8/20
 */
public class NodeDataChangeCuratorCacheListener implements CuratorCacheListenerBuilder.ChangeListener {
    private Environment environment;
    private ConfigurableApplicationContext context;

    public NodeDataChangeCuratorCacheListener(Environment environment, ConfigurableApplicationContext context) {
        this.environment = environment;
        this.context = context;
    }

    @Override
    public void event(ChildData oldNode, ChildData node) {
        System.out.println("收到zookeeper数据变更事件");
        // 将json格式数据转为Map
        String s = new String(node.getData());
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map map = mapper.readValue(s, Map.class);
            // 替换旧的PropertySource
            ConfigurableEnvironment environment = (ConfigurableEnvironment)this.environment;
            MapPropertySource propertySource = new MapPropertySource("configService", map);
            environment.getPropertySources().replace("configService", propertySource);
            System.out.println("Environment数据更新完成,开始更新bean");
            // 更新完成后发送一个事件去更新bean
            context.publishEvent(new EnvironmentChangeEvent(this));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
