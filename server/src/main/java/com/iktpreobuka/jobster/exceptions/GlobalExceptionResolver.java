package com.iktpreobuka.jobster.exceptions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@ExceptionHandler(MultipartException.class) 
	public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", e.getCause().getMessage()); 
		return "redirect:/uploadStatus";
		}
	
}
