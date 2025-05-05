package com.eir.pgm.services;

import com.eir.pgm.alert.AlertConfig;
import com.eir.pgm.config.AppConfig;
import com.eir.pgm.repository.ConfigRepository;
import com.eir.pgm.repository.entity.SystemConfigKeys;
import com.eir.pgm.repository.entity.ProvisionGatewayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigRepository repository;

    @Autowired
    AlertConfig alertConfig;

    List<String> eirInstances = new ArrayList<>();

    @Override
    public synchronized List<String> getActiveInstances() {
        if (CollectionUtils.isEmpty(eirInstances)) {
            Integer noOfOperators = findByKey(SystemConfigKeys.PGM_NO_OF_INSTANCES, 0);
            for (int i = 1; i <= noOfOperators; i++) {
                eirInstances.add(findByKey(SystemConfigKeys.PGM_INSTANCE.replaceAll("<NUMBER>", String.valueOf(i))));
            }
        }
        return eirInstances;
    }

    public String findByKey(String key) throws RuntimeException {
        Optional<ProvisionGatewayConfig> optional = repository.findByConfigKeyAndModule(key, alertConfig.getProcessId());
        if (optional.isPresent()) {
            log.info("Filled key:{} value:{}", key, optional.get().getConfigValue());
            return optional.get().getConfigValue();
        } else {
            log.info("Value for key:{} Not Found", key);
            throw new RuntimeException("Config Key:" + key + ", value not found");
        }
    }


    public Integer findByKey(String key, int defaultValue) {
        try {
            String value = findByKey(key);
            return Integer.parseInt(value);
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public String findByKey(String key, String defaultValue) {
        try {
            return findByKey(key);
        } catch (RuntimeException e) {
            return defaultValue;
        }

    }

    public Float findByKey(String key, float defaultValue) {
        try {
            String value = findByKey(key);
            return Float.parseFloat(value);
        } catch (RuntimeException e) {
            log.warn("");
            return defaultValue;
        }
    }
}
