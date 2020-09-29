# kafka&springboot实践

## Kafka 比较重要的几个概念
1. Producer（生产者） : 产生消息的一方。
2. Consumer（消费者） : 消费消息的一方。
3. Broker（代理） : 可以看作是一个独立的 Kafka 实例，启动一个kafka就是一个broker。多个 Kafka Broker 组成一个 Kafka Cluster。

每个 Broker 中又包含了 Topic 以及 Partition 这两个重要的概念：

1. Topic（主题） : Producer 将消息发送到特定的主题，Consumer 通过订阅特定的 Topic(主题) 来消费消息。
2. Partition（分区） : Partition 属于 Topic 的一部分。一个 Topic 可以有多个 Partition ，并且同一 Topic 下的 Partition 可以分布在不同的 Broker 上，这也就表明一个 Topic 可以横跨多个 Broker 。
3. Replica（副本）：分区（Partition）中的多个副本之间会有一个叫做 leader 的家伙，其他副本称为 follower。我们发送的消息会被发送到 leader 副本，然后 follower 副本才能从 leader 副本中拉取消息进行同步。

> 生产者和消费者只与 leader 副本交互。你可以理解为其他副本只是 leader 副本的拷贝，它们的存在只是为了保证消息存储的安全性。当 leader 副本发生故障时会从 follower 中选举出一个 leader,但是 follower 中如果有和 leader 同步程度达不到要求的参加不了 leader 的竞选。

#### Kafka 的多分区（Partition）以及多副本（Replica）机制有什么好处呢？
1. Kafka 通过给特定 Topic 指定多个 Partition, 而各个 Partition 可以分布在不同的 Broker 上, 这样便能提供比较好的并发能力（负载均衡）。
(可以理解为相同的队列却部署在不同的机器上，这样一台消费者机器宕机，其他机器上的队列还可以继续消费)
2. Partition 可以指定对应的 Replica 数, 这也极大地提高了消息存储的安全性, 提高了容灾能力，不过也相应的增加了所需要的存储空间。