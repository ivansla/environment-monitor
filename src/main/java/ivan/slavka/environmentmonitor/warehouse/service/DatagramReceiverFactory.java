package ivan.slavka.environmentmonitor.warehouse.service;

import ivan.slavka.environmentmonitor.common.mq.MqPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Optional;

public class DatagramReceiverFactory {

    private static final Logger log = LoggerFactory.getLogger(DatagramReceiverFactory.class);

    private final MqPublisher mqPublisher;

    public DatagramReceiverFactory(MqPublisher mqPublisher) {
        this.mqPublisher = mqPublisher;
    }

    public Optional<DatagramReceiver> getDatagramReceiver(String outputQueueName, SocketAddress socketAddress) {
        try {
            DatagramChannel channel = DatagramChannel.open().bind(socketAddress);
            channel.configureBlocking(false);
            return Optional.of(createDatagramReceiver(outputQueueName, channel, mqPublisher));
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        return Optional.empty();
    }

    protected DatagramReceiver createDatagramReceiver(String outputQueueName, DatagramChannel channel, MqPublisher mqPublisher) {
        return new DatagramReceiver(outputQueueName, channel, mqPublisher);
    }
}
