package com.eir.pgm.alert;

import com.eir.pgm.constants.AlertIds;
import com.eir.pgm.constants.AlertMessagePlaceholders;

import java.util.Map;

public interface AlertService {

    void sendAlert(AlertIds alertIds, Map<AlertMessagePlaceholders, String> placeHolderMap);
}
