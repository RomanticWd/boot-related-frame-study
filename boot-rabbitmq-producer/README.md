# rabbitmq-生产者
## BindingKey和RoutingKey
BindingKey是Exchange和Queue绑定的规则描述，这个描述用来解析当Exchange接收到消息时，Exchange接收到的消息会带有RoutingKey这个字段，Exchange就是根据这个RoutingKey和当前Exchange所有绑定的BindingKey做匹配，如果满足要求，就往BindingKey所绑定的Queue发送消息，这样我们就解决了我们向RabbitMQ发送一次消息，可以分发到不同的Queue的过程.

## 交换机类型
####  direct交换机类型
把消息路由到那些 Bindingkey 与 RoutingKey 完全匹配的 Queue 中。

direct 类型常用在处理有优先级的任务，根据任务的优先级把消息发送到对应的队列，这样可以指派更多的资源去处理高优先级的队列。

####  topic交换机类型
与 direct 类型的交换器相似，也是将消息路由到 BindingKey 和 RoutingKey 相匹配的队列中，但这里的匹配规则有些不同，它约定：

* RoutingKey 为一个点号“．”分隔的字符串（被点号“．”分隔开的每一段独立的字符串称为一个单词），如 “com.rabbitmq.client”、“java.util.concurrent”、“com.hidden.client”;
* BindingKey 和 RoutingKey 一样也是点号“．”分隔的字符串；
* BindingKey 中可以存在两种特殊字符串`*`和`#`，用于做模糊匹配，其中`*`用于匹配一个单词，`#`用于匹配多个单词(可以是零个)。

这里有个奇怪的点：我生产者新定义了一个queue，但是尚未向这个队列中发送消息，这时候通过控制台查看queue数量，不存在我刚创建的队列。
同样的，消费者监听这个queue时候启动报错说queue不存在。
只有真正的调用convertAndSend方法进行发送消息时候，才会将定义的queue、及queue和exchange的绑定信息同步到消息队列中去。
这一点不知道是由于代码的逻辑造成的还是rabbitmq的机制导致的。

####  fanout交换机类型
把所有发送到该Exchange的消息路由到所有与它绑定的Queue中，不需要做任何判断操作，所以 fanout 类型是所有的交换机类型里面速度最快的。
fanout 类型常用来广播消息。

## 生产者回调
#### ConfirmCallback
消息是推送成功到服务器即为true
#### ReturnCallback
推送成功到服务器，且没有异常情况发生即为true

