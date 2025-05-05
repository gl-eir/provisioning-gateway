package com.eir.pgm.orchestration;

import com.eir.pgm.dtos.DeviceSyncRequestDTO;
import com.eir.pgm.mapper.BeansMapper;
import com.eir.pgm.mapper.DeviceSyncRequestMapper;
import com.eir.pgm.repository.entity.DeviceOperation;
import com.eir.pgm.repository.entity.DeviceSyncRequest;
import com.eir.pgm.repository.entity.DeviceSyncRequestStatus;
import com.eir.pgm.services.DeviceSyncRequestService;
import com.eir.pgm.services.DeviceSyncRequestServiceImpl;
import com.eir.pgm.services.SystemConfigurationService;
import com.eir.pgm.util.ResponseDtoUtil;
import com.eir.pgm.util.dtos.ResponseDto;
import com.eir.pgm.validator.RequestValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListServiceOrchestration {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    DeviceSyncRequestService deviceSyncRequestService;

    @Autowired
    SystemConfigurationService systemConfigService;

    @Autowired
    BeansMapper mapper;

    @Autowired
    RequestValidator requestValidator;

    public ResponseDto saveNewRequest(DeviceSyncRequestDTO deviceSyncRequestDTO) {
//        requestValidator.validate(deviceSyncRequestDTO);
        deviceSyncRequestDTO.setOperation(DeviceOperation.ADD);
        log.debug("Validation Success {}", deviceSyncRequestDTO);
        List<DeviceSyncRequest> deviceSyncRequests = mapper.toDeviceSyncRequest(deviceSyncRequestDTO, systemConfigService.getActiveInstances());
        deviceSyncRequestService.saveAll(deviceSyncRequests);
        log.debug("Saved Request {} for all Instances:{} ", deviceSyncRequestDTO, systemConfigService.getActiveInstances());
        return ResponseDtoUtil.getSuccessResponseDto("Success");
    }

    public ResponseDto deleteRequest(DeviceSyncRequestDTO deviceSyncRequestDTO) {
//        requestValidator.validate(deviceSyncRequestDTO);
        deviceSyncRequestDTO.setOperation(DeviceOperation.DEL);
        log.debug("Validation Success {}", deviceSyncRequestDTO);
        List<DeviceSyncRequest> deviceSyncRequests = mapper.toDeviceSyncRequest(deviceSyncRequestDTO, systemConfigService.getActiveInstances());
        deviceSyncRequestService.saveAll(deviceSyncRequests);
        log.debug("Saved Request {} for all Instances:{} ", deviceSyncRequestDTO, systemConfigService.getActiveInstances());
        return ResponseDtoUtil.getSuccessResponseDto("Success");
    }

}
