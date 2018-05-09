package com.meetup.meetup.rest.controller;

import com.meetup.meetup.exception.runtime.CustomRuntimeException;
import com.meetup.meetup.exception.runtime.frontend.detailed.FrontendDetailedException;
import com.meetup.meetup.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ErrorController {

    private static Logger log = LoggerFactory.getLogger(AccountService.class);

    @ExceptionHandler(FrontendDetailedException.class)
    public void sendExceptionInfoToFront(HttpServletResponse response, Exception e) {
        log.error("Exception sent to frontend: ", e);
        response.setStatus(500);
        try {
            response.getWriter().print(e.getMessage().replace("SendCustomError", ""));
        } catch (IOException e1) {
            log.error("exception in ErrorController: ", e1);
        }
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public void handleCustomException(HttpServletResponse response, Exception e) {
        log.error("CustomException: ", e);
        response.setStatus(418);
        try {
            response.getWriter().print("Attention, an attempt to brew coffee with a teapot");
        } catch (IOException e1) {
            log.error("exception in ErrorController: ", e1);
        }
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse response, Exception e) {
        log.error("Exception: ", e);
        response.setStatus(418);
        try {
            response.getWriter().print("Attention, an attempt to brew coffee with a teapot");
        } catch (IOException e1) {
            log.error("exception in ErrorController: ", e1);
        }
    }
}

