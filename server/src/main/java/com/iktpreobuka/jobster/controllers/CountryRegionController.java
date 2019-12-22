package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.controllers.util.UserCustomValidator;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.security.Views;
import com.iktpreobuka.jobster.services.CountryRegionDao;

@Controller
@RestController
@RequestMapping(value= "/jobster/regions")

public class CountryRegionController {
	
	@Autowired 
	private UserCustomValidator userValidator;
	
	@Autowired 	
	private CountryRegionRepository countryRegionRepository;
		
	@Autowired 
	private CountryRegionDao countryRegionDao;
	
	@Autowired 
	private UserAccountRepository userAccountRepository;
	
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
		logger.info("################ /jobster/regions/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CountryRegionEntity region= countryRegionRepository.getById(id);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
			logger.info("################ /jobster/regions/getAll started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Iterable<CountryRegionEntity> regions= countryRegionRepository.findAll();
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
				} catch(Exception e) {
					logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
					return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
				}
	}
	
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active")
	public ResponseEntity<?> getAllActive(Principal principal) {
			logger.info("################ /jobster/regions/getAllActive started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Iterable<CountryRegionEntity> regions= countryRegionDao.findRegionByStatusLike(1);
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
			} catch(Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
			
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive")
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/regions/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryRegionEntity> regions= countryRegionDao.findRegionByStatusLike(0);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
					
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/regions/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CountryRegionEntity> regions= countryRegionDao.findRegionByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<CountryRegionEntity>>(regions, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
			
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name, Principal principal) {
		logger.info("################ /jobster/regions/getByName started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CountryRegionEntity region= countryRegionRepository.getByCountryRegionNameIgnoreCase(name);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/addNewRegion")
	public ResponseEntity<?> addNewRegion(@Valid @RequestBody CountryRegionEntity newRegion, Principal principal, BindingResult result) {
		logger.info("################ /jobster/regions/addNewRegion started.");
		//logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
		}
		if (newRegion == null) {
			logger.info("---------------- New region is null.");
			return new ResponseEntity<>("New region is null.", HttpStatus.BAD_REQUEST);
		}
		if (newRegion.getCountryRegionName() == null || newRegion.getCountry() == null ) {
			logger.info("---------------- Some atributes are null.");
			return new ResponseEntity<>("Some atributes are null", HttpStatus.BAD_REQUEST);
		}
		try {
			if (countryRegionRepository.existsByCountry(newRegion.getCountry()) && countryRegionRepository.existsByCountryRegionName(newRegion.getCountryRegionName())) {
				logger.info("---------------- Country already exists.");
		        return new ResponseEntity<>("Country already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			countryRegionRepository.save(newRegion);
			logger.info("New country created.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(newRegion, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/modify/{id}")
	public ResponseEntity<?> modifyRegion(@PathVariable Integer id, @Valid @RequestBody CountryRegionEntity updateRegion, /*Principal principal, */BindingResult result) {
		logger.info("################ /jobster/regions/modify/{id} started.");
		try {
			CountryRegionEntity region=countryRegionRepository.getById(id);
		//logger.info("Logged user: " + principal.getName());
		if (region == null) {
			logger.info("---------------- Region doesn't exists.");
	        return new ResponseEntity<>("Region doesn't exists.", HttpStatus.BAD_REQUEST);
	      }
		
		region.setCountryRegionName(updateRegion.getCountryRegionName());
		region.setCountry(updateRegion.getCountry());

		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		
		if (updateRegion.getCountryRegionName() == null || updateRegion.getCountry() == null ) {
			logger.info("---------------- Some or all atributes are null.");
			return new ResponseEntity<>("Some or all atributes are null", HttpStatus.BAD_REQUEST);
		}
		countryRegionRepository.save(region);
		logger.info("Country updated.");
		logger.info("---------------- Finished OK.");
		return new ResponseEntity<>(updateRegion, HttpStatus.OK);
		}
		catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	
		}
	}
	
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/regions/archive/{id} Archive started.");
		logger.info("Logged user: " + principal.getName());
		CountryRegionEntity region = new CountryRegionEntity();
		try {
			region = countryRegionRepository.getById(id);
			if (region == null || region.getStatus() == -1) {
				logger.info("---------------- Region not found.");
		        return new ResponseEntity<>("Region not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Region for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			countryRegionDao.archiveRegion(loggedUser, region);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/unarchive/{id}")
	public ResponseEntity<?> unArchive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/regions/unarchive/{id} Unarchive started.");
		logger.info("Logged user: " + principal.getName());
		CountryRegionEntity region = new CountryRegionEntity();
		try {
			region = countryRegionRepository.findByIdAndStatusLike(id, -1);
			if (region == null) {
				logger.info("---------------- Country not found.");
		        return new ResponseEntity<>("Country not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Country for unarchiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			countryRegionDao.unarchiveRegion(loggedUser, region);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/regions/undelete/{id} unDelete started.");
		logger.info("Logged user: " + principal.getName());
		CountryRegionEntity region = new CountryRegionEntity();
		try {
			region = countryRegionRepository.findByIdAndStatusLike(id, 0);
			if (region == null) {
				logger.info("---------------- Region not found.");
		        return new ResponseEntity<>("Region not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Region for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			countryRegionDao.undeleteRegion(loggedUser, region);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/regions/{id} Delete started.");
		logger.info("Logged user: " + principal.getName());
		CountryRegionEntity region = new CountryRegionEntity();
		try {
			region = countryRegionRepository.findByIdAndStatusLike(id, 1);
			if (region == null) {
				logger.info("---------------- Region not found.");
		        return new ResponseEntity<>("Region not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Region for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			countryRegionDao.deleteRegion(loggedUser, region);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<CountryRegionEntity>(region, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

