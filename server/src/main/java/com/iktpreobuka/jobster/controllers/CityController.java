package com.iktpreobuka.jobster.controllers;
		

		
import java.security.Principal;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.controllers.util.UserCustomValidator;
import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.security.Views;
		

		
@Controller
		
@RestController
		
@RequestMapping(value= "/jobster/cities")
		

		
public class CityController {
		

		
	@Autowired 
		
	private CityRepository cityRepository;
		

		
	@Autowired 
		
	private UserCustomValidator userValidator;
		

		
	@InitBinder
		
	protected void initBinder(final WebDataBinder binder) { 
		
		binder.addValidators(userValidator); 
		
		}
		

		

		
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
		

		
	private String createErrorMessage(BindingResult result) { 
		
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		
		}
		

		
	//@Secured("ROLE_ADMIN")
		
		@JsonView(Views.Admin.class)
		
		@RequestMapping(method = RequestMethod.GET)
		
		public ResponseEntity<?> getAll(Principal principal) {
		
			logger.info("################ /jobster/cities/getAll started.");
		
			logger.info("Logged username: " + principal.getName());
		
			try {
		
				Iterable<CityEntity> cities= cityRepository.getAllByStatusLike(1);
		
				logger.info("---------------- Finished OK.");
		
				return new ResponseEntity<Iterable<CityEntity>>(cities, HttpStatus.OK);
		
			} catch(Exception e) {
		
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
		
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		
			}
		
		}
		

		
		//@Secured("ROLE_ADMIN")
		
		@JsonView(Views.Admin.class)
		
		@RequestMapping(method = RequestMethod.GET, value = "/{name}")
		
		public ResponseEntity<?> getByName(@PathVariable String name, Principal principal) {
		
			logger.info("################ /jobster/cities/getByName started.");
		
			logger.info("Logged username: " + principal.getName());
		
			try {
		
				CityEntity city= cityRepository.getByCityNameIgnoreCase(name);
		
				logger.info("---------------- Finished OK.");
		
				return new ResponseEntity<CityEntity>(city, HttpStatus.OK);
		
			} catch(Exception e) {
		
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
		
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		
			}
		
		}
		
}