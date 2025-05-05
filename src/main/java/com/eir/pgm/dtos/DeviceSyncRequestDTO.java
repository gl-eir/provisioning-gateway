package com.eir.pgm.dtos;

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
@AllArgsConstructor
@NoArgsConstructor
public class DeviceSyncRequestDTO {

    private Long id;

    private String imei;

    private String actualImei;

    private String msisdn;

    private String imsi;

    private Integer noOfRetry;

    private DeviceOperation operation;

    private String instanceName;

    private String tac;

    private LocalDateTime requestDate;

    private DeviceSyncRequestStatus status;

    private DeviceSyncRequestListIdentity identity;

    private LocalDateTime syncRequestTime;

    private LocalDateTime syncResponseTime;

}
