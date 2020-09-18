package org.liu.demo.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FanoutListener {

    @RabbitListener(queues = "fanout.A")
    public void receiverA(Map testMessage) {
        System.out.println("FanoutReceiverA消费者收到消息  : " + testMessage.toString());
    }

    @RabbitListener(queues = "fanout.B")
    public void receiverB(Map testMessage) {
        System.out.println("FanoutReceiverB消费者收到消息  : " + testMessage.toString());
    }

    @RabbitListener(queues = "fanout.C")
    public void receiverC(Map testMessage) {
        System.out.println("FanoutReceiverC消费者收到消息  : " + testMessage.toString());
    }

}
