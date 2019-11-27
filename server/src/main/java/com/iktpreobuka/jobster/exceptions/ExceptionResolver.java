package com.iktpreobuka.jobster.exceptions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Order(Ordered.HIGHEST_PRECEDENCE)
//@EnableWebMvc
@RestControllerAdvice
public class ExceptionResolver {

	@ExceptionHandler(MultipartException.class) 
	public String handleMultipart(RedirectAttributes redirectAttributes, MultipartException e) {
		redirectAttributes.addFlashAttribute("message", e.getCause().getMessage()); 
		return "redirect:/uploadStatus";
	}
	
	@ExceptionHandler(Exception.class)
    public HashMap<String, String> handleException(HttpServletRequest request, Exception e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return response;
    }

	@ExceptionHandler(MissingPathVariableException.class)
    public HashMap<String, String> handleMissingPathVariableException(HttpServletRequest request, MissingPathVariableException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Required path variable is missing in this request. Please add it to your request.");
        return response;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    //@ResponseStatus(value= HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleNotFoundResourceException(HttpServletRequest request, NoHandlerFoundException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Requested resource wasn't found on the server");
        return response;
    }
	
}
