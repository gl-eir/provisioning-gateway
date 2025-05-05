package com.eir.pgm.mapper;

import com.eir.pgm.client.dto.InstanceRequestDTO;
import com.eir.pgm.dtos.DeviceSyncRequestDTO;
import com.eir.pgm.repository.entity.DeviceSyncRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceSyncRequestMapper {

    InstanceRequestDTO toInstanceRequestDTO(DeviceSyncRequest deviceSyncRequest);

    DeviceSyncRequest toDeviceSyncRequest(DeviceSyncRequestDTO deviceSyncRequestDTO);

}
