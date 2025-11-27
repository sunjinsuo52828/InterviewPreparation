# Kafka 消息语义详解

## 1. At Most Once（最多一次）
- 消息可能丢失，但绝不会重复。
- Producer 不重试，Consumer 自动提交 offset。
- 适用场景：对重复极度敏感，但可容忍丢失。

## 2. At Least Once（至少一次）
- 消息不会丢失，但可能会重复。
- Producer 开启重试，Consumer 手动提交 offset（处理完再提交）。
- 适用场景：对丢失不可容忍，但可容忍重复（如账务、计数）。

## 3. Exactly Once（精确一次）
- 依赖 Producer 事务和 Consumer 事务配合（transactional.id、isolation.level）。
- Producer 端配置：
  - enable.idempotence=true
  - transactional.id=your-transactional-id
- Producer 端代码示例：

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("enable.idempotence", "true");
props.put("transactional.id", "my-transactional-producer");
KafkaProducer<String, String> producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());

producer.initTransactions();
try {
    producer.beginTransaction();
    producer.send(new ProducerRecord<>("topic", "key", "value"));
    producer.commitTransaction();
} catch (Exception e) {
    producer.abortTransaction();
}
producer.close();
```

- Consumer 端配置：
  - isolation.level=read_committed
- Consumer 端代码示例：

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("group.id", "my-group");
props.put("isolation.level", "read_committed");
KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props, new StringDeserializer(), new StringDeserializer());

consumer.subscribe(Arrays.asList("topic"));
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
    }
}
```

- 适用场景：既不能丢失，也不能重复（如金融、订单）。

---

### 总结
- At Most Once：可能丢失，不重复
- At Least Once：不丢失，可能重复
- Exactly Once：不丢失，不重复
