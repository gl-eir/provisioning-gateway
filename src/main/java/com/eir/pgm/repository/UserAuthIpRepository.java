package com.eir.pgm.repository;

import com.eir.pgm.repository.entity.UserAuthIp;
import com.eir.pgm.repository.entity.UserAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthIpRepository extends JpaRepository<UserAuthIp, Integer> {

    List<UserAuthIp> findByUserId(Integer userId);
}
