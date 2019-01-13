package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;

import javax.enterprise.inject.spi.Producer;
import java.util.Properties;

public class NetCareAcquisitionProducer extends Thread
{
    private Producer<String, String> producer;
    public NetCareAcquisitionProducer()
    {
        Properties props = new Properties();
        props.put("bootstrap.servers", "100.100.102.163:9092,100.101.112.169:9092,100.101.81.107:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);
    }
    @Override
    public void run()
    {
        for (int i = 0; i < 5; i++) {
            producer.send(new ProducerRecord<String, String>("wireless.start.acquisition", "wireless.start.acquisition", "{\"uuidList\":\"0807a528901d4292abc4b7cd5e551cf0\"}"));
            System.out.println("NetCare Lite send:" + "{\"uuidList\":\"0807a528901d4292abc4b7cd5e551cf0\"}");
        }
        producer.close();
    }
}
