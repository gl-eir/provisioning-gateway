package com.eir.pgm.client.dto;

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
public class InstanceResponseDTO {

    private String operation;

    private String tac;

    private String imei;

    private String imsi;

    private String msisdn;

    private String operator;

    private DeviceSyncRequestStatus status;

    private String failureReason;

    private DeviceSyncRequestListIdentity identity;

    private LocalDateTime responseTime;

    private Integer retryCount = 0;

    private String responseStatus;
}
