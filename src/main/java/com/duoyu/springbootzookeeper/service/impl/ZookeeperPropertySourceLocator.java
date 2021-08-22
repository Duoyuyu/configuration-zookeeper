package com.duoyu.springbootzookeeper.service.impl;

import com.duoyu.springbootzookeeper.config.NodeDataChangeCuratorCacheListener;
import com.duoyu.springbootzookeeper.service.PropertySourceLocator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

/**
 * @Description 加载zookeeper的配置
 * @Author wangduoyu
 * @Date 2021/8/20
 */
public class ZookeeperPropertySourceLocator implements PropertySourceLocator {
    private CuratorFramework curatorFramework;
    String dataNode = "/data";

    public ZookeeperPropertySourceLocator() {
        // 连接Zookeeper
        curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.1.128:2181")
                .sessionTimeoutMs(20000)
                .connectionTimeoutMs(20000)
                .retryPolicy(new ExponentialBackoffRetry(10000, 3))
                .namespace("config")
                .build();
        curatorFramework.start();
    }

    @Override
    public PropertySource<?> locate(Environment environment, ConfigurableApplicationContext context) {
        // 加载远程Zookeeper配置到PropertySource
        System.out.println("开始加载外部配置");
        // 将远程数据源封装为PropertySource
        CompositePropertySource compositePropertySource = new CompositePropertySource("configService");
        try {
            Map<String, Object> remoteProperties = getRemoteProperties();
            MapPropertySource configService = new MapPropertySource("configService", remoteProperties);
            compositePropertySource.addPropertySource(configService);
            System.out.println("开始监听配置更新");
            addListener(environment, context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return compositePropertySource;

    }

    /**
     * 获取远程数据源并转为Map
     * @return
     * @throws Exception
     */
    private Map<String, Object> getRemoteProperties() throws Exception {
        String data = new String (curatorFramework.getData().forPath(dataNode));
        ObjectMapper mapper = new ObjectMapper();
        // 远程数据源只支持json
        return mapper.readValue(data, Map.class);
    }

    /**
     * 增加监听事件
     * @param environment
     * @param context
     */
    private void addListener(Environment environment, ConfigurableApplicationContext context){
        NodeDataChangeCuratorCacheListener ndl = new NodeDataChangeCuratorCacheListener(environment, context);
        CuratorCacheListener listener = CuratorCacheListener.builder().forChanges(ndl).build();
        CuratorCache curatorCache = CuratorCache.build(curatorFramework, dataNode, CuratorCache.Options.SINGLE_NODE_CACHE);
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }
}
