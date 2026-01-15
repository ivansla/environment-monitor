package ivan.slavka.environmentmonitor.warehouse.validate;

import ivan.slavka.environmentmonitor.common.SensorData;

public interface SensorDataValidator {
    boolean validate(SensorData sensorData);
}
