package ivan.slavka.environmentmonitor.warehouse.service;

import ivan.slavka.environmentmonitor.common.SensorDataTask;
import ivan.slavka.environmentmonitor.common.ThreadUtil;
import ivan.slavka.environmentmonitor.warehouse.ServiceControl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class WarehouseService implements ServiceControl {

    private List<SensorDataTask> sensorDataTasks = new ArrayList<>();

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    private final int numberOfThreads;

    public WarehouseService(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void start() {
        isRunning.set(true);
        try (ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads)) {
            while (isRunning.get()) {
                sensorDataTasks.forEach(executorService::execute);
                ThreadUtil.sleepNoThrow(100);
            }
        }
    }

    @Override
    public void stop() {
        this.sensorDataTasks.stream().forEach(SensorDataTask::stop);
        isRunning.set(false);
    }

    public void registerTask(SensorDataTask sensorDataTask) {
        sensorDataTask.setServiceControl(this);
        this.sensorDataTasks.add(sensorDataTask);
    }
}
