package ivan.slavka.environmentmonitor.central.service;

import ivan.slavka.environmentmonitor.common.SensorData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogAlarmEmitter implements AlarmEmitter {
    @Override
    public void raiseAlarm(SensorData sensorData) {
        log.warn("Sensor with id: {}, over threshold: {}", sensorData.getSensorId(), sensorData.getSensorValue());
    }
}
