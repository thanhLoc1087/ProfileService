package com.loc.common_service.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.loc.common_service.common.CommonException;
import com.loc.common_service.common.ErrorMessage;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        log.error("Unknown internal server error: " + ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorMessage("9999", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleCommonException(CommonException ex) {
        log.error("Common error: %s %s %s", ex.getCode(), "Unknown internal error!", ex.getStatus());
        return ResponseEntity
            .status(ex.getStatus())
            .body(new ErrorMessage(ex.getCode(), ex.getMessage(), ex.getStatus()));
    }
}
