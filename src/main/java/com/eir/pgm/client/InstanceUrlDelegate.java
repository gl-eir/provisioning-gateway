package com.eir.pgm.client;

import com.eir.pgm.client.dto.InstanceRequestDTO;
import com.eir.pgm.client.dto.InstanceResponseDTO;
import com.eir.pgm.constants.AlertIds;
import com.eir.pgm.constants.AlertMessagePlaceholders;
import com.eir.pgm.constants.DateTimeFormats;
import com.eir.pgm.repository.entity.DeviceOperation;
import com.eir.pgm.repository.entity.DeviceSyncRequestListIdentity;
import com.eir.pgm.repository.entity.DeviceSyncRequestStatus;
import com.eir.pgm.repository.entity.SystemConfigKeys;
import com.eir.pgm.services.ModuleAlertService;
import com.eir.pgm.services.SystemConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class InstanceUrlDelegate {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private String instance;

    @Autowired
    ModuleAlertService alertService;

    @Autowired
    private SystemConfigurationService config;

    private String accessTokenUrl = null;

    private String basicAuthCredentials = null;


    private Map<String, String> listUrlsMap = new HashMap<>();

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public RestTemplate restTemplate = null;
    private final String NULL = "NULL";


    public void init() throws RuntimeException {

        Integer readTimeOutInMinutes = config.findByKey(SystemConfigKeys.INSTANCE_URL_READ_TIME_OUT, 1);
        restTemplate = getRestTemplate(readTimeOutInMinutes, readTimeOutInMinutes);
        listUrlsMap.put(DeviceSyncRequestListIdentity.BLOCKED_LIST.toString(), config.findByKey(SystemConfigKeys.INSTANCE_URL_BLACKED.replaceAll("<INSTANCE_NAME>", instance)));
        listUrlsMap.put(DeviceSyncRequestListIdentity.EXCEPTION_LIST.toString(), config.findByKey(SystemConfigKeys.INSTANCE_URL_EXCEPTION.replaceAll("<INSTANCE_NAME>", instance)));
        listUrlsMap.put(DeviceSyncRequestListIdentity.TRACKED_LIST.toString(), config.findByKey(SystemConfigKeys.INSTANCE_URL_TRACKED.replaceAll("<INSTANCE_NAME>", instance)));
        listUrlsMap.put(DeviceSyncRequestListIdentity.ALLOWED_TAC.toString(), config.findByKey(SystemConfigKeys.INSTANCE_URL_ALLOWED_TAC.replaceAll("<INSTANCE_NAME>", instance)));
        listUrlsMap.put(DeviceSyncRequestListIdentity.BLOCKED_TAC.toString(), config.findByKey(SystemConfigKeys.INSTANCE_URL_BLOCKED_TAC.replaceAll("<INSTANCE_NAME>", instance)));
        listUrlsMap.put(DeviceSyncRequestListIdentity.HLR_DATA.toString(), config.findByKey(SystemConfigKeys.INSTANCE_URL_HLR_DATA.replaceAll("<INSTANCE_NAME>", instance)));
        listUrlsMap.put(DeviceSyncRequestListIdentity.GSMA_DATA.toString(), config.findByKey(SystemConfigKeys.INSTANCE_URL_GSMA_DATA.replaceAll("<INSTANCE_NAME>", instance)));

    }


    private RestTemplate getRestTemplate(Integer connectTimeoutInMinutes, Integer readTimeOutInMinutes) {

        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // Milli sec
        clientHttpRequestFactory.setConnectTimeout(connectTimeoutInMinutes * 60 * 1000);
        clientHttpRequestFactory.setReadTimeout(readTimeOutInMinutes * 60 * 1000);
        return new RestTemplate(clientHttpRequestFactory);
    }

    public InstanceResponseDTO callUrl(InstanceRequestDTO requestDTO) {
        String url = listUrlsMap.get(requestDTO.getInstanceName());
        InstanceResponseDTO responseDTO = callOperatorUrl(requestDTO);
        return responseDTO;
    }

    public InstanceResponseDTO callOperatorUrl(InstanceRequestDTO requestDTO) {
        long start = System.currentTimeMillis();
        String url = listUrlsMap.get(requestDTO.getIdentity().toString());
        if (requestDTO.getIdentity() == DeviceSyncRequestListIdentity.ALLOWED_TAC || requestDTO.getIdentity() == DeviceSyncRequestListIdentity.BLOCKED_TAC) {
            url = url.replaceAll("<TAC>", StringUtils.isBlank(requestDTO.getTac()) ? NULL : requestDTO.getTac());
            url = url.replaceAll("<REQUEST_DATE>", requestDTO.getRequestDate().format(DateTimeFormats.URL_DATE_FORMATTER));
        } else if (requestDTO.getIdentity() == DeviceSyncRequestListIdentity.GSMA_DATA) {
            url = url.replaceAll("<TAC>", StringUtils.isBlank(requestDTO.getTac()) ? NULL : requestDTO.getTac());
            try {
                url = url.replaceAll("<DEVICE_TYPE>", StringUtils.isBlank(requestDTO.getDeviceType()) ? NULL : requestDTO.getDeviceType());
//                url = url.replaceAll("<DEVICE_TYPE>", StringUtils.isBlank(requestDTO.getDeviceType()) ? NULL : URLEncoder.encode(requestDTO.getDeviceType(), StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20"));
            } catch (Exception e) {
                log.error("Error while Encoding Error:{}", e.getMessage(), e);
                throw new RuntimeException(e);
            }
        } else {
            url = url.replaceAll("<IMEI>", StringUtils.isBlank(requestDTO.getImei()) ? NULL : requestDTO.getImei());
            url = url.replaceAll("<IMSI>", StringUtils.isBlank(requestDTO.getImsi()) ? NULL : requestDTO.getImsi());
            url = url.replaceAll("<MSISDN>", StringUtils.isBlank(requestDTO.getMsisdn()) ? NULL : requestDTO.getMsisdn());
            url = url.replaceAll("<ACTUAL_IMEI>", StringUtils.isBlank(requestDTO.getActualImei()) ? NULL : requestDTO.getActualImei());
            url = url.replaceAll("<REQUEST_DATE>", requestDTO.getRequestDate().format(DateTimeFormats.URL_DATE_FORMATTER));
        }
        log.info("Calling Url for Instance:{} URL:{} Request:{}", requestDTO.getInstanceName(), url, requestDTO);
        InstanceResponseDTO responseDTO = new InstanceResponseDTO();
        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, requestDTO.getOperation() == DeviceOperation.ADD ? HttpMethod.POST : HttpMethod.DELETE, request, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.ALREADY_REPORTED) {
                responseDTO.setFailureReason("Success");
                responseDTO.setResponseStatus(response.getBody() == null ? "" : response.getBody().replaceAll("\"", ""));
                if (StringUtils.equalsAnyIgnoreCase(responseDTO.getResponseStatus(), new String[]{"notAdded", "notDeleted"})) {
                    throw new Exception("As Response " + responseDTO.getResponseStatus() + " From Core Module");
                }
                responseDTO.setStatus(DeviceSyncRequestStatus.SYNCED);
            } else {
                responseDTO.setFailureReason("Response Status " + response.getStatusCode());
                responseDTO.setStatus(DeviceSyncRequestStatus.FAILED);
            }
            log.info("Called Instance URL:{} Response:{} TimeTaken-{}", url, responseDTO.getStatus(), (System.currentTimeMillis() - start));
        } catch (org.springframework.web.client.ResourceAccessException |
                 org.springframework.web.client.HttpClientErrorException e) {
            log.error("Error while calling URL for Instance:{} url:{} ResourceAccessException:{}", instance, url, e.getMessage(), e);
            responseDTO.setFailureReason("CONNECTION_FAILED");
            responseDTO.setStatus(DeviceSyncRequestStatus.CONNECTION_FAILED);
            sendAlert(url, e.getMessage());
        } catch (Exception e) {
            log.error("Error while calling URL for Instance:{} url:{} Error:{}", instance, url, e.getMessage(), e);
            responseDTO.setFailureReason(e.getMessage());
            responseDTO.setStatus(DeviceSyncRequestStatus.FAILED);
            sendAlert(url, e.getMessage());
        }
        responseDTO.setResponseTime(LocalDateTime.now());
        return responseDTO;
    }

    public void sendAlert(String url, String exception) {
        Map<AlertMessagePlaceholders, String> map = new HashMap<>();
        map.put(AlertMessagePlaceholders.EXCEPTION, exception);
        map.put(AlertMessagePlaceholders.URL, url);
        alertService.sendAlert(AlertIds.CORE_MODULE_URL_CALL_EXCEPTION, map);
    }
}
