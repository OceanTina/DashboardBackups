package kafka;

public class KafkaClient
{
    public static void main(String[] args)
    {
        // 模拟NetCare Lite发送采集开始消息
        NetCareAcquisitionProducer netCarePro = new NetCareAcquisitionProducer();
        netCarePro.start();
        // 模拟Dashboard接收消息并回执消息
        WdmAcquisitionConsumer wdmCon = new WdmAcquisitionConsumer();
        wdmCon.start();
        // 模拟NetCare Lite接收采集完成消息
        NetCareAcquisitionConsumer netCareCon = new NetCareAcquisitionConsumer();
        netCareCon.start();
    }
}
