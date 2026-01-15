package ivan.slavka.environmentmonitor.central.service;

import ivan.slavka.environmentmonitor.common.SensorData;
import org.apache.commons.lang3.StringUtils;

public class TemperatureSensorDataHandler implements SensorDataHandler {

    private final AlarmEmitter alarmEmitter;
    private final int sensorThreshold;

    public TemperatureSensorDataHandler(int sensorThreshold, AlarmEmitter alarmEmitter) {
        this.alarmEmitter = alarmEmitter;
        this.sensorThreshold = sensorThreshold;
    }

    @Override
    public boolean isMessageValid(SensorData sensorData) {
        if (StringUtils.isEmpty(sensorData.getSensorId())) {
            return false;
        }
        return sensorData.getSensorId().charAt(0) == 't';
    }

    @Override
    public void handleMeasurement(SensorData sensorData) {
        if (sensorData.getSensorValue() > sensorThreshold) {
            alarmEmitter.raiseAlarm(sensorData);
        }
    }
}
