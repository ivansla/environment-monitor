package ivan.slavka.environmentmonitor.warehouse.service;

import ivan.slavka.environmentmonitor.common.SensorDataSerializer;
import ivan.slavka.environmentmonitor.common.SensorDataTask;
import ivan.slavka.environmentmonitor.common.mq.MqPublisher;
import ivan.slavka.environmentmonitor.common.mq.MqReceiver;
import ivan.slavka.environmentmonitor.warehouse.parser.SensorDataParser;
import ivan.slavka.environmentmonitor.warehouse.validate.SensorDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SensorDataProcessor extends SensorDataTask {

    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessor.class);

    private final String inputQueue;
    private final String outputQueue;
    private final MqPublisher mqPublisher;
    private final MqReceiver mqReceiver;
    private final SensorDataParser sensorDataParser;
    private final SensorDataSerializer sensorDataSerializer;
    private final SensorDataValidator sensorDataValidator;

    public SensorDataProcessor(String inputQueue,
                               String outputQueue,
                               MqPublisher mqPublisher,
                               MqReceiver mqReceiver,
                               SensorDataParser sensorDataParser,
                               SensorDataSerializer sensorDataSerializer,
                               SensorDataValidator sensorDataValidator) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;

        this.mqReceiver = mqReceiver;
        this.mqPublisher = mqPublisher;
        this.sensorDataParser = sensorDataParser;
        this.sensorDataSerializer = sensorDataSerializer;
        this.sensorDataValidator = sensorDataValidator;
    }

    @Override
    public void run() {
        mqReceiver.receive(inputQueue, data -> {
            sensorDataParser.parseData(data)
                    .map(peek(sensorData -> log.debug("Sensor data: {}", sensorData)))
                    .filter(sensorDataValidator::validate)
                    .flatMap(sensorDataSerializer::serializeSensorData)
                    .ifPresent(b -> mqPublisher.tryPublish(outputQueue, b));
        });
    }

    @Override
    public void stop() {
        // does nothing
    }

    <T> UnaryOperator<T> peek(Consumer<T> c) {
        return x -> {
            c.accept(x);
            return x;
        };
    }
}
