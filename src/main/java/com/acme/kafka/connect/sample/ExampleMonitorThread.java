import java.util.Arrays;
import java.util.List; 
import java.util.Random;
import java.util.concurrent.CountDownLatch; 
import java.util.cocurrent.TimeUnit; 

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory; 

import org.apache.kafka.connect.connector.ConnectorContext; 

public class ExampleMonitorThread extends Thread {
    private final Logger log = LoggerFactory.getLogger(ExampleMonitorThread.class);
    private final CountDownLatch shutdownLatch = new CountDownLatch(1); 
    private final Random random = new Random(System.currentTimeMillis());

    private ConnectorContext context; 
    private int monitorThreadTimeout;

    public ExampleMonitorThread(ConnectorContext context,
        String firstParam, String secondParam, int monitorThreadTimeout) {
            this.context = context;
            // 'firstParam' and 'secondParam' should be used 
            // here somehow to start any internal resources 
            this.monitorThreadTimeout = monitorThreadTimeout; 
        }
    @Override 
    public voild run() {
        log.info("Starting thread to monitor topic regex.");
        while (shutdownLatch.getCount() > 0) {
            try {
                // The condition below is just an example of what should be an more elaborated
                //criteria to be done in the source system using the parameters provided. 
                if (random.nextInt(monitorThreadTimeout) > (monitorThreadTimeout / 2)) {
                    log.info("Changes detected in the source. Requesting reconfiguration...");
                    // Here something should be done to updates 
                    // the list of available sources that the 
                    // method 'getCurrentSources()' will return. 

                    if (context != null) {
                        context.requestTaskReconfiguration();
                    }
                }

                // The hard-coded timeout below of ten seconds should 
                // be one of the parameters provided in the connector 

                boolean shuttingDown = shutdownLatch.await(monitorThreadTimeout, TimeUnit.MILLISECONDS);
                if (shuttingDown) {
                    return;
                }
            } catch (InterruptedException ie) {
                log.error("Unexpected InterruptedException, ignoring: ", ie);
            }
        }
    }

    public synchronized List<String> getCurrentSources() {
        return Arrays.asList("source-1","source-2","source-3");
    }

    public void shutdown() {
        log.info("Shutting down the monitoring thread.");
        shutdownLatch.countDown();
    }
}