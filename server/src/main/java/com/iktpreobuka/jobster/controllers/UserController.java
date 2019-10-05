package com.iktpreobuka.jobster.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.UserRepository;
import com.iktpreobuka.jobster.security.Views;

@Controller
@RestController
@RequestMapping(value= "/jobster/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
		
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	

	@CrossOrigin
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/users/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserEntity> users = userRepository.findAll();
			logger.info("All users (active, deleted, archived).");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("---------------- This is an exception message:" + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}		


}
