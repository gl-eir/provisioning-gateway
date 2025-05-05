package com.eir.pgm.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class DeviceSyncRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String imei;

    @Column(length = 20)
    private String actualImei;
    @Column(length = 20)
    private String msisdn;

    @Column(length = 20)
    private String imsi;

    private Integer noOfRetry;

    @Column(length = 5)
    @Enumerated(EnumType.STRING)
    private DeviceOperation operation;

    @Column(length = 20)
    private String instanceName;

    @Column(length = 8)
    private String tac;

    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DeviceSyncRequestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DeviceSyncRequestListIdentity identity;

    private LocalDateTime syncRequestTime;

    private LocalDateTime syncResponseTime;

    private String failureReason;

    private String deviceType;

    private Integer priority;

    private String responseStatus;

    private LocalDateTime createdOn;
}
