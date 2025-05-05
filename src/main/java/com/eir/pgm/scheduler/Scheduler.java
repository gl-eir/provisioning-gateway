package com.eir.pgm.scheduler;

import com.eir.pgm.orchestration.DeviceSyncRequestOrchestration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class Scheduler {

    @Autowired
    DeviceSyncRequestOrchestration deviceSyncRequestOrchestration;

    @Value("${deviceSyncRequest.delete.beforeDays:5}")
    private Integer deleteBeforeDays;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "${scheduler.daily.cronjob:0 0 1 * * *}")
    public void hourlyScheduler() {
        LocalDate date = LocalDate.now().minusDays(deleteBeforeDays);
        log.info("Daily Cronjob started at {} for:{}", LocalDateTime.now(), date);
        deviceSyncRequestOrchestration.delete(date);
    }

}
