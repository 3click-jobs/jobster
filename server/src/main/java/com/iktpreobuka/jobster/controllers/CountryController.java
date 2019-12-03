package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.List;
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
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.security.Views;
import com.iktpreobuka.jobster.services.CountryDao;

@Controller
@RestController
@RequestMapping(value= "/jobster/countries")

public class CountryController {
	
	@Autowired 
	private UserCustomValidator userValidator;
	
	@Autowired 	
	private CountryRepository countryRepository;
		
	@Autowired 
	private CountryDao countryDao;
	
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
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/countries/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CountryEntity country= countryRepository.getById(id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryEntity>(country, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
			logger.info("################ /jobster/countries/getAll started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Iterable<CountryEntity> countries= countryRepository.findAll();
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
				} catch(Exception e) {
					logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
					return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
				}
	}
	
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active")
	public ResponseEntity<?> getAllActive(Principal principal) {
			logger.info("################ /jobster/countries/getAllActive started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Iterable<CountryEntity> countries= countryDao.findCountryByStatusLike(1);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
			} catch(Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
			
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive")
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/countries/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryEntity> countries= countryDao.findCountryByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
					
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/countries/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryEntity> countries= countryDao.findCountryByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryEntity>>(countries, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
			
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name, Principal principal) {
		logger.info("################ /jobster/countries/getByName started.");
		logger.info("Logged username: " + principal.getName());
		try {
			List<CountryEntity> country= countryRepository.getByCountryNameIgnoreCase(name);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<List<CountryEntity>>(country, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
