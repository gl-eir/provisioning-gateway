package com.eir.pgm.exceptions;

import com.eir.pgm.util.ResponseDtoUtil;
import com.eir.pgm.util.dtos.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(ResponseDtoUtil.getErrorResponseDto(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ResponseDtoUtil.getErrorResponseDto(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ResponseDto> handleInternalServerException(
            InternalServerException ex, WebRequest request) {
        return new ResponseEntity<>(ResponseDtoUtil.getErrorResponseDto(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
