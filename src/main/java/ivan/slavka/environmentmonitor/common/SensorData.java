package ivan.slavka.environmentmonitor.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@ToString
public class SensorData implements Serializable {

    private String sensorId;
    private int sensorValue;
}
