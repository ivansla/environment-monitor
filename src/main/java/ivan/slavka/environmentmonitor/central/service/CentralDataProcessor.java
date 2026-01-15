package ivan.slavka.environmentmonitor.central.service;

import ivan.slavka.environmentmonitor.common.SensorDataDeserializer;
import ivan.slavka.environmentmonitor.common.SensorDataTask;
import ivan.slavka.environmentmonitor.common.mq.MqReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class CentralDataProcessor extends SensorDataTask {

    private static final Logger log = LoggerFactory.getLogger(CentralDataProcessor.class);

    private final String inputQueue;
    private final MqReceiver mqReceiver;
    private final List<SensorDataHandler> sensorDataHandlers = new ArrayList<>();
    private final SensorDataDeserializer sensorDataDeserializer = new SensorDataDeserializer();

    public CentralDataProcessor(String inputQueue,
                                MqReceiver mqReceiver) {
        this.inputQueue = inputQueue;
        this.mqReceiver = mqReceiver;
    }

    @Override
    public void run() {
        mqReceiver.receive(inputQueue, data -> {
            sensorDataDeserializer.deserializeData(data)
                    .map(peek(sensorData -> log.debug("Sensor data: {}", sensorData)))
                    .ifPresent(sensorData -> sensorDataHandlers.stream()
                            .filter(h -> h.isMessageValid(sensorData))
                            .findFirst()
                            .ifPresent(h -> h.handleMeasurement(sensorData)));
        });
    }

    public void addSensorDataHandler(SensorDataHandler sensorDataHandler) {
        this.sensorDataHandlers.add(sensorDataHandler);
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
