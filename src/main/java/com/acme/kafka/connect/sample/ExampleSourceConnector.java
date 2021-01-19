
import java.util.ArrayList;
import java.util.Collections; 
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef; 
import org.apache.kafka.config.ConfigValue;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException; 
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.connect.util.ConnectorUtils; 

import static com.acme.kafka.connect.sample.ExampleSourceConnectorConfig..*; 

public class ExampleSourceConnector extends SourceConnector {
    private final Logger log = LoggerFactory.getLogger(ExampleSourceConnector.class);

    private Map<String, String> originalProps; 
    private ExampleSourceConnectorConfig config; 
    private SourceMonitorThread sourceMonitorThread; 

    @Override 
    public String version() {
        return PropertiesUtil.getConnectorVersion();
    }

    @Override 
    public ConfigDef config() {
        return CONFIG_DEF;
    }
}