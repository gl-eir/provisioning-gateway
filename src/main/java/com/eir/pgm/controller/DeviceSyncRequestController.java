package com.eir.pgm.controller;

import com.eir.pgm.constants.ResourcesUrls;
import com.eir.pgm.dtos.DeviceSyncRequestDTO;
import com.eir.pgm.orchestration.DeviceSyncRequestOrchestration;
import com.eir.pgm.orchestration.ListServiceOrchestration;
import com.eir.pgm.repository.entity.DeviceSyncRequestListIdentity;
import com.eir.pgm.util.dtos.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(path = ResourcesUrls.DEVICE_SYNC_RESOURCE_PATH)
public class DeviceSyncRequestController {

    @Autowired
    private DeviceSyncRequestOrchestration orchestration;

    @DeleteMapping(path = "/{date}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("date") LocalDate date) {
        return new ResponseEntity<ResponseDto>(orchestration.delete(date), HttpStatus.ACCEPTED);
    }


}
