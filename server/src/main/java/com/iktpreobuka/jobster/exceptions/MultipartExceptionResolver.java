package com.iktpreobuka.jobster.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestControllerAdvice
//@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MultipartExceptionResolver {

	@ExceptionHandler(MultipartException.class) 
	//@ExceptionHandler(value = { MultipartException.class })
	public String handleMultipart(RedirectAttributes redirectAttributes, MultipartException e) {
		redirectAttributes.addFlashAttribute("message", e.getCause().getMessage()); 
		return "redirect:/uploadStatus";
	}
	
}
