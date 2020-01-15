package com.iktpreobuka.jobster.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;

@Controller
@RestController
@RequestMapping(value= "/jobster/login")
public class LoginController {

	
	@Autowired
	private UserAccountRepository userAccountRepository;
		
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getLoggedInUser(Principal principal) {
		logger.info("################ /jobster/login/getLoggedInUser started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity user = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			if (user == null) {
				logger.info("---------------- User not found.");
		        return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("---------------- This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}		
	
	
}
