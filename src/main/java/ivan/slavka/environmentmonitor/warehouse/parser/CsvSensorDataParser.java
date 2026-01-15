package ivan.slavka.environmentmonitor.warehouse.parser;

import ivan.slavka.environmentmonitor.common.SensorData;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class CsvSensorDataParser implements SensorDataParser {

    private static final String SENSOR_ID_FIELD = "sensor_id";
    private static final String VALUE_FIELD = "value";

    @Override
    public Optional<SensorData> parseData(byte[] rawData) {
        String data = cleanMessage(rawData);
        Properties p = new Properties();
        try {
            p.load(new StringReader(data.replace(";", "\n")));
        } catch (IOException e) {
            log.error("Failed to parse sensor data: {}", data);
        }

        try {
            return Optional.of(SensorData.builder()
                    .sensorId(p.getProperty(SENSOR_ID_FIELD, ""))
                    .sensorValue(Integer.parseInt(p.getProperty(VALUE_FIELD, "0")))
                    .build());
        } catch (Exception e) {
            log.warn("Failed to parse message.", e);
            return Optional.empty();
        }
    }

    private String cleanMessage(byte[] rawData) {
        String rawMessage = new String(rawData);
        log.debug("Received raw message: {}", rawMessage);
        return rawMessage.replace("\n", "");
    }
}
