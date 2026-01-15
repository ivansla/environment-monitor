package ivan.slavka.environmentmonitor.warehouse.service;

import ivan.slavka.environmentmonitor.common.mq.MqPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.DatagramChannel;

public class ControlDatagramReceiverFactory extends DatagramReceiverFactory {

    private static final Logger log = LoggerFactory.getLogger(ControlDatagramReceiverFactory.class);

    public ControlDatagramReceiverFactory(MqPublisher mqPublisher) {
        super(mqPublisher);
    }

    @Override
    protected DatagramReceiver createDatagramReceiver(String outputQueueName, DatagramChannel channel, MqPublisher mqPublisher) {
        return new ControlDatagramReceiver(outputQueueName, channel, mqPublisher);
    }
}
