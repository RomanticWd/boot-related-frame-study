package org.liu.demo.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DirectListenerOne {

    @RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
    public void process(Message testMessage, Channel channel, @Headers Map<String, Object> map) {
        System.out.println("One DirectReceiver消费者收到消息  : " + testMessage.toString());
        //RabbitListener与ChannelAwareMessageListener同时basicReject情况测试
        /*try {
            channel.basicReject(testMessage.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
