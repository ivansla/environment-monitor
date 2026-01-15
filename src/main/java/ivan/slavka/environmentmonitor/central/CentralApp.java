package ivan.slavka.environmentmonitor.central;

import ivan.slavka.environmentmonitor.central.service.CentralDataProcessor;
import ivan.slavka.environmentmonitor.central.service.CentralDataProcessorFactory;
import ivan.slavka.environmentmonitor.central.service.CentralService;
import ivan.slavka.environmentmonitor.central.service.HumiditySensorDataHandler;
import ivan.slavka.environmentmonitor.central.service.LogAlarmEmitter;
import ivan.slavka.environmentmonitor.central.service.TemperatureSensorDataHandler;
import ivan.slavka.environmentmonitor.common.mq.MqPublisher;
import ivan.slavka.environmentmonitor.common.mq.MqReceiver;

public class CentralApp {

    private static final int NUMBER_OF_THREADS = 1;
    private static final int HUMIDITY_THRESHOLD = 50;
    private static final int TEMPERATURE_THRESHOLD = 35;

    public static void main(String[] args) throws Exception {

        MqPublisher mqPublisher = MqPublisher.getInstance("localhost", 3);
        MqReceiver mqReceiver = MqReceiver.getInstance("localhost", 3);
        CentralDataProcessorFactory centralDataProcessorFactory = new CentralDataProcessorFactory(mqReceiver);

        CentralService centralService = configureCentralService(centralDataProcessorFactory);
        centralService.start();

        mqReceiver.close();
        mqPublisher.close();
    }

    private static CentralService configureCentralService(CentralDataProcessorFactory centralDataProcessorFactory) {
        CentralService centralService = new CentralService(NUMBER_OF_THREADS);
        CentralDataProcessor centralDataProcessor = centralDataProcessorFactory.getCentralDataProcessor("central.in");
        centralDataProcessor.addSensorDataHandler(new TemperatureSensorDataHandler(TEMPERATURE_THRESHOLD, new LogAlarmEmitter()));
        centralDataProcessor.addSensorDataHandler(new HumiditySensorDataHandler(HUMIDITY_THRESHOLD, new LogAlarmEmitter()));
        centralService.registerTask(centralDataProcessor);
        return centralService;
    }
}
