package ivan.slavka.environmentmonitor.central.service;

import ivan.slavka.environmentmonitor.common.SensorData;

public interface SensorDataHandler {

    boolean isMessageValid(SensorData sensorData);

    void handleMeasurement(SensorData sensorData);
}
