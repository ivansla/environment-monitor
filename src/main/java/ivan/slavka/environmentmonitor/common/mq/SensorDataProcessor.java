package ivan.slavka.environmentmonitor.common.mq;

public interface SensorDataProcessor {

    void process(byte[] data);
}
