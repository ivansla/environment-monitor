package ivan.slavka.environmentmonitor.central.service;

import ivan.slavka.environmentmonitor.common.SensorData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TemperatureSensorDataHandlerTest {

    private SensorDataHandler sut = new TemperatureSensorDataHandler(50, new LogAlarmEmitter());

    @Test
    void givenValidSensorData_whenIsMessageValid_thenSuccess() {
        SensorData sensorData = SensorData.builder()
                .sensorId("t1")
                .sensorValue(30)
                .build();

        boolean result = sut.isMessageValid(sensorData);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void givenMissingSensorId_whenIsMessageValid_thenSuccess() {
        SensorData sensorData = SensorData.builder()
                .sensorId("")
                .sensorValue(30)
                .build();

        boolean result = sut.isMessageValid(sensorData);
        Assertions.assertThat(result).isFalse();
    }
}
