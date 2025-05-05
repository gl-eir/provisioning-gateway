package com.eir.pgm.validator;

import com.eir.pgm.dtos.DeviceSyncRequestDTO;
import com.eir.pgm.exceptions.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class RequestValidator {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    Pattern onlyNumberPattern = Pattern.compile("^[0-9]*$"); // Accept only Number
    private final String zero = "0";

    public void validate(DeviceSyncRequestDTO deviceSyncRequestDTO) {

        if (deviceSyncRequestDTO.getTac() != null) {
            String tac = deviceSyncRequestDTO.getTac();
            if (!isNumeric(tac)) {
                log.error("Numeric Validation Fails for Request {}", deviceSyncRequestDTO);
                throw new BadRequestException("Tac value Must be Numeric");
            } else if (!isLength(tac, 8)) {
                log.error("Length Validation Fails for Request {}", deviceSyncRequestDTO);
                throw new BadRequestException("Tac Must have 8 length ");
            }
        }

        if (deviceSyncRequestDTO.getImei() != null) {
            String imei = deviceSyncRequestDTO.getImei();
            if (!isNumeric(imei)) {
                log.error("Numeric Validation Fails for Request {}", deviceSyncRequestDTO);
                throw new BadRequestException("IMEI value must be Numeric");
            } else if (!zero.equals(imei) && !isLength(imei, 14)) {
                throw new BadRequestException("IMEI must have 14 length ");
            }
        }

        if (deviceSyncRequestDTO.getImsi() != null) {
            String imsi = deviceSyncRequestDTO.getImsi();
            if (!isNumeric(imsi)) {
                log.error("Numeric Validation Fails for Request {}", deviceSyncRequestDTO);
                throw new BadRequestException("IMSI value must be Numeric");
            } else if (!isLengthLess(imsi, 20)) {
                log.error("Length Validation Fails for Request {}", deviceSyncRequestDTO);
                throw new BadRequestException("IMSI length must be less than 20  ");
            }
        }

        if (deviceSyncRequestDTO.getMsisdn() != null) {
            String msisdn = deviceSyncRequestDTO.getMsisdn();
            if (!isNumeric(msisdn)) {
                log.error("Numeric Validation Fails for Request {}", deviceSyncRequestDTO);
                throw new BadRequestException("MSISDN value must be Numeric");
            }
            if (!isLengthLess(msisdn, 20)) {
                log.error("Length Validation Fails for Request {}", deviceSyncRequestDTO);
                throw new BadRequestException("MSISDN length must be less than 20 ");
            }
        }
    }

    private boolean isNumeric(String number) {
        return onlyNumberPattern.matcher(number).matches();
    }

    private boolean isLength(String number, int length) {
        return number.length() == length ? true : false;
    }

    private boolean isLengthLess(String number, int length) {
        return number.length() <= length ? true : false;
    }
}
