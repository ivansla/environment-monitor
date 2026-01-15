package ivan.slavka.environmentmonitor.common.mq;

import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import ivan.slavka.environmentmonitor.common.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MqReceiver implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(MqReceiver.class);

    private Connection connection;
    private Channel channel;
    private final String host;
    private final ConnectionFactory connectionFactory;

    private static MqReceiver instance;
    private AtomicBoolean isConnectionEstablished = new AtomicBoolean(false);

    private final int numberOfRetries;

    public MqReceiver(String host, int numberOfRetries) {
        this.host = host;
        this.numberOfRetries = numberOfRetries;

        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);

        if (!establishConnection()) {
            log.error("Failed to instantiate SensorData publisher. Host: {}", host);
            throw new RuntimeException("Failed to instantiate SensorData publisher");
        }
    }

    public static MqReceiver getInstance(String host, int numberOfRetries) {
        if (instance == null) {
            instance = new MqReceiver(host, numberOfRetries);
        }
        return instance;
    }

    public void receive(String queueName, SensorDataProcessor dataProcessor) {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            dataProcessor.process(delivery.getBody());
        };

        try {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (AlreadyClosedException | IOException e) {
            isConnectionEstablished.set(false);
            int i = 0;
            while (i < numberOfRetries) {
                try {
                    ThreadUtil.sleepNoThrow(1000);
                    if (establishConnection()) {
                        channel.queueDeclare(queueName, false, false, false, null);
                        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                        });
                        return;
                    }
                } catch (IOException e1) {
                    i++;
                    log.warn("Retrying to receieve data. Number of attempts: {} of {}", i, numberOfRetries);
                }
            }
        }
    }

    private synchronized boolean establishConnection() {
        try {
            if (isConnectionEstablished.get()) {
                return true;
            }

            if (connection != null) {
                connection.close();
            }

            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
            isConnectionEstablished.set(true);
            return true;
        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
