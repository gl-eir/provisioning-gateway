package com.eir.pgm;

import com.eir.pgm.alert.AlertConfig;
import com.eir.pgm.alert.AlertConfigDto;
import com.eir.pgm.alert.AlertDto;
import com.eir.pgm.alert.AlertServiceImpl;
import com.eir.pgm.client.EirInstancesProcessFactory;
import com.eir.pgm.constants.AlertIds;
import com.eir.pgm.repository.entity.SystemConfigKeys;
import com.eir.pgm.services.SystemConfigurationService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class ProvisioningGatewayApplication {

    private static final Logger logger = LoggerFactory.getLogger(ProvisioningGatewayApplication.class);

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(ProvisioningGatewayApplication.class, args);
        validationAlertsConfig(context);
        //        System.setProperty("javax.net.debug", "ssl:handshake");
        if (CollectionUtils.isEmpty(context.getBean(SystemConfigurationService.class).getActiveInstances()))
            throw new RuntimeException("config missing [" + SystemConfigKeys.PGM_NO_OF_INSTANCES + "] OR [" + SystemConfigKeys.PGM_INSTANCE + "]");


        context.getBean(EirInstancesProcessFactory.class).init();
    }

    private static void validationAlertsConfig(ApplicationContext context) {
        AlertConfig alertConfig = context.getBean(AlertConfig.class);
        if (StringUtils.isBlank(alertConfig.getPostUrl())) {
            logger.error("Alerts Alert's URL Configuration missing");
            System.exit(1);
        } else if (StringUtils.isBlank(alertConfig.getProcessId())) {
            logger.error("Alerts Alert's Process Id Configuration missing");
            System.exit(1);
        } else {
            Map<AlertIds, AlertConfigDto> requiredAlerts = context.getBean(AlertConfig.class).getAlertsMapping();
            if (requiredAlerts == null) {
                logger.error("Alerts Configuration missing");
                System.exit(1);
            } else {
                for (AlertIds alertId : AlertIds.values()) {
                    AlertConfigDto alertConfigDto = requiredAlerts.get(alertId);
                    if (alertConfigDto == null) {
                        logger.error("AlertsId:{} Configuration missing", alertId);
                        System.exit(1);
                    } else {
                        if (StringUtils.isBlank(alertConfigDto.getAlertId()) || StringUtils.isBlank(alertConfigDto.getMessage())) {
                            logger.error("AlertsId or Message  Configuration missing for alertId:{}", alertId);
                            System.exit(1);
                        }
                    }
                }
            }
        }
    }
}
