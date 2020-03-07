package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Iterables;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.RejectSeekEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.repositories.RejectSeekRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;
import com.iktpreobuka.jobster.security.Views;
import com.iktpreobuka.jobster.services.RejectSeekDao;

@RestController
@RequestMapping(path = "/jobster/reject/seek")
public class RejectSeekController {

	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	RejectSeekRepository rejectSeekRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JobSeekRepository jobSeekRepository;

	@Autowired
	RejectSeekDao rejectSeekDao;


	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("unused")
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	// *************** GET ALL *************

	// ************* get all rejections ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all")
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/reject/seek/all/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findAll();
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No rejections found");
				return new ResponseEntity<>("No rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// ************* get all active rejections ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/admin")
	public ResponseEntity<?> getAllActiveByAdmin(Principal principal) {
		logger.info("################ /jobster/reject/seek/admin/getAllActive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByStatusLike(1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No active rejections found");
				return new ResponseEntity<>("No active rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllActive(Principal principal) {
		logger.info("################ /jobster/reject/seek/getAllActive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (loggedUser == null) {
				logger.error("++++++++++++++++ User attempting to reject seek was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByUserAndStatusLike(loggedUser, 1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No active rejections found");
				return new ResponseEntity<>("No active rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get all inactive rejections ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/admin")
	public ResponseEntity<?> getAllInactiveByAdmin(Principal principal) {
		logger.info("################ /jobster/reject/seek/admin/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByStatusLike(0);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No inactive rejections found");
				return new ResponseEntity<>("No inactive rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive")
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/reject/seek/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (loggedUser == null) {
				logger.error("++++++++++++++++ User attempting to reject seek was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByUserAndStatusLike(loggedUser, 0);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No inactive rejections found");
				return new ResponseEntity<>("No inactive rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get all archived rejections ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/admin")
	public ResponseEntity<?> getAllArchivedByAdmin(Principal principal) {
		logger.info("################ /jobster/reject/seek/admin/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByStatusLike(-1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No archived rejections found");
				return new ResponseEntity<>("No archived rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({"ROLE_ADMIN", "ROLE_USER"})
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/reject/seek/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (loggedUser == null) {
				logger.error("++++++++++++++++ User attempting to reject seek was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByUserAndStatusLike(loggedUser, -1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No archived rejections found");
				return new ResponseEntity<>("No archived rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// *************** GET BY ID *************
	// ************* get active rejection by ID ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/{id}")
	public ResponseEntity<?> getActiveById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/active/getActivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			RejectSeekEntity rejection = rejectSeekRepository.findByIdAndStatusLike(id, 1);
			if (rejection == null) {
				logger.info("++++++++++++++++ Active rejection not found");
				return new ResponseEntity<>("Active rejection not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejection - OK.");
			return new ResponseEntity<RejectSeekEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive rejection by id************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/{id}")
	public ResponseEntity<?> getInactivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/inactive/getInactivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			RejectSeekEntity rejection = rejectSeekRepository.findByIdAndStatusLike(id, 0);
			if (rejection == null) {
				logger.info("++++++++++++++++ Inactive rejection not found");
				return new ResponseEntity<>("Inactive rejection not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejection - OK.");
			return new ResponseEntity<RejectSeekEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived rejection by id************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			RejectSeekEntity rejection = rejectSeekRepository.findByIdAndStatusLike(id, -1);
			if (rejection == null) {
				logger.info("++++++++++++++++ Archived rejection not found");
				return new ResponseEntity<>("Archived applicaiton not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejection - OK.");
			return new ResponseEntity<RejectSeekEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get rejection by id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all/{id}")
	public ResponseEntity<?> getByIdAll(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/all/getByIdAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			RejectSeekEntity rejection = rejectSeekRepository.findById(id).orElse(null);
			if (rejection == null) {
				logger.info("++++++++++++++++ Rejection not found");
				return new ResponseEntity<>("Rejection not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejection - OK.");
			return new ResponseEntity<RejectSeekEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// *************** GET BY SEEK *************
	// ************* get active rejections by active seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/seekActive/{id}")
	public ResponseEntity<?> getActiveRejectionsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/active/seekActive/getActiveRejectionsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, 1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Active rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive rejections by active seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/seekActive/{id}")
	public ResponseEntity<?> getInactiveRejectionsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/inactive/seekActive/getInactiveRejectionsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, 0);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Inactive rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived rejections by active seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/seekActive/{id}")
	public ResponseEntity<?> getArchivedRejectionsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/archived/seekActive/getArchivedRejectionsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, -1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Archived rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get rejections by active seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/seekActive/{id}")
	public ResponseEntity<?> getByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/seekActive/getByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeek(seek);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get active rejections by inactive seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/seekInactive/{id}")
	public ResponseEntity<?> getActiveRejectionsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/active/seekInactive/getActiveRejectionsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, 1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Active rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive rejections by inactive seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/seekInactive/{id}")
	public ResponseEntity<?> getInactiveRejectionsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/reject/seek/inactive/seekInactive/getInactiveRejectionsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, 0);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived rejections by inactive seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/seekInactive/{id}")
	public ResponseEntity<?> getArchivedRejectionsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/reject/seek/archived/seekInactive/getArchivedRejectionsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, -1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get rejections by inactive seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/seekInactive/{id}")
	public ResponseEntity<?> getByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/seekInactive/getByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeek(seek);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get active rejections by archived seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/seekArchived/{id}")
	public ResponseEntity<?> getActiveRejectionsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/active/seekArchived/getActiveRejectionsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, 1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive rejections by archived seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/seekArchived/{id}")
	public ResponseEntity<?> getInactiveRejectionsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/reject/seek/inactive/seekArchived/getInactiveRejectionsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, 0);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived rejections by archived seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/seekArchived/{id}")
	public ResponseEntity<?> getArchivedRejectionsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/reject/seek/archived/seekArchived/getArchivedRejectionsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, -1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get rejections by archived seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/seekArchived/{id}")
	public ResponseEntity<?> getByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/seek/seekArchived/getByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if (seek == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectSeekEntity> rejections = rejectSeekRepository.findByRejectedSeek(seek);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
//***************************************** pagination:	


	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value="/activePaginated") // get all active comments
	public ResponseEntity<?> getAllActivePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/reject/seek/getAllActivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByStatusLike(1, pageable);
			Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();

			if(Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No active rejections found");
				return new ResponseEntity<>("No activerejections found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated") // get all inactive
	public ResponseEntity<?> getAllInactivePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/reject/seek/getAllInactivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByStatusLike(0, pageable);
			Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
			if(Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No inactive rejections found");
				return new ResponseEntity<>("No inactive rejections found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated") // get all archived
	public ResponseEntity<?> getAllArchivedPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/reject/seek/getAllArchivedPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByStatusLike(-1, pageable);
			Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
			if(Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No archived rejections found");
				return new ResponseEntity<>("No archived rejections found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/seekActive/{id}")
	public ResponseEntity<?> getActiveRejectionsByActiveSeekPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/reject/seek/active/seekActive/getActiveRejectionsByActiveSeekPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek,1, pageable);
			Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Active rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/seekActive/{id}")
	public ResponseEntity<?> getInactiveRejectionsByActiveSeekPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/reject/seek/inactive/seekActive/getInactiveRejectionsByActiveSeekPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek,0, pageable);
			Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Inactive rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/seekActive/{id}")
	public ResponseEntity<?> getArchivedRejectionsByActiveSeekPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/reject/seek/archived/seekActive/getArchivedRejectionsByActiveSeekPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek,-1, pageable);
			Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Archived rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/seekActivePaginated/{id}")
		public ResponseEntity<?> getByActiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/seek/seekActive/getByActiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, pageable);
				Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found archived rejections rejection - OK.");
				return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/seekInactive/{id}")
		public ResponseEntity<?> getActiveRejectionsByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/seek/active/seekInactive/getActiveRejectionsByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek,1, pageable);
				Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Active rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found active rejections rejection - OK.");
				return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/seekInactive/{id}")
		public ResponseEntity<?> getInactiveRejectionsByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/seek/inactive/seekInactive/getInactiveRejectionsByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek,0, pageable);
				Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/seekInactive/{id}")
		public ResponseEntity<?> getArchivedRejectionsByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/seek/archived/seekInactive/getArchivedRejectionsByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek,-1, pageable);
				Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/seekInactivePaginated/{id}")
		public ResponseEntity<?> getByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/seek/seekInactive/getByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, pageable);
				Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found rejections - OK.");
				return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/seekArchived/{id}")
		public ResponseEntity<?> getActiveRejectionsByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/seek/active/seekArchived/getActiveRejectionsByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek,1, pageable);
				Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/seekArchived/{id}")
		public ResponseEntity<?> getInactiveRejectionsByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/seek/inactive/seekArchived/getInactiveRejectionsByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek,0, pageable);
				Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/seekArchived/{id}")
		public ResponseEntity<?> getArchivedRejectionsByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/seek/archived/seekArchived/getArchivedRejectionsByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek,-1, pageable);
				Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/seekArchivedPagianted/{id}")
		public ResponseEntity<?> getByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/seek/seekArchived/getByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
				if(seek==null) {
					logger.info("++++++++++++++++ Offer not found");
					return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectSeekEntity> rejectionsPage = rejectSeekRepository.findByRejectedSeekAndStatusLike(seek, pageable);
				Iterable<RejectSeekEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found rejections - OK.");
				return new ResponseEntity<Iterable<RejectSeekEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	
		
		/*
		 * ************************************ POST, DELETE, ARCHIVE, UNDELETE... *******************************
		 * */
	
	// @Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, value = "/{seekId}")
	public ResponseEntity<?> newSeekRejection(@PathVariable Integer seekId, Principal principal) {
		logger.info("################ /jobster/reject/seek/{seekId}/newSeekRejection started.");
		logger.info("Logged username: " + principal.getName());

		RejectSeekEntity rejection = new RejectSeekEntity();

		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(seekId, 1);
			if (seek == null) {
				logger.info("---------------- Job seek not found.");
				return new ResponseEntity<>("Job seek not found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Job seek Found");

			UserEntity loggedUser = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (loggedUser == null) {
				logger.error("++++++++++++++++ User attempting to reject seek was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			if (loggedUser.getId() == seek.getEmployee().getId()) {
				logger.error("---------------- " + principal.getName() + " cannot reject own job seek.");
				return new ResponseEntity<>(principal.getName() + " cannot reject own job seek.",
						HttpStatus.FORBIDDEN);
			}
			logger.info("---------------- Logged user found");

			rejection.setUser(loggedUser);
			rejection.setRejectedSeek(seek);
			rejection.setCreatedById(loggedUser.getId());
			rejection.setStatusActive();
			rejection.setUpdatedById(null);
			rejection.setRejectionDate(new Date());
			logger.info("---------------- Rejection created!!!");
			
			rejectSeekRepository.save(rejection);
			logger.info("---------------- Rejection saved in DB!!!");
						
			return new ResponseEntity<>(rejection, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
		
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}") 
	public ResponseEntity<?> deactivateRejection(@PathVariable Integer id, Principal principal) {
		try {
			logger.info("################ /jobster/reject/seek/deactivateRejection started.");
			logger.info("Logged username: " + principal.getName());
			RejectSeekEntity rejection = rejectSeekRepository.findByIdAndStatusLike(id, 1);
			if (rejection == null) {
				logger.error("++++++++++++++++ Active rejection not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			UserEntity user = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (user == null) {
				logger.error("++++++++++++++++ User attempting to delete was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- User and rejection found OK");
			rejectSeekDao.deleteRejection(user.getId(), rejection);
			logger.info("---------------- Rejection deactivated and updated by updated!!!");
						
			return new ResponseEntity<RejectSeekEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}") 
	public ResponseEntity<?> activateRejection(@PathVariable Integer id, Principal principal) {
		try {
			logger.info("################ /jobster/reject/seek/activateRejection started.");
			logger.info("Logged username: " + principal.getName());
			RejectSeekEntity rejection = rejectSeekRepository.findByIdAndStatusLike(id, 0);
			if (rejection == null) {
				logger.error("++++++++++++++++ Inactive rejection not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			UserEntity user = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (user == null) {
				logger.error("++++++++++++++++ User attempting to undelete was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- User and rejection found OK");
			rejectSeekDao.undeleteRejection(user.getId(), rejection);
			logger.info("---------------- Rejection activated and updated by updated!!!");
			return new ResponseEntity<RejectSeekEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}") 
	public ResponseEntity<?> archiveRejection(@PathVariable Integer id, Principal principal) {
		try {
			logger.info("################ /jobster/reject/seek/archiveRejection started.");
			logger.info("Logged username: " + principal.getName());
			RejectSeekEntity rejection = rejectSeekRepository.findById(id).orElse(null);
			if (rejection == null || rejection.getStatus() == -1) {
				logger.error("++++++++++++++++ Rejection not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			UserEntity user = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (user == null) {
				logger.error("++++++++++++++++ User attempting to archive was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- User and rejection found OK");
			rejectSeekDao.archiveRejection(user.getId(), rejection);
			logger.info("---------------- Rejection archived and updated by updated!!!");
			return new ResponseEntity<RejectSeekEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
