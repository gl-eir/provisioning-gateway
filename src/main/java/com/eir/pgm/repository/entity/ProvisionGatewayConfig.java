package com.eir.pgm.repository.entity;

import lombok.Data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_param", schema = "app")
public class ProvisionGatewayConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "tag")
    public String configKey;

    @Column(name = "value")
    public String configValue;

    @Column(name = "feature_name")
    public String module;

}
