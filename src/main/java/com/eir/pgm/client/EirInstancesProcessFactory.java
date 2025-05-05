package com.eir.pgm.client;

import com.eir.pgm.services.SystemConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EirInstancesProcessFactory {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationContext context;

    @Autowired
    private SystemConfigurationService config;

    private Map<String, InstanceProcessService> map = new ConcurrentHashMap<>();

    private Map<String, InstanceUrlDelegate> operatorUrlMap = new ConcurrentHashMap<>();

    public void init() throws RuntimeException {

        for (String instance : config.getActiveInstances()) {
            InstanceProcessService process = context.getBean(InstanceProcessService.class);
            process.setInstance(instance);
            map.put(instance, process);

            InstanceUrlDelegate operatorUrlService = context.getBean(InstanceUrlDelegate.class);
            operatorUrlService.setInstance(instance);
            operatorUrlService.init();

            process.setOperatorUrlService(operatorUrlService);
            log.info("Instance:{} , Process:{} In Factory", instance, process);
            process.init();
        }
    }

}
