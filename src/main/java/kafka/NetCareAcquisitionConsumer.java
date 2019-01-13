package kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class NetCareAcquisitionConsumer extends Thread
{
    private KafkaConsumer<String, String> consumer;
    public NetCareAcquisitionConsumer()
    {
        Properties props = new Properties();
        props.put("bootstrap.servers", "100.100.102.163:9092,100.101.112.169:9092,100.101.81.107:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("wireless.finish.acquisition"));
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
                    if(record.topic() != null){
                        System.out.println("NetCare Lite receive:" + record.value());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
