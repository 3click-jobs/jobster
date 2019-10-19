package com.iktpreobuka.jobster.exceptions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@Order(Ordered.HIGHEST_PRECEDENCE)
//@EnableWebMvc
@RestControllerAdvice
//@ControllerAdvice
public class GlobalExceptionResolver extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	//@ExceptionHandler(value = { Exception.class })
    public HashMap<String, String> handleException(HttpServletRequest request, Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return response;
    }
	
}
