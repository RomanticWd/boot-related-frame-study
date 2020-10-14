package org.liu.demo.config;

import org.liu.demo.properties.HelloWorldProperties;
import org.liu.demo.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//当 HelloWorldService 在类路径的条件
@ConditionalOnClass({HelloWorldService.class})
// 将 application.yml 的相关的属性字段与该类一一对应，并生成 Bean
@EnableConfigurationProperties(HelloWorldProperties.class)
public class HelloWorldAutoConfiguration {

    @Autowired
    private HelloWorldProperties helloWorldProperties;

    @Bean
    // 当容器没有这个 Bean 的时候才创建这个 Bean
    @ConditionalOnMissingBean(HelloWorldService.class)
    public HelloWorldService helloworldService() {
        HelloWorldService helloworldService = new HelloWorldService();
        helloworldService.setWord(helloWorldProperties.getWord());
        return helloworldService;
    }

}
