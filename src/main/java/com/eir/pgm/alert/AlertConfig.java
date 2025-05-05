package com.eir.pgm.alert;

import com.eir.pgm.constants.AlertIds;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@Data
@ConfigurationProperties(prefix = "alerts")
public class AlertConfig {

    private String postUrl;

    private String processId;

    private Map<AlertIds, AlertConfigDto> alertsMapping;

}
