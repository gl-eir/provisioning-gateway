package com.eir.pgm.mapper;

import com.eir.pgm.client.dto.InstanceRequestDTO;
import com.eir.pgm.dtos.DeviceSyncRequestDTO;
import com.eir.pgm.repository.entity.DevicePriority;
import com.eir.pgm.repository.entity.DeviceSyncRequest;
import com.eir.pgm.repository.entity.DeviceSyncRequestStatus;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BeansMapper {

    private DeviceSyncRequestMapper mapper = Mappers.getMapper(DeviceSyncRequestMapper.class);

    public DeviceSyncRequest toDeviceSyncRequest(DeviceSyncRequestDTO deviceSyncRequestDTO) {
        return mapper.toDeviceSyncRequest(deviceSyncRequestDTO);
    }

    public List<DeviceSyncRequest> toDeviceSyncRequest(DeviceSyncRequestDTO dto, List<String> instances) {
        List<DeviceSyncRequest> requests = new ArrayList<>();
        LocalDateTime requestDate = LocalDateTime.now();
        instances.forEach(instance -> {
            DeviceSyncRequest request = toDeviceSyncRequest(dto);
            request.setCreatedOn(LocalDateTime.now());
            request.setInstanceName(instance);
            request.setNoOfRetry(0);
            request.setStatus(DeviceSyncRequestStatus.NEW);
            request.setPriority(DevicePriority.high.getPriority());
            requests.add(request);
        });
        return requests;
    }

    public InstanceRequestDTO toInstanceRequestDTO(DeviceSyncRequest request) {
        return mapper.toInstanceRequestDTO(request);
    }
}
