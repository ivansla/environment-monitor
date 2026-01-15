package ivan.slavka.environmentmonitor.warehouse.service;

import ivan.slavka.environmentmonitor.common.mq.MqPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ControlDatagramReceiver extends DatagramReceiver {

    private static final Logger log = LoggerFactory.getLogger(ControlDatagramReceiver.class);

    private final MqPublisher mqPublisher;
    private final String queueName;

    public ControlDatagramReceiver(String queueName, DatagramChannel channel, MqPublisher mqPublisher) {
        super(queueName, channel, mqPublisher);
        this.mqPublisher = mqPublisher;
        this.queueName = queueName;
    }

    @Override
    protected void publishMessage(ByteBuffer buffer) {
        log.debug("Received control message.");
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        if (new String(bytes).contains("end")) {
            mqPublisher.tryPublish(queueName, bytes);
            this.serviceControl.stop();
        }
    }
}
