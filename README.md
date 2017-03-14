# Disruptor(无锁并发框架)
Disruptor 是实现了“队列”的功能，而且是一个有界队列。那么它的应用场景自然就是“生产者-消费者”模型的应用场合  

# 核心概念

* Ring Buffer
如其名，环形的缓冲区。曾经 RingBuffer 是 Disruptor 中的最主要的对象，但从3.0版本开始，其职责被简化为仅仅负责对通过 Disruptor 进行交换的数据（事件）进行存储和更新。在一些更高级的应用场景中，Ring Buffer 可以由用户的自定义实现来完全替代。
* Sequence  Disruptor
通过顺序递增的序号来编号管理通过其进行交换的数据（事件），对数据(事件)的处理过程总是沿着序号逐个递增处理。一个 Sequence 用于跟踪标识某个特定的事件处理者( RingBuffer/Consumer )的处理进度。虽然一个 AtomicLong 也可以用于标识进度，但定义 Sequence 来负责该问题还有另一个目的，那就是防止不同的 Sequence 之间的CPU缓存伪共享(Flase Sharing)问题。
（注：这是 Disruptor 实现高性能的关键点之一，网上关于伪共享问题的介绍已经汗牛充栋，在此不再赘述）。
* Sequencer 
Sequencer 是 Disruptor 的真正核心。此接口有两个实现类 SingleProducerSequencer、MultiProducerSequencer ，它们定义在生产者和消费者之间快速、正确地传递数据的并发算法。
* Sequence Barrier
用于保持对RingBuffer的 main published Sequence 和Consumer依赖的其它Consumer的 Sequence 的引用。 Sequence Barrier 还定义了决定 Consumer 是否还有可处理的事件的逻辑。
* Wait Strategy
定义 Consumer 如何进行等待下一个事件的策略。 （注：Disruptor 定义了多种不同的策略，针对不同的场景，提供了不一样的性能表现）
* Event
在 Disruptor 的语义中，生产者和消费者之间进行交换的数据被称为事件(Event)。它不是一个被 Disruptor 定义的特定类型，而是由 Disruptor 的使用者定义并指定。
* EventProcessor
EventProcessor 持有特定消费者(Consumer)的 Sequence，并提供用于调用事件处理实现的事件循环(Event Loop)。
* EventHandler
Disruptor 定义的事件处理接口，由用户实现，用于处理事件，是 Consumer 的真正实现。
* Producer
即生产者，只是泛指调用 Disruptor 发布事件的用户代码，Disruptor 没有定义特定接口或类型。

# 流程图(收集)
![Disruptor](disruptor.png)

# 测试代码
`生产者`发布交易订单详情数据  
`消费者`依次进行业务处理，DB写入，消息通知

# 总结
* 队列使用数组结构，而不是使用传统的链表结构，寻址更快
* 新生产的对象采用覆盖的方式(不是传统阻塞队列，删除->添加的逻辑)，减少GC回收的负担
* 从CPU层面优化，对Sequencer进行内存分配补齐，消除Java伪共享(cpu缓存行)
* 多个线程同时访问，由于他们都通过序号器Sequencer访问ringBuffer，通过CAS取代了加锁和同步块，这也是并发编程的一个指导性原则：把同步块最小化到一个变量上。

# 相关连接
* Disruptor 极速体验: [http://www.cnblogs.com/haiq/p/4112689.html](http://www.cnblogs.com/haiq/p/4112689.html)
* Disruptor 学习分享: [http://www.jianshu.com/p/d3e5915a7ac5](http://www.jianshu.com/p/d3e5915a7ac5)

# 问题反馈
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流

* 邮件: (zhou5827297@163.com)
