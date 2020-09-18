package org.liu.demo.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "topic.man")//监听的队列名称 topic.man
public class TopicManListener {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("Man TopicReceiver消费者收到消息  : " + testMessage);
    }
}
