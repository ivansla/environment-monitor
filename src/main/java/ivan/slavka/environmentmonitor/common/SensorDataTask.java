package ivan.slavka.environmentmonitor.common;

import ivan.slavka.environmentmonitor.warehouse.ServiceControl;
import lombok.Setter;

public abstract class SensorDataTask implements Runnable {

    @Setter
    protected ServiceControl serviceControl;

    public abstract void stop();
}
