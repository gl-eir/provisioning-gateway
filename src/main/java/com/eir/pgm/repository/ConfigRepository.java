package com.eir.pgm.repository;

import com.eir.pgm.repository.entity.ProvisionGatewayConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<ProvisionGatewayConfig, Long> {

    public Optional<ProvisionGatewayConfig> findByConfigKeyAndModule(String configKey, String module);
}
