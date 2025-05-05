package com.eir.pgm.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_feature_ip_access_list")
public class UserAuthIp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ip_address")
    private String remoteIp;

    @Column(name = "user_id")
    private Integer userId;

}
