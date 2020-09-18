package org.liu.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TopicRabbitConfig {

    public static final String man = "topic.man";
    public static final String woman = "topic.woman";
    public static final String test = "topic.test";

    //队列 起名：topic.man
    @Bean
    public Queue firstQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(man, true);
    }

    //队列 起名：topic.woman
    @Bean
    public Queue secondQuene() {
        return new Queue(woman, true);
    }

    //队列 起名：topic.test
    @Bean
    public Queue thirdQueue() {
        return new Queue(test, true);
    }

    //Topic交换机 起名：TestTopicExchange
    @Bean
    TopicExchange testTopicExchange() {
        return new TopicExchange("TestTopicExchange", true, false);
    }

    //将firstQueue和TestTopicExchange绑定,而且绑定的键值为topic.man
    //这样只要是消息携带的路由键是topic.man,才会分发到该队列
    @Bean
    Binding bindingTopicExchange() {
        return BindingBuilder.bind(firstQueue()).to(testTopicExchange()).with(man);
    }

    // 将secondQueue和TestTopicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    @Bean
    Binding bindingTopicExchange2() {
        return BindingBuilder.bind(secondQuene()).to(testTopicExchange()).with("topic.#");
    }

    //将thirdQueue和TestTopicExchange绑定,而且绑定的键值为topic.test
    //这样只要是消息携带的路由键是topic.test,才会分发到该队列
    @Bean
    Binding bindingTopicExchange3() {
        log.info("绑定topic.test队列到交换机TestTopicExchange");
        return BindingBuilder.bind(thirdQueue()).to(testTopicExchange()).with(test);
    }
}
