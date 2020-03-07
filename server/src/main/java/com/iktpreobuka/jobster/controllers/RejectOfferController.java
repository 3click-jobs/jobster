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
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.RejectOfferEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.RejectOfferRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;
import com.iktpreobuka.jobster.security.Views;
import com.iktpreobuka.jobster.services.RejectOfferDao;

@RestController
@RequestMapping(path = "/jobster/reject/offer")
public class RejectOfferController {

	@Autowired
	UserAccountRepository userAccountRepository;

	@Autowired
	RejectOfferRepository rejectOfferRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JobOfferRepository jobOfferRepository;

	@Autowired
	RejectOfferDao rejectOfferDao;


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
		logger.info("################ /jobster/reject/offer/all/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findAll();
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No rejections found");
				return new ResponseEntity<>("No rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejections - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// ************* get all active rejections ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllActive(Principal principal) {
		logger.info("################ /jobster/reject/offer/getAllActive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByStatusLike(1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No active rejections found");
				return new ResponseEntity<>("No active rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get all inactive rejections ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive")
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/reject/offer/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByStatusLike(0);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No inactive rejections found");
				return new ResponseEntity<>("No inactive rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejections - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get all archived rejections ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/reject/offer/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByStatusLike(-1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No archived rejections found");
				return new ResponseEntity<>("No archived rejections found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
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
		logger.info("################ /jobster/reject/offer/active/getActivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			RejectOfferEntity rejection = rejectOfferRepository.findByIdAndStatusLike(id, 1);
			if (rejection == null) {
				logger.info("++++++++++++++++ Active rejection not found");
				return new ResponseEntity<>("Active rejection not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejection - OK.");
			return new ResponseEntity<RejectOfferEntity>(rejection, HttpStatus.OK);
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
		logger.info("################ /jobster/reject/offer/inactive/getInactivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			RejectOfferEntity rejection = rejectOfferRepository.findByIdAndStatusLike(id, 0);
			if (rejection == null) {
				logger.info("++++++++++++++++ Inactive rejection not found");
				return new ResponseEntity<>("Inactive rejection not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejection - OK.");
			return new ResponseEntity<RejectOfferEntity>(rejection, HttpStatus.OK);
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
		logger.info("################ /jobster/reject/offer/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			RejectOfferEntity rejection = rejectOfferRepository.findByIdAndStatusLike(id, -1);
			if (rejection == null) {
				logger.info("++++++++++++++++ Archived rejection not found");
				return new ResponseEntity<>("Archived applicaiton not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejection - OK.");
			return new ResponseEntity<RejectOfferEntity>(rejection, HttpStatus.OK);
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
		logger.info("################ /jobster/reject/offer/all/getByIdAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			RejectOfferEntity rejection = rejectOfferRepository.findById(id).orElse(null);
			if (rejection == null) {
				logger.info("++++++++++++++++ Rejection not found");
				return new ResponseEntity<>("Rejection not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejection - OK.");
			return new ResponseEntity<RejectOfferEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// *************** GET BY SEEK *************
	// ************* get active rejections by active offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/offerActive/{id}")
	public ResponseEntity<?> getActiveRejectionsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/offer/active/offerActive/getActiveRejectionsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, 1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Active rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive rejections by active offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/offerActive/{id}")
	public ResponseEntity<?> getInactiveRejectionsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/offer/inactive/offerActive/getInactiveRejectionsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, 0);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Inactive rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived rejections by active offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/offerActive/{id}")
	public ResponseEntity<?> getArchivedRejectionsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/offer/archived/offerActive/getArchivedRejectionsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, -1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Archived rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get rejections by active offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/offerActive/{id}")
	public ResponseEntity<?> getByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/offer/offerActive/getByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOffer(offer);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get active rejections by inactive offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/offerInactive/{id}")
	public ResponseEntity<?> getActiveRejectionsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/offer/active/offerInactive/getActiveRejectionsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, 1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Active rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive rejections by inactive offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/offerInactive/{id}")
	public ResponseEntity<?> getInactiveRejectionsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/reject/offer/inactive/offerInactive/getInactiveRejectionsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, 0);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived rejections by inactive offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/offerInactive/{id}")
	public ResponseEntity<?> getArchivedRejectionsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/reject/offer/archived/offerInactive/getArchivedRejectionsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, -1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get rejections by inactive offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/offerInactive/{id}")
	public ResponseEntity<?> getByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/offer/offerInactive/getByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOffer(offer);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejections - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get active rejections by archived offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/offerArchived/{id}")
	public ResponseEntity<?> getActiveRejectionsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/offer/active/offerArchived/getActiveRejectionsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, 1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive rejections by archived offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/offerArchived/{id}")
	public ResponseEntity<?> getInactiveRejectionsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/reject/offer/inactive/offerArchived/getInactiveRejectionsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, 0);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived rejections by archived offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/offerArchived/{id}")
	public ResponseEntity<?> getArchivedRejectionsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/reject/offer/archived/offerArchived/getArchivedRejectionsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if (offer == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, -1);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get rejections by archived offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/offerArchived/{id}")
	public ResponseEntity<?> getByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/reject/offer/offerArchived/getByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<RejectOfferEntity> rejections = rejectOfferRepository.findByRejectedOffer(offer);
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found rejections - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
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
		logger.info("################ /jobster/reject/offer/getAllActivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByStatusLike(1, pageable);
			Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();

			if(Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No active rejections found");
				return new ResponseEntity<>("No activerejections found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
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
		logger.info("################ /jobster/reject/offer/getAllInactivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByStatusLike(0, pageable);
			Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
			if(Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No inactive rejections found");
				return new ResponseEntity<>("No inactive rejections found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejections - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
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
		logger.info("################ /jobster/reject/offer/getAllArchivedPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByStatusLike(-1, pageable);
			Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
			if(Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ No archived rejections found");
				return new ResponseEntity<>("No archived rejections found.",HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/offerActive/{id}")
	public ResponseEntity<?> getActiveRejectionsByActiveSeekPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/reject/offer/active/offerActive/getActiveRejectionsByActiveSeekPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer,1, pageable);
			Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Active rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/offerActive/{id}")
	public ResponseEntity<?> getInactiveRejectionsByActiveSeekPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/reject/offer/inactive/offerActive/getInactiveRejectionsByActiveSeekPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer,0, pageable);
			Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Inactive rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/offerActive/{id}")
	public ResponseEntity<?> getArchivedRejectionsByActiveSeekPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/reject/offer/archived/offerActive/getArchivedRejectionsByActiveSeekPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer,-1, pageable);
			Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
			if (Iterables.isEmpty(rejections)) {
				logger.info("++++++++++++++++ Archived rejections not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived rejections rejection - OK.");
			return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/offerActivePaginated/{id}")
		public ResponseEntity<?> getByActiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/offer/offerActive/getByActiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
				if(offer==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, pageable);
				Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found archived rejections rejection - OK.");
				return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/offerInactive/{id}")
		public ResponseEntity<?> getActiveRejectionsByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/offer/active/offerInactive/getActiveRejectionsByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
				if(offer==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer,1, pageable);
				Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Active rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found active rejections rejection - OK.");
				return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/offerInactive/{id}")
		public ResponseEntity<?> getInactiveRejectionsByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/offer/inactive/offerInactive/getInactiveRejectionsByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
				if(offer==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer,0, pageable);
				Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/offerInactive/{id}")
		public ResponseEntity<?> getArchivedRejectionsByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/offer/archived/offerInactive/getArchivedRejectionsByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
				if(offer==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer,-1, pageable);
				Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/offerInactivePaginated/{id}")
		public ResponseEntity<?> getByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/offer/offerInactive/getByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
				if(offer==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, pageable);
				Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found rejections - OK.");
				return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/offerArchived/{id}")
		public ResponseEntity<?> getActiveRejectionsByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/offer/active/offerArchived/getActiveRejectionsByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
				if(offer==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer,1, pageable);
				Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/offerArchived/{id}")
		public ResponseEntity<?> getInactiveRejectionsByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/offer/inactive/offerArchived/getInactiveRejectionsByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
				if(offer==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer,0, pageable);
				Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/offerArchived/{id}")
		public ResponseEntity<?> getArchivedRejectionsByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/offer/archived/offerArchived/getArchivedRejectionsByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
				if(offer==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer,-1, pageable);
				Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications rejection - OK.");
				return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/offerArchivedPagianted/{id}")
		public ResponseEntity<?> getByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/reject/offer/offerArchived/getByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
				if(offer==null) {
					logger.info("++++++++++++++++ Offer not found");
					return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<RejectOfferEntity> rejectionsPage = rejectOfferRepository.findByRejectedOfferAndStatusLike(offer, pageable);
				Iterable<RejectOfferEntity> rejections = rejectionsPage.getContent();
				if (Iterables.isEmpty(rejections)) {
					logger.info("++++++++++++++++ Rejections not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found rejections - OK.");
				return new ResponseEntity<Iterable<RejectOfferEntity>>(rejections, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.POST, value = "/{offerId}")
	public ResponseEntity<?> newSeekRejection(@PathVariable Integer offerId, Principal principal) {
		logger.info("################ /jobster/reject/offer/{offerId}/newSeekRejection started.");
		logger.info("Logged username: " + principal.getName());

		RejectOfferEntity rejection = new RejectOfferEntity();

		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(offerId, 1);
			if (offer == null) {
				logger.info("---------------- Job offer not found.");
				return new ResponseEntity<>("Job offer not found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Job offer Found");

			UserEntity loggedUser = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (loggedUser.getId() == offer.getEmployer().getId()) {
				logger.error("---------------- " + principal.getName() + " cannot reject own job offer.");
				return new ResponseEntity<>(principal.getName() + " cannot reject own job offer.",
						HttpStatus.FORBIDDEN);
			}
			logger.info("---------------- Logged user found");

			rejection.setUser(loggedUser);
			rejection.setRejectedOffer(offer);
			rejection.setCreatedById(loggedUser.getId());
			rejection.setStatusActive();
			rejection.setUpdatedById(null);
			rejection.setRejectionDate(new Date());
			logger.info("---------------- Rejection created!!!");
			
			rejectOfferRepository.save(rejection);
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
			logger.info("################ /jobster/reject/offer/deactivateRejection started.");
			logger.info("Logged username: " + principal.getName());
			RejectOfferEntity rejection = rejectOfferRepository.findByIdAndStatusLike(id, 1);
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
			rejectOfferDao.deleteRejection(user.getId(), rejection);
			logger.info("---------------- Rejection deactivated and updated by updated!!!");
						
			return new ResponseEntity<RejectOfferEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}") 
	public ResponseEntity<?> activateRejection(@PathVariable Integer id, Principal principal) {
		try {
			logger.info("################ /jobster/reject/offer/activateRejection started.");
			logger.info("Logged username: " + principal.getName());
			RejectOfferEntity rejection = rejectOfferRepository.findByIdAndStatusLike(id, 0);
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
			rejectOfferDao.undeleteRejection(user.getId(), rejection);
			logger.info("---------------- Rejection activated and updated by updated!!!");
			return new ResponseEntity<RejectOfferEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}") 
	public ResponseEntity<?> archiveRejection(@PathVariable Integer id, Principal principal) {
		try {
			logger.info("################ /jobster/reject/offer/archiveRejection started.");
			logger.info("Logged username: " + principal.getName());
			RejectOfferEntity rejection = rejectOfferRepository.findById(id).orElse(null);
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
			rejectOfferDao.archiveRejection(user.getId(), rejection);
			logger.info("---------------- Rejection archived and updated by updated!!!");
			return new ResponseEntity<RejectOfferEntity>(rejection, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
