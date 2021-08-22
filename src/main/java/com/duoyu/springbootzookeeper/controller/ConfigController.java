package com.duoyu.springbootzookeeper.controller;

import com.duoyu.springbootzookeeper.annotation.RefreshScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author wangduoyu
 * @Date 2021/8/20
 */
@RefreshScope
@RestController
public class ConfigController {

    @Autowired
    Environment environment;

    @Value("${name}")
    private String name;

    @RequestMapping("/env")
    public String env(){
        return environment.getProperty("name") + name;
    }
}
