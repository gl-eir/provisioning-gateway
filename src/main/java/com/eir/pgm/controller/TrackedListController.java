package com.eir.pgm.controller;

import com.eir.pgm.constants.DateTimeFormats;
import com.eir.pgm.constants.ResourcesUrls;
import com.eir.pgm.dtos.DeviceSyncRequestDTO;
import com.eir.pgm.orchestration.ListServiceOrchestration;
import com.eir.pgm.repository.entity.DeviceSyncRequestListIdentity;
import com.eir.pgm.util.dtos.ResponseDto;
import com.eir.pgm.util.ResponseDtoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = ResourcesUrls.TRACKED_LIST_RESOURCE_PATH)
public class TrackedListController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ListServiceOrchestration listServiceOrchestration;

    @PostMapping(path = "/{imei}/{imsi}/{msisdn}/{actualImei}/{requestDate}")
    public ResponseEntity<ResponseDto> add(@PathVariable("imei") String imei, @PathVariable("imsi") String imsi, @PathVariable("msisdn")
    String msisdn, @PathVariable("actualImei") String actualImei, @PathVariable("requestDate") String requestDate) {
        return new ResponseEntity<ResponseDto>(listServiceOrchestration.saveNewRequest(get(imei, imsi, msisdn, actualImei, requestDate)), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{imei}/{imsi}/{msisdn}/{actualImei}/{requestDate}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("imei") String imei, @PathVariable("imsi") String imsi, @PathVariable("msisdn")
    String msisdn, @PathVariable("actualImei") String actualImei, @PathVariable("requestDate") String requestDate) {
        return new ResponseEntity<ResponseDto>(listServiceOrchestration.deleteRequest(get(imei, imsi, msisdn, actualImei, requestDate)), HttpStatus.ACCEPTED);
    }

    private DeviceSyncRequestDTO get(String imei, String imsi, String msisdn, String actualImei, String requestDate) {
        return DeviceSyncRequestDTO.builder().imei(imei).imsi(imsi).msisdn(msisdn)
                .actualImei(actualImei)
                .requestDate(LocalDateTime.parse(requestDate, DateTimeFormats.URL_DATE_FORMATTER))
                .identity(DeviceSyncRequestListIdentity.TRACKED_LIST).build();
    }

    @GetMapping(path = "/")
    public ResponseEntity<ResponseDto> get(Principal principal, HttpServletRequest request) {
        log.info("principal: {}", principal);
        X509Certificate[] certs = (X509Certificate[]) request.getAttribute("jakarta.servlet.request.X509Certificate");
        log.info("certs: {}", certs);
//        log.info("principal : {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        log.info("Cred : {}", SecurityContextHolder.getContext().getAuthentication().getCredentials());
//        log.info("D : {}", SecurityContextHolder.getContext().getAuthentication().getDetails());
        return new ResponseEntity<ResponseDto>(ResponseDtoUtil.getSuccessResponseDto("Success"), HttpStatus.OK);
    }
}
