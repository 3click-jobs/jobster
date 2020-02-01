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
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.UserAccountDTO;
import com.iktpreobuka.jobster.enumerations.EUserRole;
import com.iktpreobuka.jobster.repositories.CompanyRepository;
import com.iktpreobuka.jobster.repositories.PersonRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;
import com.iktpreobuka.jobster.security.Views;
import com.iktpreobuka.jobster.services.UserAccountDao;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/jobster/accounts")
public class UserAccountController {

	@Autowired
	private UserAccountDao userAccountDao;

	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
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
		logger.info("################ /jobster/accounts/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserAccountEntity> accounts= userAccountRepository.findByStatusLike(1);
			if (Iterables.isEmpty(accounts)) {
				logger.info("---------------- User accounts not found.");
		        return new ResponseEntity<>("User accounts not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/accounts/getById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserAccountEntity account= userAccountRepository.findByIdAndStatusLike(id, 1);
			if (account == null) {
				logger.info("---------------- User account not found.");
		        return new ResponseEntity<>("User account not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /jobster/accounts/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserAccountEntity> accounts= userAccountRepository.findByStatusLike(0);
			if (Iterables.isEmpty(accounts)) {
				logger.info("---------------- Deleted user accounts not found.");
		        return new ResponseEntity<>("Deleted user accounts not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted/{id}")
	public ResponseEntity<?> getDeletedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/accounts/deleted/{id}/getDeletedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserAccountEntity account= userAccountRepository.findByIdAndStatusLike(id, 0);
			if (account == null) {
				logger.info("---------------- Deleted user account not found.");
		        return new ResponseEntity<>("Deleted user account not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/accounts/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<UserAccountEntity> accounts= userAccountRepository.findByStatusLike(-1);
			if (Iterables.isEmpty(accounts)) {
				logger.info("---------------- Archived user accounts not found.");
		        return new ResponseEntity<>("Archived user accounts not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/accounts/archived/{id}/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserAccountEntity account= userAccountRepository.findByIdAndStatusLike(id, -1);
			if (account == null) {
				logger.info("---------------- Archived user account not found.");
		        return new ResponseEntity<>("Archived user account not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNew(@Valid @RequestBody UserAccountDTO newUserAccount, Principal principal, BindingResult result) {
		logger.info("################ /jobster/accounts/addNew started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newUserAccount == null) {
			logger.info("---------------- New user account is null.");
	        return new ResponseEntity<>("New user account is null", HttpStatus.BAD_REQUEST);
	      }
		if (newUserAccount.getUsername() == null || newUserAccount.getAccessRole() == null || (newUserAccount.getPassword() == null || newUserAccount.getConfirmedPassword() == null) || newUserAccount.getUserId() == null) {
			logger.info("---------------- Some or all atributes are null.");
			return new ResponseEntity<>("Some or all atributes are null.", HttpStatus.BAD_REQUEST);
		}
		if (newUserAccount.getAccessRole().equals(" ") || newUserAccount.getAccessRole().equals("") || newUserAccount.getUsername().equals(" ") || newUserAccount.getUsername().equals("") || newUserAccount.getPassword().equals(" ") || newUserAccount.getPassword().equals("") || newUserAccount.getConfirmedPassword().equals(" ") || newUserAccount.getConfirmedPassword().equals("") || newUserAccount.getUserId().equals(" ") || newUserAccount.getUserId().equals("") ) {
			logger.info("---------------- Some or all atributes are blanks.");
			return new ResponseEntity<>("Some or all atributes are blanks", HttpStatus.BAD_REQUEST);
		}
		UserAccountEntity account = new UserAccountEntity();
		try {
			if (newUserAccount.getUsername() != null && userAccountRepository.getByUsername(newUserAccount.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<>("Username already exists.", HttpStatus.NOT_ACCEPTABLE);
		    }
			UserEntity user = new UserEntity();
			EUserRole role = null;
//			if (newUserAccount.getUsername() != null && userAccountRepository.getByUsername(newUserAccount.getUsername()) != null) {
//				logger.info("---------------- Username already exist.");
//		        return new ResponseEntity<>("Username already exist.", HttpStatus.NOT_ACCEPTABLE);
//		    }
			if (newUserAccount.getUserId() != null) {
				user = userRepository.getById(Integer.parseInt(newUserAccount.getUserId()));
				if (user == null) {
					logger.info("---------------- User not found.");
					return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
				}
				Integer userStatus = null;
				UserEntity userWithRole = new UserEntity();
				if (user != null) {
					if (newUserAccount.getAccessRole().equals("ROLE_ADMIN")) {
						userWithRole = userRepository.getByIdAndStatusLike(Integer.parseInt(newUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_ADMIN;
						}
					} else if (newUserAccount.getAccessRole().equals("ROLE_USER")) {
						userWithRole = personRepository.getByIdAndStatusLike(Integer.parseInt(newUserAccount.getUserId()), 1);
						if (userWithRole == null) {
							userWithRole = companyRepository.getByIdAndStatusLike(Integer.parseInt(newUserAccount.getUserId()), 1);
						}
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_USER;
						}
					} else {
						logger.info("---------------- User not exist or wrong access role.");
						return new ResponseEntity<>("User not exist or wrong access role.", HttpStatus.BAD_REQUEST);
					}
				}
				if (user == null || userStatus == null || userStatus != 1) {
					logger.info("---------------- User not found for that role.");
					return new ResponseEntity<>("User not found for that role.", HttpStatus.NOT_FOUND);
				}
			}		
			if (user != null && role !=null && userAccountRepository.findByUserAndAccessRoleAndStatusLike(user, role, 1) != null) {
				logger.info("---------------- User already have account for that role.");
		        return new ResponseEntity<>("User already have account for that role.", HttpStatus.FORBIDDEN);
		    }
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (newUserAccount.getUsername() != null && newUserAccount.getPassword() != null && newUserAccount.getConfirmedPassword() != null && newUserAccount.getPassword().equals(newUserAccount.getConfirmedPassword())) {
				account = userAccountDao.addNewUserAccount(loggedUser, user, newUserAccount.getUsername(), role, newUserAccount.getPassword());
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modify(@PathVariable Integer id, @Valid @RequestBody UserAccountDTO updateUserAccount, Principal principal, BindingResult result) {
		logger.info("################ /jobster/accounts/{id}/modify started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateUserAccount == null) {
			logger.info("---------------- New user account is null.");
	        return new ResponseEntity<>("New user account is null", HttpStatus.BAD_REQUEST);
	      }
		if (updateUserAccount.getUsername() == null && updateUserAccount.getAccessRole() == null && (updateUserAccount.getPassword() == null || updateUserAccount.getConfirmedPassword() == null) && updateUserAccount.getUserId() == null) {
			logger.info("---------------- All atributes is null.");
			return new ResponseEntity<>("All atributes is null.", HttpStatus.BAD_REQUEST);
		}
		if ( (updateUserAccount.getAccessRole() != null && (updateUserAccount.getAccessRole().equals(" ") || updateUserAccount.getAccessRole().equals(""))) || (updateUserAccount.getUsername() != null && (updateUserAccount.getUsername().equals(" ") || updateUserAccount.getUsername().equals(""))) || (updateUserAccount.getPassword() != null && (updateUserAccount.getPassword().equals(" ") || updateUserAccount.getPassword().equals(""))) || (updateUserAccount.getConfirmedPassword() != null && (updateUserAccount.getConfirmedPassword().equals(" ") || updateUserAccount.getConfirmedPassword().equals(""))) || (updateUserAccount.getUserId() != null && (updateUserAccount.getUserId().equals(" ") || updateUserAccount.getUserId().equals(""))) ) {
			logger.info("---------------- Some or all atributes are blanks.");
			return new ResponseEntity<>("Some or all atributes are blanks", HttpStatus.BAD_REQUEST);
		}
		UserAccountEntity account = new UserAccountEntity();
		try {
			if (updateUserAccount.getUsername() != null && userAccountRepository.getByUsername(updateUserAccount.getUsername()) != null) {
				logger.info("---------------- Username already exists.");
		        return new ResponseEntity<>("Username already exists.", HttpStatus.NOT_ACCEPTABLE);
		    }
			account = userAccountRepository.findByIdAndStatusLike(id, 1);
			if (account == null) {
				logger.info("---------------- User account not found.");
		        return new ResponseEntity<>("User account not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("User account identified.");			
			UserEntity user = new UserEntity();
			EUserRole role = null;
			if (updateUserAccount.getUsername() != null && userAccountRepository.getByUsername(updateUserAccount.getUsername()) != null) {
				logger.info("---------------- Username already exist.");
		        return new ResponseEntity<>("Username already exist.", HttpStatus.NOT_ACCEPTABLE);
		    }
			if (updateUserAccount.getUserId() != null && Integer.parseInt(updateUserAccount.getUserId()) != account.getUser().getId()) {
				user = userRepository.getById(Integer.parseInt(updateUserAccount.getUserId()));
				Integer userStatus = null;
				UserEntity userWithRole = new UserEntity();
				if (user != null) {
					if (updateUserAccount.getAccessRole().equals("ROLE_ADMIN")) {
						userWithRole = userRepository.getByIdAndStatusLike(Integer.parseInt(updateUserAccount.getUserId()), 1);
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_ADMIN;
						}
					} else if (updateUserAccount.getAccessRole().equals("ROLE_USER")) {
						userWithRole = personRepository.getByIdAndStatusLike(Integer.parseInt(updateUserAccount.getUserId()), 1);
						if (userWithRole == null) {
							userWithRole = companyRepository.getByIdAndStatusLike(Integer.parseInt(updateUserAccount.getUserId()), 1);
						}
						if (userWithRole != null) {
							userStatus = userWithRole.getStatus();
							role = EUserRole.ROLE_USER;
						}
					} else {
						logger.info("---------------- User not exist or wrong access role.");
						return new ResponseEntity<>("User not exist or wrong access role.", HttpStatus.BAD_REQUEST);
					}
				}
				if (user == null || userStatus == null || userStatus != 1) {
					logger.info("---------------- User not found with that role.");
					return new ResponseEntity<>("User not found with that role.", HttpStatus.BAD_REQUEST);
				}
			}		
			if (user != null && role !=null && userAccountRepository.findByUserAndAccessRoleAndStatusLike(user, EUserRole.valueOf(updateUserAccount.getAccessRole()), 1) != null) {
				logger.info("---------------- User already have account with that role.");
		        return new ResponseEntity<>("User already have account with that role.", HttpStatus.FORBIDDEN);
		    }	
			if (updateUserAccount.getUserId() == null && updateUserAccount.getAccessRole() != null && userAccountRepository.findByUserAndAccessRoleAndStatusLike(account.getUser(), EUserRole.valueOf(updateUserAccount.getAccessRole()), 1) != null) {
				logger.info("---------------- Other account of same user with that role already exist.");
		        return new ResponseEntity<>("Other account of same user with that role already exist.", HttpStatus.FORBIDDEN);
		    }	
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateUserAccount.getUsername() != null && !updateUserAccount.getUsername().equals("") && !updateUserAccount.getUsername().equals(" ") && userAccountRepository.getByUsername(updateUserAccount.getUsername()) == null) {
				userAccountDao.modifyAccountUsername(loggedUser, account, updateUserAccount.getUsername());
				logger.info("Username modified.");					
			}
			if (updateUserAccount.getPassword() != null && !updateUserAccount.getPassword().equals("") && !updateUserAccount.getPassword().equals(" ") && updateUserAccount.getConfirmedPassword() != null && updateUserAccount.getPassword().equals(updateUserAccount.getConfirmedPassword())) {
				userAccountDao.modifyAccountPassword(loggedUser, account, updateUserAccount.getPassword());
				logger.info("Password modified.");
			}
			if (updateUserAccount.getUserId() != null && updateUserAccount.getAccessRole() != null && user != account.getUser() && !role.equals(account.getAccessRole())) {
				userAccountDao.modifyAccountUserAndAccessRole(loggedUser, account, user, role);
				logger.info("User and access role modified.");
			} else if (updateUserAccount.getUserId() != null && user != account.getUser()) {
				userAccountDao.modifyAccountUser(loggedUser, account, user);
				logger.info("User modified.");
			} else if (updateUserAccount.getAccessRole() != null && !role.equals(account.getAccessRole())) {
				userAccountDao.modifyAccountAccessRole(loggedUser, account, EUserRole.valueOf(updateUserAccount.getAccessRole()));
				logger.info("Access role modified.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(account, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}")
	public ResponseEntity<?> archive(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/accounts/archive/{id}/archive started.");
		logger.info("Logged user: " + principal.getName());
		UserAccountEntity account = new UserAccountEntity();
		try {
			account = userAccountRepository.getById(id);
			if (account == null || account.getStatus() == -1) {
				logger.info("---------------- User account not found.");
		        return new ResponseEntity<>("User account not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("User account for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (account.getUser().getId() == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User account: Cann't archive your account.");
				return new ResponseEntity<>("Selected Id is ID of logged User account: Cann't archive your account.", HttpStatus.FORBIDDEN);
		      }	
			userAccountDao.archiveAccount(loggedUser, account);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}")
	public ResponseEntity<?> unDelete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/accounts/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		UserAccountEntity account = new UserAccountEntity();
		try {
			account = userAccountRepository.findByIdAndStatusLike(id, 0);
			if (account == null) {
				logger.info("---------------- User account not found.");
		        return new ResponseEntity<>("User account not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("User account for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			userAccountDao.undeleteAccount(loggedUser, account);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/accounts/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		UserAccountEntity account = new UserAccountEntity();
		try {
			account = userAccountRepository.findByIdAndStatusLike(id, 1);
			if (account == null) {
				logger.info("---------------- User account not found.");
		        return new ResponseEntity<>("User account not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("User account for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (account.getUser().getId() == loggedUser.getId()) {
				logger.info("---------------- Selected Id is ID of logged User account: Cann't delete your account.");
				return new ResponseEntity<>("Selected Id is ID of logged User account: Cann't delete your account.", HttpStatus.FORBIDDEN);
		      }	
			userAccountDao.deleteAccount(loggedUser, account);
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<UserAccountEntity>(account, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//pagination:
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value="/activePaginated")
	public ResponseEntity<?> getAllActivePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/accounts/getAllActivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("username"));
			Page<UserAccountEntity> accountsPage= userAccountRepository.findByStatusLike(1,pageable);
			Iterable<UserAccountEntity> accounts = accountsPage.getContent();
			
			if (Iterables.isEmpty(accounts)) {
				logger.info("---------------- User accounts not found.");
		        return new ResponseEntity<>("User accounts not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
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
		logger.info("################ /jobster/accounts/getAllDeletedPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("username"));
			Page<UserAccountEntity> accountsPage= userAccountRepository.findByStatusLike(0,pageable);
			Iterable<UserAccountEntity> accounts = accountsPage.getContent();
			
			if (Iterables.isEmpty(accounts)) {
				logger.info("---------------- User accounts not found.");
		        return new ResponseEntity<>("User accounts not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
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
		logger.info("################ /jobster/accounts/getAllArchivedPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("username"));
			Page<UserAccountEntity> accountsPage= userAccountRepository.findByStatusLike(-1,pageable);
			Iterable<UserAccountEntity> accounts = accountsPage.getContent();
			
			if (Iterables.isEmpty(accounts)) {
				logger.info("---------------- User accounts not found.");
		        return new ResponseEntity<>("User accounts not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<Iterable<UserAccountEntity>>(accounts, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}