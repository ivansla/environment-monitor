package ivan.slavka.environmentmonitor.warehouse.parser;

import ivan.slavka.environmentmonitor.common.SensorData;

import java.util.Optional;

public interface SensorDataParser {

    Optional<SensorData> parseData(byte[] rawData);
}
