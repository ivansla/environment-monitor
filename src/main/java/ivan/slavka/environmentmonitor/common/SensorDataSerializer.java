package ivan.slavka.environmentmonitor.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class SensorDataSerializer {

    private static final Logger log = LoggerFactory.getLogger(SensorDataSerializer.class);

    public Optional<byte[]> serializeSensorData(SensorData sensorData) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(sensorData);
            return Optional.of(bos.toByteArray());
        } catch (IOException e) {
            log.warn("Failed to serialize sensor data. Data: {}", sensorData, e);
        }
        return Optional.empty();
    }
}
