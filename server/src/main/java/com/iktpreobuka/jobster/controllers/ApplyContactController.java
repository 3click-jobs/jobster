package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.repositories.ApplyContactRepository;
import com.iktpreobuka.jobster.repositories.CommentRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;
import com.iktpreobuka.jobster.security.Views;
import com.iktpreobuka.jobster.services.ApplyContactDao;
import com.iktpreobuka.jobster.services.CommentDao;
import com.iktpreobuka.jobster.services.EmailDao;

@RestController
@RequestMapping(path = "/jobster/apply")
public class ApplyContactController {

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	ApplyContactRepository applyContactRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ApplyContactDao applyContactDao;

	@Autowired
	CommentDao commentDao;

	@Autowired
	EmailDao emailDao;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all") // get all comments
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/apply/all/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findAll();
			logger.info("---------------- Found applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET) // get all comments
	public ResponseEntity<?> getAllActive(Principal principal) {
		logger.info("################ /jobster/apply/getAllActive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByStatusLike(1);
			logger.info("---------------- Found active applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive") // get all inactive
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/apply/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByStatusLike(0);
			logger.info("---------------- Found inactive applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived") // get all archived
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/apply/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByStatusLike(-1);
			logger.info("---------------- Found archived applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/{id}") // get active by ID
	public ResponseEntity<?> getActivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/getActivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, 1);
			if (application == null) {
				logger.info("++++++++++++++++ Active application not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found application - OK.");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/{id}") // get inactive by ID
	public ResponseEntity<?> getInactivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/inactive/getInactivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, 0);
			if (application == null) {
				logger.info("++++++++++++++++ Inactive application not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive application - OK.");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}") // get archived by ID
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, -1);
			if (application == null) {
				logger.info("++++++++++++++++ Archived application not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived application - OK.");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all/{id}") // get archived by ID
	public ResponseEntity<?> getByIdAll(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/all/getByIdAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ApplyContactEntity application = applyContactRepository.findById(id).orElse(null);
			if (application == null) {
				logger.info("++++++++++++++++ Application not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found application - OK.");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	


}
