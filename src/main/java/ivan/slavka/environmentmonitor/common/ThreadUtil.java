package ivan.slavka.environmentmonitor.common;

public class ThreadUtil {

    private ThreadUtil() {
    }

    public static void sleepNoThrow(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // swallow this exception - nothing to do
        }
    }
}
