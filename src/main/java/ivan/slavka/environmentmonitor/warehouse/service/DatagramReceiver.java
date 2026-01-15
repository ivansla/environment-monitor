package ivan.slavka.environmentmonitor.warehouse.service;

import ivan.slavka.environmentmonitor.common.SensorDataTask;
import ivan.slavka.environmentmonitor.common.mq.MqPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class DatagramReceiver extends SensorDataTask {

    private static final Logger log = LoggerFactory.getLogger(DatagramReceiver.class);

    private final String queueName;
    private final DatagramChannel channel;
    private final MqPublisher mqPublisher;
    private final AtomicBoolean isEnabled = new AtomicBoolean(true);

    public DatagramReceiver(String queueName, DatagramChannel channel, MqPublisher mqPublisher) {
        this.channel = channel;
        this.mqPublisher = mqPublisher;
        this.queueName = queueName;
    }

    @Override
    public void run() {
        if (!isEnabled.get()) {
            return;
        }

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            if (channel.receive(buffer) != null) {
                publishMessage(buffer);
            }
        } catch (IOException e) {
            log.warn("Failed to receive message.");
        }
    }

    protected void publishMessage(ByteBuffer buffer) {
        log.debug("Publishing message.");
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        mqPublisher.tryPublish(queueName, bytes);
    }

    @Override
    public void stop() {
        isEnabled.set(false);
        try {
            channel.close();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
    }
}
