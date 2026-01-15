package ivan.slavka.environmentmonitor.warehouse.service;

import ivan.slavka.environmentmonitor.common.SensorDataSerializer;
import ivan.slavka.environmentmonitor.common.mq.MqPublisher;
import ivan.slavka.environmentmonitor.common.mq.MqReceiver;
import ivan.slavka.environmentmonitor.warehouse.parser.SensorDataParser;
import ivan.slavka.environmentmonitor.warehouse.validate.SensorDataValidator;

public class SensorDataProcessorFactory {

    private final MqPublisher mqPublisher;
    private final MqReceiver mqReceiver;
    private final SensorDataSerializer sensorDataSerializer = new SensorDataSerializer();

    public SensorDataProcessorFactory(MqPublisher mqPublisher, MqReceiver mqReceiver) {
        this.mqPublisher = mqPublisher;
        this.mqReceiver = mqReceiver;
    }

    public SensorDataProcessor getSensorDataProcessor(String inputQueue, String outputQueue, SensorDataParser sensorDataParser, SensorDataValidator sensorDataValidator) {
        return new SensorDataProcessor(inputQueue, outputQueue, mqPublisher, mqReceiver, sensorDataParser, sensorDataSerializer, sensorDataValidator);
    }
}
