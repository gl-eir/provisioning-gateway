package com.eir.pgm.client.dto;

import com.eir.pgm.repository.entity.DeviceOperation;
import com.eir.pgm.repository.entity.DeviceSyncRequestListIdentity;
import com.eir.pgm.repository.entity.DeviceSyncRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceRequestDTO {

    private DeviceOperation operation;

    private String actualImei;

    private String imei;

    private String imsi;

    private String msisdn;

    private String tac;

    private String instanceName;

    private LocalDateTime requestDate;

    private DeviceSyncRequestStatus status;

    private DeviceSyncRequestListIdentity identity;

    private String deviceType;
}
