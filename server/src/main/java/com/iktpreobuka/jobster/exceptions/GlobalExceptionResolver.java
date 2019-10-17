package com.iktpreobuka.jobster.exceptions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@Order(Ordered.HIGHEST_PRECEDENCE)
//@EnableWebMvc
@RestControllerAdvice
public class GlobalExceptionResolver {

	@ExceptionHandler(Exception.class)
    public HashMap<String, String> handleException(HttpServletRequest request, Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return response;
    }
	
}
