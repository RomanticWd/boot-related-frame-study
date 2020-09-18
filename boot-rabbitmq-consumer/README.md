# rabbitmq-消费者
## 消息确认模式：
* AcknowledgeMode.NONE：自动确认
* AcknowledgeMode.AUTO：根据情况确认
* AcknowledgeMode.MANUAL：手动确认

## 手动确认
####  basic.ack用于肯定确认
####  basic.nack用于否定确认
channel.basicNack(deliveryTag, false, true);
* 第一个参数依然是当前消息到的数据的唯一id;
* 第二个参数是指是否针对多条消息；如果是true，也就是说一次性针对当前通道的消息的tagID小于当前这条消息的，都拒绝确认。
* 第三个参数是指是否重新入列，也就是指不确认的消息是否重新丢回到队列里面去。
#### basic.reject用于否定确认，但与basic.nack相比有一个限制:一次只能拒绝单条消息 
channel.basicReject(deliveryTag, true);  拒绝消费当前消息，如果第二参数传入true，就是将数据重新丢回队列里，那么下次还会消费这消息。设置false，就是告诉服务器，我已经知道这条消息数据了，因为一些原因拒绝它，而且服务器也把这个消息丢掉就行。 下次不想再消费这条消息了。

## @RabbitListener注解和ChannelAwareMessageListener之间的关系
两者本质上是同级的，比如监听了同一个queue，那么生产者发到这个queue的消息会轮询这两个消费方式。

不同的点是@RabbitListener注解想实现手动ack必须在配置文件中配置：
```yaml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: manual
```
增加配置后才可以在代码中进行手动ack：
```java
@Component
public class DirectListenerOne {

    @RabbitHandler
    @RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
    public void process(Message testMessage, Channel channel, @Headers Map<String, Object> map) {
        System.out.println("One DirectReceiver消费者收到消息  : " + testMessage.toString());
        try {
            channel.basicReject(testMessage.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
否则会报`unknown delivery tag 1`的错误，就是因为rabbitmq的默认ack是自动的，如果在代码中手动reject，那么就相当于ack了两次，那么第二次肯定就会找不到delivery。

而ChannelAwareMessageListener一般是配合SimpleMessageListenerContainer一起使用，通过在配置文件中配置实现：
```java
@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private MyAckListener myAckListener;//消息接收处理类

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // RabbitMQ默认是自动确认，这里改为手动确认消息
        //设置一个队列
        container.setQueueNames("TestDirectQueue");
        //如果同时设置多个如下： 前提是队列都是必须已经创建存在的
        //  container.setQueueNames("TestDirectQueue","TestDirectQueue2","TestDirectQueue3");

        //另一种设置队列的方法,如果使用这种情况,那么要设置多个,就使用addQueues
        //container.setQueues(new Queue("TestDirectQueue",true));
        //container.addQueues(new Queue("TestDirectQueue2",true));
        //container.addQueues(new Queue("TestDirectQueue3",true));
        container.setMessageListener(myAckListener);

        return container;
    }

}
```

##  @RabbitListener 和 @RabbitHandler 搭配使用
@RabbitListener 可以标注在类上面，需配合 @RabbitHandler 注解一起使用
@RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理，具体使用哪个方法处理，根据 MessageConverter 转换后的参数类型
```java
@Component
@RabbitListener(queues = "consumer_queue")
public class Receiver {

    @RabbitHandler
    public void processMessage1(String message) {
        System.out.println(message);
    }

    @RabbitHandler
    public void processMessage2(byte[] message) {
        System.out.println(new String(message));
    }
    
}
```
