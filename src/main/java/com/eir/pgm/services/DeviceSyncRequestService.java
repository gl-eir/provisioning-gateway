package com.eir.pgm.services;


import com.eir.pgm.repository.entity.DevicePriority;
import com.eir.pgm.repository.entity.DeviceSyncRequest;

import java.util.List;

public interface DeviceSyncRequestService {

    DeviceSyncRequest save(DeviceSyncRequest request);

    void saveAll(List<DeviceSyncRequest> requests);

    List<DeviceSyncRequest> getNewRequests(String operator, DevicePriority priority);

    List<DeviceSyncRequest> getRetryRequest(String operator, DevicePriority priority);

    void delete(DeviceSyncRequest request);

}
