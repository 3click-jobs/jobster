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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Iterables;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.controllers.util.UserCustomValidator;
import com.iktpreobuka.jobster.entities.CompanyEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.CompanyDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.CompanyRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.security.Views;
import com.iktpreobuka.jobster.services.CompanyDao;
import com.iktpreobuka.jobster.services.UserAccountDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/jobster/users/companies")
public class CompanyController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CompanyRepository companyRepository;
	
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

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/users/companies/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CompanyEntity> users= companyDao.findCompanyByStatusLike(1);
			logger.info("---------------- Finished OK.");
			if (Iterables.isEmpty(users)) {
				logger.info("---------------- Companies not found.");
		        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
		      }
			return new ResponseEntity<Iterable<CompanyEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/companies/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CompanyEntity user= companyRepository.findByIdAndStatusLike(id, 1);
			logger.info("---------------- Finished OK.");
			if (user == null) {
				logger.info("---------------- Company not found.");
		        return new ResponseEntity<>("Company not found.", HttpStatus.NOT_FOUND);
		      }
			return new ResponseEntity<CompanyEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /jobster/users/companies/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CompanyEntity> users= companyDao.findCompanyByStatusLike(0);
			logger.info("---------------- Finished OK.");
			if (Iterables.isEmpty(users)) {
				logger.info("---------------- Deleted companies not found.");
		        return new ResponseEntity<>("Deleted companies not found.", HttpStatus.NOT_FOUND);
		      }
			return new ResponseEntity<Iterable<CompanyEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getDeletedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/companies/deleted/getDeletedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CompanyEntity user= companyRepository.findByIdAndStatusLike(id, 0);
			logger.info("---------------- Finished OK.");
			if (user == null) {
				logger.info("---------------- Company not found in deleted.");
		        return new ResponseEntity<>("Company not found in deleted.", HttpStatus.NOT_FOUND);
		      }
			return new ResponseEntity<CompanyEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/users/companies/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<CompanyEntity> users= companyDao.findCompanyByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			if (Iterables.isEmpty(users)) {
				logger.info("---------------- Archived companies not found.");
		        return new ResponseEntity<>("Archived companies not found.", HttpStatus.NOT_FOUND);
		      }
			return new ResponseEntity<Iterable<CompanyEntity>>(users, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/companies/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			CompanyEntity user= companyRepository.findByIdAndStatusLike(id, -1);
			logger.info("---------------- Finished OK.");
			if (user == null) {
				logger.info("---------------- Company not found in archived.");
		        return new ResponseEntity<>("Company not found in archived.", HttpStatus.NOT_FOUND);
		      }
			return new ResponseEntity<CompanyEntity>(user, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@JsonView(Views.Guest.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewCompany(@Valid @RequestBody CompanyDTO newCompany, BindingResult result) {
		logger.info("################ /jobster/users/companies/addNewCompany started.");
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newCompany.equals(null)) {
			logger.info("---------------- New Company is null.");
	        return new ResponseEntity<>("New Company is null.", HttpStatus.BAD_REQUEST);
	      }
		if (newCompany.getCompanyName() == null || newCompany.getCompanyRegistrationNumber() == null || newCompany.getAccessRole() == null || newCompany.getEmail() == null || newCompany.getMobilePhone() == null || newCompany.getCity() == null || newCompany.getCountry() == null || newCompany.getIso2Code() == null || newCompany.getLatitude() == null || newCompany.getLongitude() == null || newCompany.getUsername() == null || newCompany.getPassword() == null || newCompany.getConfirmedPassword() == null ) {
			logger.info("---------------- Some or all atributes are null.");
			return new ResponseEntity<>("Some or all atributes are null", HttpStatus.BAD_REQUEST);
		}
		if (newCompany.getCompanyName().equals(" ") || newCompany.getCompanyName().equals("") || newCompany.getCompanyRegistrationNumber().equals(" ") || newCompany.getCompanyRegistrationNumber().equals("") || newCompany.getAccessRole().equals(" ") || newCompany.getAccessRole().equals("") || newCompany.getEmail().equals(" ") || newCompany.getEmail().equals("") || newCompany.getMobilePhone().equals(" ") || newCompany.getMobilePhone().equals("") || newCompany.getCity().equals(" ") || newCompany.getCity().equals("") || (newCompany.getCountryRegion() != null && (newCompany.getCountryRegion().equals(" ") || newCompany.getCountryRegion().equals("") ) ) || newCompany.getCountry().equals(" ") || newCompany.getCountry().equals("") || newCompany.getIso2Code().equals(" ") || newCompany.getIso2Code().equals("") || newCompany.getUsername().equals(" ") || newCompany.getUsername().equals("") || newCompany.getPassword().equals(" ") || newCompany.getPassword().equals("") || newCompany.getConfirmedPassword().equals(" ") || newCompany.getConfirmedPassword().equals("") ) {
			logger.info("---------------- Some or all atributes are blanks.");
			return new ResponseEntity<>("Some or all atributes are blanks", HttpStatus.BAD_REQUEST);
		}
		UserEntity user = new CompanyEntity();
		try {
			if (newCompany.getEmail() != null && companyRepository.getByEmail(newCompany.getEmail()) != null) {
				logger.info("---------------- Email already exists.");
		        return new ResponseEntity<>("Email already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (newCompany.getMobilePhone() != null && companyRepository.getByMobilePhone(newCompany.getMobilePhone()) != null) {
				logger.info("---------------- Mobile phone number already exists.");
		        return new ResponseEntity<>("Mobile phone number already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (newCompany.getCompanyRegistrationNumber() != null && companyRepository.getByCompanyRegistrationNumber(newCompany.getCompanyRegistrationNumber()) != null) {
				logger.info("---------------- Company Id already exists.");
		        return new ResponseEntity<>("Company Id already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (newCompany.getAccessRole() != null && !newCompany.getAccessRole().equals("ROLE_USER")) {
				logger.info("---------------- Access role must be ROLE_USER.");
		        return new ResponseEntity<>("Access role must be ROLE_USER.", HttpStatus.NOT_ACCEPTABLE);
			}		
			if (newCompany.getUsername() != null && userAccountRepository.getByUsername(newCompany.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<>("Username already exists.", HttpStatus.NOT_ACCEPTABLE);
		      }
			user = companyDao.addNewCompany(newCompany);
			logger.info("Company created.");
			if (newCompany.getUsername() != null && newCompany.getPassword() != null && newCompany.getConfirmedPassword() != null && newCompany.getPassword().equals(newCompany.getConfirmedPassword())) {
				UserAccountEntity account = userAccountDao.addNewUserAccount(user, user, newCompany.getUsername(), EUserRole.ROLE_USER, newCompany.getPassword());
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
			if (user != null && companyRepository.findByIdAndStatusLike(user.getId(), 1) != null) {
				companyRepository.deleteById(user.getId());
				logger.error("++++++++++++++++ Because of exeption Company with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@JsonView(Views.User.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyCompany(@PathVariable Integer id, @Valid @RequestBody CompanyDTO updateCompany, Principal principal, BindingResult result) {
		logger.info("################ /jobster/users/companies/{id}/modifyCompany started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateCompany == null) {
			logger.info("---------------- New Company is null.");
	        return new ResponseEntity<>("New Company data is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updateCompany.getCompanyName() == null && updateCompany.getCompanyRegistrationNumber() == null && updateCompany.getAccessRole() == null && updateCompany.getEmail() == null && updateCompany.getMobilePhone() == null && (updateCompany.getCity() == null || updateCompany.getCountry() == null || updateCompany.getIso2Code() == null || updateCompany.getLatitude() == null || updateCompany.getLongitude() == null) && updateCompany.getAbout() == null && updateCompany.getUsername() == null && (updateCompany.getPassword() == null || updateCompany.getConfirmedPassword() == null) ) {
			logger.info("---------------- All atributes are null.");
			return new ResponseEntity<>("All atributes are null", HttpStatus.BAD_REQUEST);
		}
		if ( ( updateCompany.getCompanyName() != null && (updateCompany.getCompanyName().equals(" ") || updateCompany.getCompanyName().equals("") ) ) || ( updateCompany.getCompanyRegistrationNumber() != null && (updateCompany.getCompanyRegistrationNumber().equals(" ") || updateCompany.getCompanyRegistrationNumber().equals("") ) ) || ( updateCompany.getAccessRole() != null && ( updateCompany.getAccessRole().equals(" ") || updateCompany.getAccessRole().equals("") ) ) || ( updateCompany.getEmail() != null && ( updateCompany.getEmail().equals(" ") || updateCompany.getEmail().equals("") ) ) || ( updateCompany.getMobilePhone() != null && ( updateCompany.getMobilePhone().equals(" ") || updateCompany.getMobilePhone().equals("") ) ) || ( updateCompany.getCity() != null && ( updateCompany.getCity().equals(" ") || updateCompany.getCity().equals("") ) ) || (updateCompany.getCountry() != null && ( updateCompany.getCountry().equals(" ") || updateCompany.getCountry().equals("") ) ) || ( updateCompany.getIso2Code() != null && ( updateCompany.getIso2Code().equals(" ") || updateCompany.getIso2Code().equals("") ) ) || ( updateCompany.getUsername() != null && (updateCompany.getUsername().equals(" ") || updateCompany.getUsername().equals("") ) ) || ( updateCompany.getPassword() != null && ( updateCompany.getPassword().equals(" ") || updateCompany.getPassword().equals("") ) ) || ( updateCompany.getConfirmedPassword() != null && (updateCompany.getConfirmedPassword().equals(" ") || updateCompany.getConfirmedPassword().equals("") ) ) ) {
			logger.info("---------------- Some or all atributes are blanks.");
			return new ResponseEntity<>("Some or all atributes are blanks", HttpStatus.BAD_REQUEST);
		}
		CompanyEntity user = new CompanyEntity();
		try {
			if (updateCompany.getEmail() != null && companyRepository.getByEmail(updateCompany.getEmail()) != null) {
				logger.info("---------------- Email already exists.");
		        return new ResponseEntity<>("Email already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateCompany.getMobilePhone() != null && companyRepository.getByMobilePhone(updateCompany.getMobilePhone()) != null) {
				logger.info("---------------- Mobile phone number already exists.");
		        return new ResponseEntity<>("Mobile phone number already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateCompany.getCompanyRegistrationNumber() != null && companyRepository.getByCompanyRegistrationNumber(updateCompany.getCompanyRegistrationNumber()) != null) {
				logger.info("---------------- Company Id already exists.");
		        return new ResponseEntity<>("Company Id already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			if (updateCompany.getAccessRole() != null && !updateCompany.getAccessRole().equals("ROLE_USER")) {
				logger.info("---------------- Access role must be ROLE_USER.");
		        return new ResponseEntity<>("Access role must be ROLE_USER.", HttpStatus.NOT_ACCEPTABLE);
			}		
			if (updateCompany.getUsername() != null && userAccountRepository.getByUsername(updateCompany.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<>("Username already exists.", HttpStatus.NOT_ACCEPTABLE);
		      }
			user = companyRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Company not found.");
		        return new ResponseEntity<>("Company not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Company identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			//UserEntity loggedUser = userRepository.getByIdAndStatusLike(id, 1);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user account identified.");
			if ((loggedUserAccount.getAccessRole().equals("ROLE_USER") || loggedUserAccount.getAccessRole().equals("ROLE_GUEST")) && id != loggedUser.getId()) {
				logger.info("---------------- Selected Id is not ID of logged User.");
				return new ResponseEntity<>("Selected Id is not ID of logged User.", HttpStatus.FORBIDDEN);
		      }
			if (updateCompany.getCompanyName() != null || updateCompany.getCompanyRegistrationNumber() != null || updateCompany.getEmail() != null || updateCompany.getMobilePhone() != null || (updateCompany.getCity() != null && updateCompany.getCountry() != null && updateCompany.getIso2Code() != null && updateCompany.getCountryRegion() != null && updateCompany.getLatitude() != null && updateCompany.getLongitude() != null) || updateCompany.getAbout() != null ) {
				companyDao.modifyCompany(loggedUser, user, updateCompany);
				logger.info("Company modified.");
			}
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_USER, 1);
			logger.info("User account identified.");
			if (account != null) {
				if (updateCompany.getUsername() != null && !updateCompany.getUsername().equals("") && !updateCompany.getUsername().equals(" ") && userAccountRepository.getByUsername(updateCompany.getUsername()) == null) {
					userAccountDao.modifyAccountUsername(loggedUser, account, updateCompany.getUsername());
					logger.info("Username modified.");					
				}
				if (updateCompany.getPassword() != null && !updateCompany.getPassword().equals("") && !updateCompany.getPassword().equals(" ") && updateCompany.getConfirmedPassword() != null && updateCompany.getPassword().equals(updateCompany.getConfirmedPassword())) {
					userAccountDao.modifyAccountPassword(loggedUser, account, updateCompany.getPassword());
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
	
	@SuppressWarnings("unlikely-arg-type")
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@JsonView(Views.User.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/companies/archive/archive started.");
		logger.info("Logged user: " + principal.getName());
		CompanyEntity user = new CompanyEntity();
		try {
			user = companyRepository.getById(id);
			if (user == null || user.getStatus() == -1) {
				logger.info("---------------- Company not found.");
		        return new ResponseEntity<>("Company not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Company for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			//UserEntity loggedUser = userRepository.getById(id);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user account identified.");
			if ((loggedUserAccount.getAccessRole().equals("ROLE_USER") || loggedUserAccount.getAccessRole().equals("ROLE_GUEST")) && id != loggedUser.getId()) {
				logger.info("---------------- Selected Id is not ID of logged User.");
				return new ResponseEntity<>("Selected Id is not ID of logged User.", HttpStatus.FORBIDDEN);
		      }
			companyDao.archiveCompany(loggedUser, user);
			logger.info("Company archived.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLike(user, EUserRole.ROLE_USER);
			logger.info("Company's user account identified.");
			if (account != null && account.getStatus() != -1) {
				userAccountDao.archiveAccount(loggedUser, account);
				logger.info("Company's user account archived.");
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
				companyRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Company with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@JsonView(Views.User.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/companies/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		CompanyEntity user = new CompanyEntity();
		try {
			user = companyRepository.findByIdAndStatusLike(id, 0);
			if (user == null) {
				logger.info("---------------- Company not found.");
		        return new ResponseEntity<>("Company not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Company for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			//UserEntity loggedUser = userRepository.getByIdAndStatusLike(id, 0);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user account identified.");
			if ((loggedUserAccount.getAccessRole().equals("ROLE_USER") || loggedUserAccount.getAccessRole().equals("ROLE_GUEST")) && id != loggedUser.getId()) {
				logger.info("---------------- Selected Id is not ID of logged User.");
				return new ResponseEntity<>("Selected Id is not ID of logged User.", HttpStatus.FORBIDDEN);
		      }
			companyDao.undeleteCompany(loggedUser, user);
			logger.info("Company undeleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_USER, 1);
			logger.info("Company's user account identified.");
			if (account != null) {
				userAccountDao.undeleteAccount(loggedUser, account);
				logger.info("Company's user account undeleted.");
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
				companyRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Company with Id " + user.getId().toString() + " deleted.");
			}
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	@JsonView(Views.User.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/users/companies/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		CompanyEntity user = new CompanyEntity();
		try {
			user = companyRepository.findByIdAndStatusLike(id, 1);
			if (user == null) {
				logger.info("---------------- Company not found.");
		        return new ResponseEntity<>("Company not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Company for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			//UserEntity loggedUser = userRepository.getByIdAndStatusLike(id, 1);
			logger.info("Logged user identified.");
			UserAccountEntity loggedUserAccount = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user account identified.");
			if ((loggedUserAccount.getAccessRole().equals("ROLE_USER") || loggedUserAccount.getAccessRole().equals("ROLE_GUEST")) && id != loggedUser.getId()) {
				logger.info("---------------- Selected Id is not ID of logged User.");
				return new ResponseEntity<>("Selected Id is not ID of logged User.", HttpStatus.FORBIDDEN);
		      }
			companyDao.deleteCompany(loggedUser, user);
			logger.info("Company deleted.");
			UserAccountEntity account = userAccountRepository.findByUserAndAccessRoleLikeAndStatusLike(user, EUserRole.ROLE_USER, 1);
			logger.info("Company's user account identified.");
			if (account != null) {
				userAccountDao.deleteAccount(loggedUser, account);
				logger.info("Company's user account deleted.");
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
				companyRepository.save(user);
				logger.error("++++++++++++++++ Because of exeption Company with Id " + user.getId().toString() + " activated.");
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
			logger.info("################ /jobster/users/companies/getAllPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("companyName"));
				Page<CompanyEntity> companiesPage= companyRepository.findCompanyByStatusLike(1,pageable);
				Iterable<CompanyEntity> users = companiesPage.getContent();
				logger.info("---------------- Finished OK.");
				if (Iterables.isEmpty(users)) {
					logger.info("---------------- Companies not found.");
			        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
			      }
				return new ResponseEntity<Iterable<CompanyEntity>>(users, HttpStatus.OK);
			} catch(Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//@Secured("ROLE_ADMIN")
		//@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value="/allDeletedPaginated")
		public ResponseEntity<?> getAllDeletedPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction,
				@RequestParam Optional<String> sortBy, 
				Principal principal) {
			logger.info("################ /jobster/users/companies/getAllPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("companyName"));
				Page<CompanyEntity> companiesPage= companyRepository.findCompanyByStatusLike(0,pageable);
				Iterable<CompanyEntity> users = companiesPage.getContent();
				logger.info("---------------- Finished OK.");
				if (Iterables.isEmpty(users)) {
					logger.info("---------------- Companies not found.");
			        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
			      }
				return new ResponseEntity<Iterable<CompanyEntity>>(users, HttpStatus.OK);
			} catch(Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		//@Secured("ROLE_ADMIN")
		//@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value="/allArchivedPaginated")
		public ResponseEntity<?> getAllArchivedPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction,
				@RequestParam Optional<String> sortBy, 
				Principal principal) {
			logger.info("################ /jobster/users/companies/getAllArchivedPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("companyName"));
				Page<CompanyEntity> companiesPage= companyRepository.findCompanyByStatusLike(-1,pageable);
				Iterable<CompanyEntity> users = companiesPage.getContent();
				logger.info("---------------- Finished OK.");
				if (Iterables.isEmpty(users)) {
					logger.info("---------------- Companies not found.");
			        return new ResponseEntity<>("Companies not found.", HttpStatus.NOT_FOUND);
			      }
				return new ResponseEntity<Iterable<CompanyEntity>>(users, HttpStatus.OK);
			} catch(Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

}
