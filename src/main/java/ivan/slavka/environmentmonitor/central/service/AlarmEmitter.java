package ivan.slavka.environmentmonitor.central.service;

import ivan.slavka.environmentmonitor.common.SensorData;

public interface AlarmEmitter {
    void raiseAlarm(SensorData sensorData);
}
