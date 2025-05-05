package com.eir.pgm.client;

import com.eir.pgm.client.dto.InstanceResponseDTO;
import com.eir.pgm.mapper.BeansMapper;
import com.eir.pgm.repository.entity.DevicePriority;
import com.eir.pgm.repository.entity.DeviceSyncRequest;
import com.eir.pgm.repository.entity.DeviceSyncRequestStatus;
import com.eir.pgm.repository.entity.SystemConfigKeys;
import com.eir.pgm.services.DeviceSyncRequestService;
import com.eir.pgm.services.SystemConfigurationService;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Scope("prototype")
public class InstanceProcessService {
    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Logger csvFileLogger = LoggerFactory.getLogger("syncedRequestFileLogger");

    private String instance;

    private InstanceUrlDelegate operatorUrlService;

    @Autowired
    private DeviceSyncRequestService operatorRequestService;

//    @Autowired
//    private OperatorExecutor executor;

    @Autowired
    private SystemConfigurationService config;

    @Autowired
    private BeansMapper beansMapper;

    RateLimiter rateLimiter;

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public void setOperatorUrlService(InstanceUrlDelegate operatorUrlService) {
        this.operatorUrlService = operatorUrlService;
    }

    public void init() {
        new Thread(() -> consume()).start();
    }

    private void consume() {
        String tpsKey = SystemConfigKeys.INSTANCE_TPS.replaceAll("<INSTANCE_NAME>", instance);
        Integer tps = config.findByKey(tpsKey, SystemConfigKeys.DEFAULT_INSTANCE_TPS);
        rateLimiter = RateLimiter.create(tps);
//        log.info("Operator:{}, Process:{} Executor:{} TPS-key:{},TPS-value:{}", operator, this, executor, tpsKey, tps);
        log.info("Instance:{}, Process:{} TPS-key:{},TPS-value:{}", instance, this, tpsKey, tps);
        log.info("Process started Instance:{} , Process:{} TPS-key:{},TPS-value:{} ", instance, this, tpsKey, tps);
        while (true) {
            try {
                List<DeviceSyncRequest> requests = operatorRequestService.getRetryRequest(instance, DevicePriority.high);
                if (CollectionUtils.isEmpty(requests)) {
                    requests = operatorRequestService.getNewRequests(instance, DevicePriority.high);
                }

                if (CollectionUtils.isEmpty(requests)) {
                    requests = operatorRequestService.getRetryRequest(instance, DevicePriority.low);
                }

                if (CollectionUtils.isEmpty(requests)) {
                    requests = operatorRequestService.getNewRequests(instance, DevicePriority.low);
                }

                executeWithTPS(requests, tps);
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void executeWithTPS(List<DeviceSyncRequest> requests, Integer tps) {
        try {
            if (!CollectionUtils.isEmpty(requests)) {
                for (DeviceSyncRequest request : requests) {
                    rateLimiter.acquire();
                    process(request);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void process(DeviceSyncRequest request) {
        if (request.getStatus() == DeviceSyncRequestStatus.FAILED || request.getStatus() == DeviceSyncRequestStatus.CONNECTION_FAILED)
            request.setNoOfRetry(request.getNoOfRetry() + 1);

        request.setStatus(DeviceSyncRequestStatus.INIT);

        request = operatorRequestService.save(request);
        request.setSyncRequestTime(LocalDateTime.now());
        InstanceResponseDTO responseDTO = operatorUrlService.callUrl(beansMapper.toInstanceRequestDTO(request));
        request.setSyncResponseTime(responseDTO.getResponseTime());
        request.setFailureReason(responseDTO.getFailureReason());
        request.setResponseStatus(responseDTO.getResponseStatus());
        request.setStatus(responseDTO.getStatus());
        operatorRequestService.save(request);
    }

}
