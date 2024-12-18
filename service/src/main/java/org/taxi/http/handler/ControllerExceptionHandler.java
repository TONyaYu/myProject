package org.taxi.http.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = "org.taxi.http.controller")
public class ControllerExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception e, HttpServletRequest request) {
//        log.error("Failed to return response", e);
//        return "error/error500";
//    }
}
