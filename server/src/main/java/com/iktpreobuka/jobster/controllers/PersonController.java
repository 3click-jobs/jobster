package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Iterables;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.controllers.util.UserCustomValidator;
import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.PersonRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.services.PersonDao;
import com.iktpreobuka.jobster.services.UserAccountDao;

@Controller
@RestController
@RequestMapping(value= "/jobster/users/persons")
public class PersonController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private PersonDao personDao;

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	/*@Autowired
	private JobOfferRepository jobOfferRepository;*/

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
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/users/persons/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<PersonEntity> users= personRepository.findByStatusLike(1);
			if (Iterables.isEmpty(users)) {
				logger.info("---------------- Companies not found.");
		        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<PersonEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/persons/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			PersonEntity user= personRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Company not found.");
		        return new ResponseEntity<>("Company not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<PersonEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /jobster/users/persons/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<PersonEntity> users= personRepository.findByStatusLike(0);
			if (Iterables.isEmpty(users)) {
				logger.info("---------------- Companies not found.");
		        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<PersonEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getDeletedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/persons/deleted/getDeletedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			PersonEntity user= personRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Company not found.");
		        return new ResponseEntity<>("Company not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<PersonEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/users/persons/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<PersonEntity> users= personRepository.findByStatusLike(-1);
			if (Iterables.isEmpty(users)) {
				logger.info("---------------- Companies not found.");
		        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<PersonEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/persons/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			PersonEntity user= personRepository.findByIdAndStatusLike(id, -1);
			if (user == null) {
				logger.info("---------------- Company not found.");
		        return new ResponseEntity<>("Company not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<PersonEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	//@JsonView(Views.Guest.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewPerson(@Valid @RequestBody PersonDTO newPerson, BindingResult result) {
		logger.info("################ /jobster/users/persons/addNewPerson started.");
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newPerson == null) {
			logger.info("---------------- New person is null.");
	        return new ResponseEntity<>("New person is null.", HttpStatus.BAD_REQUEST);
	      }
		if (newPerson.getFirstName() == null || newPerson.getLastName() == null || newPerson.getAccessRole() == null || newPerson.getGender() == null || newPerson.getBirthDate() == null || newPerson.getEmail() == null || newPerson.getMobilePhone() == null || newPerson.getCity() == null || newPerson.getCountry() == null || newPerson.getIso2Code() == null || newPerson.getCountryRegion() == null || newPerson.getLatitude() == null || newPerson.getLongitude() == null || newPerson.getUsername() == null || newPerson.getPassword() == null || newPerson.getConfirmedPassword() == null ) {
			logger.info("---------------- Some atributes are null.");
			return new ResponseEntity<>("Some atributes are null", HttpStatus.BAD_REQUEST);
		}
		if (newPerson.getFirstName().equals(" ") || newPerson.getFirstName().equals("") || newPerson.getLastName().equals(" ") || newPerson.getLastName().equals("") || newPerson.getGender().equals(" ") || newPerson.getGender().equals("") || newPerson.getBirthDate().equals(" ") || newPerson.getBirthDate().equals("") || newPerson.getAccessRole().equals(" ") || newPerson.getAccessRole().equals("") || newPerson.getEmail().equals(" ") || newPerson.getEmail().equals("") || newPerson.getMobilePhone().equals(" ") || newPerson.getMobilePhone().equals("") || newPerson.getCity().equals(" ") || newPerson.getCity().equals("") || newPerson.getCountry().equals(" ") || newPerson.getCountry().equals("") || newPerson.getIso2Code().equals(" ") || newPerson.getIso2Code().equals("") || newPerson.getUsername().equals(" ") || newPerson.getUsername().equals("") || newPerson.getPassword().equals(" ") || newPerson.getPassword().equals("") || newPerson.getConfirmedPassword().equals(" ") || newPerson.getConfirmedPassword().equals("") ) {
			logger.info("---------------- Some atributes are blanks.");
			return new ResponseEntity<>("Some atributes are blanks", HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new PersonEntity();
		try {
			if (newPerson.getEmail() != null && personRepository.getByEmail(newPerson.getEmail()) != null) {
				logger.info("---------------- Email already exists.");
		        return new ResponseEntity<>("Email already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (newPerson.getMobilePhone() != null && personRepository.getByMobilePhone(newPerson.getMobilePhone()) != null) {
				logger.info("---------------- Mobile phone number already exists.");
		        return new ResponseEntity<>("Mobile phone number already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (newPerson.getAccessRole() != null && !newPerson.getAccessRole().equals("ROLE_USER")) {
				logger.info("---------------- Access role must be ROLE_USER.");
		        return new ResponseEntity<>("Access role must be ROLE_USER.", HttpStatus.NOT_ACCEPTABLE);
			}	
			if (newPerson.getGender() != null && !(newPerson.getGender().equals("GENDER_MALE") || newPerson.getGender().equals("GENDER_FEMALE"))) {
				logger.info("---------------- Gender must be GENDER_MALE or GENDER_FEMALE.");
		        return new ResponseEntity<>("Gender must be GENDER_MALE or GENDER_FEMALE.", HttpStatus.NOT_ACCEPTABLE);
			}		
			if (newPerson.getUsername() != null && userAccountRepository.getByUsername(newPerson.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<>("Username already exists.", HttpStatus.NOT_ACCEPTABLE);
		      }
			user = personDao.addNewPerson(newPerson);
			logger.info("Person created.");
			if (newPerson.getUsername() != null && newPerson.getPassword() != null && newPerson.getConfirmedPassword() != null && newPerson.getPassword().equals(newPerson.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(user, user, newPerson.getUsername(), EUserRole.ROLE_USER, newPerson.getPassword());
				logger.info("Account created.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			if (user != null && personRepository.findByIdAndStatusLike(user.getId(), 1) != null) {
				personRepository.deleteById(user.getId());
				logger.error("++++++++++++++++ Because of exeption person with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyPerson(@PathVariable Integer id, @Valid @RequestBody PersonDTO updatePerson, Principal principal, BindingResult result) {
		logger.info("################ /jobster/users/persons/{id}/modifyPerson started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updatePerson == null) {
			logger.info("---------------- New person is null.");
	        return new ResponseEntity<>("New person data is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updatePerson.getFirstName() == null && updatePerson.getLastName() == null && updatePerson.getGender() == null && updatePerson.getBirthDate() == null && updatePerson.getAccessRole() == null && updatePerson.getEmail() == null && updatePerson.getMobilePhone() == null && (updatePerson.getCity() == null || updatePerson.getCountry() == null || updatePerson.getIso2Code() == null || updatePerson.getLatitude() == null || updatePerson.getLongitude() == null) && updatePerson.getAbout() == null && updatePerson.getUsername() == null && (updatePerson.getPassword() == null || updatePerson.getConfirmedPassword() == null) ) {
			logger.info("---------------- All atributes are null.");
			return new ResponseEntity<>("All atributes are null", HttpStatus.BAD_REQUEST);
		}
		if ( ( updatePerson.getFirstName() != null && (updatePerson.getFirstName().equals(" ") || updatePerson.getFirstName().equals("") ) ) || ( updatePerson.getLastName() != null && (updatePerson.getLastName().equals(" ") || updatePerson.getLastName().equals("") ) ) || ( updatePerson.getGender() != null && (updatePerson.getGender().equals(" ") || updatePerson.getGender().equals("") ) ) || ( updatePerson.getBirthDate() != null && (updatePerson.getBirthDate().equals(" ") || updatePerson.getBirthDate().equals("") ) ) || ( updatePerson.getAccessRole() != null && ( updatePerson.getAccessRole().equals(" ") || updatePerson.getAccessRole().equals("") ) ) || ( updatePerson.getEmail() != null && ( updatePerson.getEmail().equals(" ") || updatePerson.getEmail().equals("") ) ) || ( updatePerson.getMobilePhone() != null && ( updatePerson.getMobilePhone().equals(" ") || updatePerson.getMobilePhone().equals("") ) ) || ( updatePerson.getCity() != null && ( updatePerson.getCity().equals(" ") || updatePerson.getCity().equals("") ) ) || (updatePerson.getCountry() != null && ( updatePerson.getCountry().equals(" ") || updatePerson.getCountry().equals("") ) ) || ( updatePerson.getIso2Code() != null && ( updatePerson.getIso2Code().equals(" ") || updatePerson.getIso2Code().equals("") ) ) || ( updatePerson.getUsername() != null && (updatePerson.getUsername().equals(" ") || updatePerson.getUsername().equals("") ) ) || ( updatePerson.getPassword() != null && ( updatePerson.getPassword().equals(" ") || updatePerson.getPassword().equals("") ) ) || ( updatePerson.getConfirmedPassword() != null && (updatePerson.getConfirmedPassword().equals(" ") || updatePerson.getConfirmedPassword().equals("") ) ) ) {
			logger.info("---------------- Some or all atributes are blanks.");
			return new ResponseEntity<>("Some or all atributes are blanks", HttpStatus.BAD_REQUEST);
		}
		PersonEntity user = new PersonEntity();
		try {
			if (updatePerson.getEmail() != null && personRepository.getByEmail(updatePerson.getEmail()) != null) {
				logger.info("---------------- Email already exists.");
		        return new ResponseEntity<>("Email already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (updatePerson.getMobilePhone() != null && personRepository.getByMobilePhone(updatePerson.getMobilePhone()) != null) {
				logger.info("---------------- Mobile phone number already exists.");
		        return new ResponseEntity<>("Mobile phone number already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (updatePerson.getAccessRole() != null && !updatePerson.getAccessRole().equals("ROLE_USER")) {
				logger.info("---------------- Access role must be ROLE_USER.");
		        return new ResponseEntity<>("Access role must be ROLE_USER.", HttpStatus.NOT_ACCEPTABLE);
			}	
			if (updatePerson.getGender() != null && (!updatePerson.getGender().equals("GENDER_MALE") || !updatePerson.getGender().equals("GENDER_FEMALE"))) {
				logger.info("---------------- Gender must be GENDER_MALE or GENDER_FEMALE.");
		        return new ResponseEntity<>("Gender must be GENDER_MALE or GENDER_FEMALE.", HttpStatus.NOT_ACCEPTABLE);
			}		
			if (updatePerson.getUsername() != null && userAccountRepository.getByUsername(updatePerson.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<>("Username already exists.", HttpStatus.NOT_ACCEPTABLE);
		      }
			user = personRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Person not found.");
		        return new ResponseEntity<>("Person not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Person identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updatePerson.getFirstName() != null || updatePerson.getLastName() != null || updatePerson.getGender() != null || updatePerson.getBirthDate() != null|| updatePerson.getEmail() != null || updatePerson.getMobilePhone() != null || (updatePerson.getCity() != null && updatePerson.getCountry() != null && updatePerson.getIso2Code() != null && updatePerson.getCountryRegion() != null && updatePerson.getLatitude() != null && updatePerson.getLongitude() != null) || updatePerson.getAbout() != null ) {
				personDao.modifyPerson(loggedUser, user, updatePerson);
				logger.info("Person modified.");
			}
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_USER, 1);
			logger.info("User account identified.");
			if (account != null) {
				if (updatePerson.getUsername() != null && !updatePerson.getUsername().equals("") && !updatePerson.getUsername().equals(" ") && userAccountRepository.getByUsername(updatePerson.getUsername()) == null) {
					userAccountDao.modifyAccountUsername(loggedUser, account, updatePerson.getUsername());
					logger.info("Username modified.");					
				}
				if (updatePerson.getPassword() != null && !updatePerson.getPassword().equals("") && !updatePerson.getPassword().equals(" ") && updatePerson.getConfirmedPassword() != null && updatePerson.getPassword().equals(updatePerson.getConfirmedPassword())) {
					userAccountDao.modifyAccountPassword(loggedUser, account, updatePerson.getPassword());
					logger.info("Password modified.");
				}
				logger.info("User account modified.");
				return new ResponseEntity<>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/add-offer")
	public ResponseEntity<?> addOfferToPerson(@PathVariable Integer id, @Valid @RequestBody PersonDTO updatePerson, Principal principal, BindingResult result) {
		logger.info("################ /jobster/users/persons/{id}/add-offer/addOfferToPerson started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updatePerson == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<>("Data is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updatePerson.getFirstName() != null || updatePerson.getLastName() != null || updatePerson.getGender() != null || updatePerson.getBirthDate() != null|| updatePerson.getEmail() != null || updatePerson.getMobilePhone() != null || updatePerson.getCity() != null || updatePerson.getAccessRole() != null || updatePerson.getUsername() != null || updatePerson.getPassword() != null || updatePerson.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<>("Update have non acceptable atrributes.", HttpStatus.NOT_ACCEPTABLE);
		}
		for (String t : updatePerson.getJobOffers()) {
			if (t == null || t.equals("") || t.equals(" ")) {
				logger.info("---------------- New offer/s is null.");
		        return new ResponseEntity<>("New offer/s is null.", HttpStatus.BAD_REQUEST);
			}
		}
		List<JobOfferEntity> st = new ArrayList<JobOfferEntity>();
		try {
			for (String s : updatePerson.getJobOffers()) {
				if (jobOfferRepository.findByIdAndStatusLike(Integer.parseInt(s), 1) == null ) {
					logger.info("---------------- Offer/s not found.");
			        return new ResponseEntity<>("Offer/s not found.", HttpStatus.NOT_FOUND);
				}
			}
			PersonEntity user = personRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Person not found.");
		        return new ResponseEntity<>("Person not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Person identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updatePerson.getJobOffers() != null) {
				st = jobOfferDao.addOffersToPerson(loggedUser, user, updatePerson.getJobOffers());
				logger.info("Offer/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(st, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/remove-offer")
	public ResponseEntity<?> removeOffersFromPerson(@PathVariable Integer id, @Valid @RequestBody PersonDTO updatePerson, Principal principal, BindingResult result) {
		logger.info("################ /jobster/users/persons/{id}/remove-offer/removeOffersFromPerson started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updatePerson == null) {
			logger.info("---------------- Data is null.");
	        return new ResponseEntity<>("Data is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updatePerson.getFirstName() != null || updatePerson.getLastName() != null || updatePerson.getGender() != null || updatePerson.getBirthDate() != null|| updatePerson.getEmail() != null || updatePerson.getMobilePhone() != null || updatePerson.getCity() != null || updatePerson.getAccessRole() != null || updatePerson.getUsername() != null || updatePerson.getPassword() != null || updatePerson.getConfirmedPassword() != null) {
			logger.info("---------------- Update have non acceptable atrributes.");
	        return new ResponseEntity<>("Update have non acceptable atrributes.", HttpStatus.NOT_ACCEPTABLE);
		}
		for (String t : updatePerson.getJobOffers()) {
			if (t ==null || t.equals("") || t.equals(" ")) {
				logger.info("---------------- Remove offer/s is null.");
		        return new ResponseEntity<>("Remove offer/s is null.", HttpStatus.BAD_REQUEST);
			}
		}
		List<JobOfferEntity> st = new ArrayList<JobOfferEntity>();
		try {
			for (String s : updatePerson.getJobOffers()) {
				if (jobOfferRepository.findByIdAndStatusLike(Integer.parseInt(s), 1) == null ) {
					logger.info("---------------- Offer/s not found.");
			        return new ResponseEntity<>("Offer/s not found.", HttpStatus.NOT_FOUND);
				}
			}
			PersonEntity user = personRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Person not found.");
		        return new ResponseEntity<>("Person not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Person identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updatePerson.getJobOffers() != null) {
				st = jobSeekDao.removeOffersFromPerson(loggedUser, user, updatePerson.getJobOffers());
				logger.info("Offer/s added.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(st, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/

	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/persons/archive/archive started.");
		logger.info("Logged user: " + principal.getName());
		PersonEntity user = new PersonEntity();
		try {
			user = personRepository.getById(id);
			if (user == null || user.getStatus() == -1) {
				logger.info("---------------- Person not found.");
		        return new ResponseEntity<>("Person not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Person for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			/*if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't archive yourself.");
				return new ResponseEntity<>("Selected Id is ID of logged User: Cann't archive yourself.", HttpStatus.FORBIDDEN);
		      }	*/
			personDao.archivePerson(loggedUser, user);
			logger.info("Person archived.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLike(user, EUserRole.ROLE_USER);
			logger.info("Person's user account identified.");
			if (account != null && account.getStatus() != -1) {
				userAccountDao.archiveAccount(loggedUser, account);
				logger.info("Person's user account archived.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			if (user != null && user.getStatus() == -1) {
				user.setStatusInactive();
				personRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption person with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/persons/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		PersonEntity user = new PersonEntity();
		try {
			user = personRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Person not found.");
		        return new ResponseEntity<>("Person not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Person for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			personDao.undeletePerson(loggedUser, user);
			logger.info("Person undeleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_USER, 1);
			logger.info("Person's user account identified.");
			if (account != null) {
				userAccountDao.undeleteAccount(loggedUser, account);
				logger.info("Person's user account undeleted.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			if (user != null && user.getStatus() == 1) {
				user.setStatusInactive();
				personRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption person with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/persons/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		PersonEntity user = new PersonEntity();
		try {
			user = personRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Person not found.");
		        return new ResponseEntity<>("Person not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Person for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			/*if (id == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User: Cann't delete yourself.");
				return new ResponseEntity<>("Selected Id is ID of logged User: Cann't delete yourself.", HttpStatus.FORBIDDEN);
		      }	*/
			personDao.deletePerson(loggedUser, user);
			logger.info("Person deleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_USER, 1);
			logger.info("Person's user account identified.");
			if (account != null) {
				userAccountDao.deleteAccount(loggedUser, account);
				logger.info("Person's user account deleted.");
				return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			if (user != null && user.getStatus() == 0) {
				user.setStatusActive();
				personRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption person with Id " + user.getId().toString() + " activated.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

//pagination:
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value="/allPaginated")
	public ResponseEntity<?> getAllPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/users/persons/getAllPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("lastName"));
			Page<PersonEntity> userssPage = personRepository.findByStatusLike(1, pageable);
			Iterable<PersonEntity> users = userssPage.getContent();
			if (Iterables.isEmpty(users)) {
				logger.info("---------------- Companies not found.");
		        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<PersonEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value="/deletedPaginated")
	public ResponseEntity<?> getAllDeletedPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/users/persons/getAllDeletedPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("lastName"));
			Page<PersonEntity> userssPage = personRepository.findByStatusLike(0, pageable);
			Iterable<PersonEntity> users = userssPage.getContent();
			if (Iterables.isEmpty(users)) {
				logger.info("---------------- Companies not found.");
		        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<PersonEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value="/archivedPaginated")
	public ResponseEntity<?> getAllArchivedPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/users/persons/getAllArchivedPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("lastName"));
			Page<PersonEntity> userssPage = personRepository.findByStatusLike(-1, pageable);
			Iterable<PersonEntity> users = userssPage.getContent();
			if (Iterables.isEmpty(users)) {
				logger.info("---------------- Companies not found.");
		        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<PersonEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}