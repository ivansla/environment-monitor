package ivan.slavka.environmentmonitor.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Optional;

public class SensorDataDeserializer {
    private static final Logger log = LoggerFactory.getLogger(SensorDataDeserializer.class);

    public Optional<SensorData> deserializeData(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return Optional.of((SensorData) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            log.warn(e.getMessage(), e);
        }
        return Optional.empty();
    }
}
