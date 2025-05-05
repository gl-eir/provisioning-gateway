package com.eir.pgm.controller;

import com.eir.pgm.constants.DateTimeFormats;
import com.eir.pgm.constants.ResourcesUrls;
import com.eir.pgm.dtos.DeviceSyncRequestDTO;
import com.eir.pgm.orchestration.ListServiceOrchestration;
import com.eir.pgm.repository.entity.DeviceSyncRequestListIdentity;
import com.eir.pgm.util.dtos.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = ResourcesUrls.BLOCKED_TAC_RESOURCE_PATH)
public class BlockedTacController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ListServiceOrchestration listServiceOrchestration;

    @PostMapping(path = "/{tac}/{requestDate}")
    public ResponseEntity<ResponseDto> add(@PathVariable("tac") String tac, @PathVariable("requestDate") String requestDate) {
        return new ResponseEntity<ResponseDto>(listServiceOrchestration.saveNewRequest(DeviceSyncRequestDTO.builder().tac(tac)
                .identity(DeviceSyncRequestListIdentity.BLOCKED_TAC)
                .requestDate(LocalDateTime.parse(requestDate, DateTimeFormats.URL_DATE_FORMATTER)).build()), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{tac}/{requestDate}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("tac") String tac, @PathVariable("requestDate") String requestDate) {
        return new ResponseEntity<ResponseDto>(listServiceOrchestration.deleteRequest(DeviceSyncRequestDTO.builder().tac(tac)
                .identity(DeviceSyncRequestListIdentity.BLOCKED_TAC)
                .requestDate(LocalDateTime.parse(requestDate, DateTimeFormats.URL_DATE_FORMATTER)).build()), HttpStatus.ACCEPTED);
    }

}
