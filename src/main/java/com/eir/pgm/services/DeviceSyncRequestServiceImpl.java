package com.eir.pgm.services;

import com.eir.pgm.repository.DeviceSyncRequestRepository;
import com.eir.pgm.repository.entity.DevicePriority;
import com.eir.pgm.repository.entity.DeviceSyncRequest;
import com.eir.pgm.repository.entity.DeviceSyncRequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeviceSyncRequestServiceImpl implements DeviceSyncRequestService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DeviceSyncRequestRepository repository;

    @Override
    public DeviceSyncRequest save(DeviceSyncRequest request) {
        return repository.save(request);
    }

    @Override
    public void saveAll(List<DeviceSyncRequest> requests) {
        repository.saveAll(requests);
    }

    @Override
    public List<DeviceSyncRequest> getNewRequests(String operator, DevicePriority devicePriority) {
        log.info("Going to Query New Requests for instance:{} devicePriority:{}[{}]", operator, devicePriority.name(), devicePriority.getPriority());
        List<DeviceSyncRequest> requests = repository.findRequests(devicePriority.getPriority(), operator, Collections.singletonList(DeviceSyncRequestStatus.NEW));
        log.info("Result for New Requests for instance:{} devicePriority:{}[{}] RequestsSize:{}", operator, devicePriority.name(), devicePriority.getPriority(), (requests == null ? 0 : requests.size()));
        return requests;
    }

    @Override
    public List<DeviceSyncRequest> getRetryRequest(String operator, DevicePriority devicePriority) {
        log.info("Going to Query Retries Requests for instance:{} devicePriority:{}[{}]", operator, devicePriority.name(), devicePriority.getPriority());
        List<DeviceSyncRequestStatus> statuses = new ArrayList<>();
        statuses.add(DeviceSyncRequestStatus.FAILED);
        statuses.add(DeviceSyncRequestStatus.CONNECTION_FAILED);
        List<DeviceSyncRequest> requests = repository.findRequests(devicePriority.getPriority(), operator, statuses);
        log.info("Result for Retries Requests for instance:{} devicePriority:{}[{}] RequestsSize:{}", operator, devicePriority.name(), devicePriority.getPriority(), (requests == null ? 0 : requests.size()));
        return requests;
    }

    @Override
    public void delete(DeviceSyncRequest request) {
        repository.delete(request);
    }
}
