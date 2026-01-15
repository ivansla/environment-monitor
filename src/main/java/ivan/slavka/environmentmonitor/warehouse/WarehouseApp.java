package ivan.slavka.environmentmonitor.warehouse;

import ivan.slavka.environmentmonitor.common.mq.MqPublisher;
import ivan.slavka.environmentmonitor.common.mq.MqReceiver;
import ivan.slavka.environmentmonitor.warehouse.parser.CsvSensorDataParser;
import ivan.slavka.environmentmonitor.warehouse.service.ControlDatagramReceiverFactory;
import ivan.slavka.environmentmonitor.warehouse.service.DatagramReceiverFactory;
import ivan.slavka.environmentmonitor.warehouse.service.SensorDataProcessorFactory;
import ivan.slavka.environmentmonitor.warehouse.service.WarehouseService;
import ivan.slavka.environmentmonitor.warehouse.validate.HumiditySensorDataValidator;
import ivan.slavka.environmentmonitor.warehouse.validate.TemperatureSensorDataValidator;

import java.net.InetSocketAddress;

public class WarehouseApp {

    private static final int NUMBER_OF_THREADS = 1;
    private static final int HUMIDITY_PORT = 3355;
    private static final int TEMPERATURE_PORT = 3344;
    private static final int CONTROL_PORT = 3366;

    public static void main(String[] args) throws Exception {

        MqPublisher mqPublisher = MqPublisher.getInstance("localhost", 3);
        MqReceiver mqReceiver = MqReceiver.getInstance("localhost", 3);
        DatagramReceiverFactory datagramReceiverFactory = new DatagramReceiverFactory(mqPublisher);
        ControlDatagramReceiverFactory controlDatagramReceiverFactory = new ControlDatagramReceiverFactory(mqPublisher);
        SensorDataProcessorFactory sensorDataProcessorFactory = new SensorDataProcessorFactory(mqPublisher, mqReceiver);

        WarehouseService warehouseService = configureWarehouseService
                (datagramReceiverFactory,
                        controlDatagramReceiverFactory,
                        sensorDataProcessorFactory);

        warehouseService.start();

        mqReceiver.close();
        mqPublisher.close();
    }

    private static WarehouseService configureWarehouseService(DatagramReceiverFactory datagramReceiverFactory,
                                                              ControlDatagramReceiverFactory controlDatagramReceiverFactory,
                                                              SensorDataProcessorFactory sensorDataProcessorFactory) {
        WarehouseService warehouseService = new WarehouseService(NUMBER_OF_THREADS);

        datagramReceiverFactory.getDatagramReceiver("temperature.warehouse", new InetSocketAddress("localhost", TEMPERATURE_PORT))
                .ifPresent(warehouseService::registerTask);
        datagramReceiverFactory.getDatagramReceiver("humidity.warehouse", new InetSocketAddress("localhost", HUMIDITY_PORT))
                .ifPresent(warehouseService::registerTask);
        controlDatagramReceiverFactory.getDatagramReceiver("central.control", new InetSocketAddress("localhost", CONTROL_PORT))
                .ifPresent(warehouseService::registerTask);

        warehouseService.registerTask(
                sensorDataProcessorFactory.getSensorDataProcessor("temperature.warehouse", "central.in",
                        new CsvSensorDataParser(),
                        new TemperatureSensorDataValidator()));
        warehouseService.registerTask(
                sensorDataProcessorFactory.getSensorDataProcessor("humidity.warehouse", "central.in",
                        new CsvSensorDataParser(),
                        new HumiditySensorDataValidator()));

        return warehouseService;
    }
}
