package com.eir.pgm.repository;

import com.eir.pgm.repository.entity.ProvisionGatewayConfig;
import com.eir.pgm.repository.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Integer> {

    public UserAuthentication findByUsername(String username);
}
