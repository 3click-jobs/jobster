package com.iktpreobuka.jobster.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping(value= "/jobster/users/principal")
public class PrincipalController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET, value = "/nesto")
	public ResponseEntity<?> userNamePrincipal(Principal principal) {
		logger.info("metoda pocela");
		try {
			logger.info("Logged username: " + principal.getName());
		} catch (Exception e) {
			return new ResponseEntity<String>("Eror is" + " " + e.getMessage() + " " + e, HttpStatus.BAD_REQUEST);
		}
		logger.info("skoro zavrsla");
		return new ResponseEntity<String>("Metoda prosla " + principal.getName(), HttpStatus.OK);
	}
	
}
