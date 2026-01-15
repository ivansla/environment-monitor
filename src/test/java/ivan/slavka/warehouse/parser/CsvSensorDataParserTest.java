package ivan.slavka.warehouse.parser;

import ivan.slavka.environmentmonitor.common.SensorData;
import ivan.slavka.environmentmonitor.warehouse.parser.CsvSensorDataParser;
import ivan.slavka.environmentmonitor.warehouse.parser.SensorDataParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class CsvSensorDataParserTest {

    private SensorDataParser sut = new CsvSensorDataParser();

    @Test
    void givenValidData_whenParseData_thenSuccess() {
        String data = "sensor_id=h1; value=10";

        Optional<SensorData> result = sut.parseData(data.getBytes());
        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    void givenInvalidNumericData_whenParseData_thenSuccess() {
        String data = "sensor_id=h1; value=asdf";

        Optional<SensorData> result = sut.parseData(data.getBytes());
        Assertions.assertThat(result.isPresent()).isFalse();
    }
}
