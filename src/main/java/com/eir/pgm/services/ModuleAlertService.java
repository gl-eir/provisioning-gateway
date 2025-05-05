package com.eir.pgm.services;

import com.eir.pgm.alert.AlertConfig;
import com.eir.pgm.alert.AlertConfigDto;
import com.eir.pgm.alert.AlertDto;
import com.eir.pgm.alert.AlertService;
import com.eir.pgm.constants.AlertIds;
import com.eir.pgm.constants.AlertMessagePlaceholders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ModuleAlertService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AlertService alertService;

    public void sendAlert(AlertIds alertIds, Map<AlertMessagePlaceholders, String> placeHolderMap) {
        alertService.sendAlert(alertIds, placeHolderMap);
    }
}
