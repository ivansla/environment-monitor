package ivan.slavka.environmentmonitor.central.service;

import ivan.slavka.environmentmonitor.common.mq.MqReceiver;

public class CentralDataProcessorFactory {

    private final MqReceiver mqReceiver;

    public CentralDataProcessorFactory(MqReceiver mqReceiver) {
        this.mqReceiver = mqReceiver;
    }

    public CentralDataProcessor getCentralDataProcessor(String inputQueue) {
        return new CentralDataProcessor(inputQueue, mqReceiver);
    }
}
