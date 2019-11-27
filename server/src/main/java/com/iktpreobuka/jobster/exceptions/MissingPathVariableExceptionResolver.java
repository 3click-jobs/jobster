package com.iktpreobuka.jobster.exceptions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MissingPathVariableExceptionResolver {

	@ExceptionHandler(MissingPathVariableException.class)
	//@ExceptionHandler(value = { MissingPathVariableException.class })
    public HashMap<String, String> handleMissingPathVariableException(HttpServletRequest request, MissingPathVariableException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Required path variable is missing in this request. Please add it to your request.");
        return response;
    }
	
}
