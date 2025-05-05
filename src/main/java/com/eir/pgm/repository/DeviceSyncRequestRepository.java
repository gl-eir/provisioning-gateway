package com.eir.pgm.repository;

import com.eir.pgm.repository.entity.DeviceSyncRequest;
import com.eir.pgm.repository.entity.DeviceSyncRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface DeviceSyncRequestRepository extends JpaRepository<DeviceSyncRequest, Long> {

    @Query("SELECT d FROM DeviceSyncRequest d WHERE d.priority=:priority and d.instanceName=:instanceName and d.status IN (:statuses) order by d.id asc LIMIT 60000")
    List<DeviceSyncRequest> findRequests(@Param("priority") Integer priority, @Param("instanceName") String instanceName, @Param("statuses") List<DeviceSyncRequestStatus> statuses);

    @Transactional
    Integer deleteByRequestDateLessThanAndStatus(LocalDateTime requestDate, DeviceSyncRequestStatus status);
}
