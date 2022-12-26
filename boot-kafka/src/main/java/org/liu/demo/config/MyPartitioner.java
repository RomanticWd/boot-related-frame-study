package org.liu.demo.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * kafka实现自定义分区
 */
public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //key不能空，如果key为空的会通过轮询的方式 选择分区
        if (keyBytes == null || (!(key instanceof String))) {
            throw new RuntimeException("key is null");
        }
        //获取分区列表
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);

        //以下是上述各种策略的实现，不能共存
        //随机策略
//        return ThreadLocalRandom.current().nextInt(partitions.size());

        //按消息键保存策略
//        return Math.abs(key.hashCode()) % partitions.size();

        //自定义分区策略, 比如key为123的消息，选择放入最后一个分区
        if (key.toString().equals("123")) {
            return partitions.size() - 1;
        } else {
            //否则随机
            return ThreadLocalRandom.current().nextInt(partitions.size());
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
