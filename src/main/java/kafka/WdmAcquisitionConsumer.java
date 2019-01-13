package kafka;

import java.util.Arrays;
import java.util.Properties;

public class WdmAcquisitionConsumer extends Thread
{
    public static Properties kafkaProperties;
    public static KafkaProducer<String, String> producer;
    public static KafkaConsumer<String, String> consumer;
    static {
        initKafkaProperties();
        initKafkaProducer();
        initKafkaConsumer();
    }
    @Override
    public void run()
    {
        while (true)
        {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
            {
                try
                {
                    // 发送采集完成消息
                    if(record.value() != null && !record.value().equals("")){
                        JSONObject json = JSONObject.fromObject(record.value());
                        String uuidList = json.getString("uuidList");
                        System.out.printf("topic = %s, partition = %d, offset = %d, key = %s, value = %s%n", record.topic(), record.partition(), record.offset(), record.key(), uuidList);
                        // TODO 调用采集接口
                        // TODO 上传文件至HDFS
                        // 发送回执消息
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("status", "1");
                        map.put("failureRootCause", "");
                        map.put("HDFSPath", "/opt/oss/xxx");
                        String respStr = JSONObject.fromObject(map).toString();
                        producer.send(new ProducerRecord<String, String>("wireless.finish.acquisition", "wireless.finish.acquisition", respStr));
                        //Thread.sleep(1000);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void initKafkaProperties() {
        try {
            InputStream in = WdmAcquisitionConsumer.class.getClassLoader()
                    .getResourceAsStream("kafka/kafka.properties");
            kafkaProperties = new Properties();
            kafkaProperties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaProperties.get("bootstrap.servers"));
        props.put("acks", kafkaProperties.get("acks"));
        props.put("retries", kafkaProperties.get("retries"));
        props.put("batch.size",kafkaProperties.get("batch.size"));
        props.put("linger.ms", kafkaProperties.get("linger.ms"));
        props.put("buffer.memory", kafkaProperties.get("buffer.memory"));
        props.put("key.serializer", kafkaProperties.get("key.serializer"));
        props.put("value.serializer", kafkaProperties.get("value.serializer"));

        producer = new KafkaProducer<String, String>(props);
    }
    private static void initKafkaConsumer() {

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaProperties.get("bootstrap.servers"));
        props.put("group.id", kafkaProperties.get("group.id"));
        props.put("enable.auto.commit", kafkaProperties.get("enable.auto.commit"));
        props.put("auto.commit.interval.ms", kafkaProperties.get("auto.commit.interval.ms"));
        props.put("key.deserializer", kafkaProperties.get("key.deserializer"));
        props.put("value.deserializer",  kafkaProperties.get("value.deserializer"));

        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(kafkaProperties.getProperty("subscribedTopic")));
