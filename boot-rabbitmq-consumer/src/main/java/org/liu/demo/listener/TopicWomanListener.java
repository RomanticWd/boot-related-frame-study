package org.liu.demo.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "topic.woman")//监听的队列名称 topic.woman
public class TopicWomanListener {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("Woman TopicReceiver消费者收到消息  : " + testMessage);
    }
}
