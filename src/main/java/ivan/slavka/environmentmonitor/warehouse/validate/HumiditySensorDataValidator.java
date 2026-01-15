package ivan.slavka.environmentmonitor.warehouse.validate;

import ivan.slavka.environmentmonitor.common.SensorData;

public class HumiditySensorDataValidator implements SensorDataValidator {
    @Override
    public boolean validate(SensorData sensorData) {
        return sensorData.getSensorId().charAt(0) == 'h';
    }
}
