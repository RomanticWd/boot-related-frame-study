package org.liu.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * kafka配置类
 */
@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.my-topic}")
    private String myTopic;

    @Value("${kafka.topic.my-topic2}")
    private String myTopic2;

    /**
     * Json消息转换器
     *
     * @return RecordMessageConverter
     */
    @Bean
    public RecordMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }

    /**
     * 通过注入一个 NewTopic 类型的 Bean 来创建 topic，如果 topic 已存在，则会忽略。
     */
    @Bean
    public NewTopic myTopic() {
        /*
        mytopic 主题 Producer 将消息发送到特定的主题，Consumer 通过订阅特定的 Topic(主题) 来消费消息
        partition 数为 2  Partition 属于 Topic 的一部分。一个 Topic 可以有多个 Partition ，
            并且同一 Topic 下的 Partition 可以分布在不同的 Broker 上，这也就表明一个 Topic 可以横跨多个 Broker
        replica 数为 1  多副本机制多个副本之间会有一个叫做 leader 的家伙，其他副本称为 follower。
            我们发送的消息会被发送到 leader 副本，然后 follower 副本才能从 leader 副本中拉取消息进行同步。
        */
        return new NewTopic(myTopic, 2, (short) 1);
    }

    @Bean
    public NewTopic myTopic2() {
        NewTopic newTopic = new NewTopic(myTopic2, 1, (short) 1);
        Map<String, String> configs = new HashMap<>();
        // 一般情况下我们还需要设置 min.insync.replicas> 1 ，这样配置代表消息至少要被写入到 2 个副本才算是被成功发送。min.insync.replicas 的默认值为 1 ，在实际生产中应尽量避免默认值 1。
        // 为了保证整个 Kafka 服务的高可用性，你需要确保 replication.factor > min.insync.replicas 。
        // 为什么呢？设想一下假如两者相等的话，只要是有一个副本挂掉，整个分区就无法正常工作了。这明显违反高可用性！一般推荐设置成 replication.factor = min.insync.replicas + 1。
        configs.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2");
        // 配置了 unclean.leader.election.enable = false 的话，当 leader 副本发生故障时就不会从 follower 副本中和 leader 同步程度达不到要求的副本中选择出 leader ，这样降低了消息丢失的可能性。
        configs.put(TopicConfig.UNCLEAN_LEADER_ELECTION_ENABLE_CONFIG, "false");
        newTopic.configs(configs);
        return newTopic;
    }
}
