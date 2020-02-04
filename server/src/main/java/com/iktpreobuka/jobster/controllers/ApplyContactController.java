package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
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
//import com.google.common.collect.Iterables;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.repositories.ApplyContactRepository;
import com.iktpreobuka.jobster.repositories.CommentRepository;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
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
	JobOfferRepository jobOfferRepository;

	@Autowired
	JobSeekRepository jobSeekRepository;

	@Autowired
	ApplyContactDao applyContactDao;

	@Autowired
	CommentDao commentDao;

	@Autowired
	EmailDao emailDao;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("unused")
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	// *************** GET ALL *************

	// ************* get all applications ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all")
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/apply/all/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findAll();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ No applications found");
				return new ResponseEntity<>("No applications found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// ************* get all active applications ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllActive(Principal principal) {
		logger.info("################ /jobster/apply/getAllActive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByStatusLike(1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ No active applications found");
				return new ResponseEntity<>("No activeapplications found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get all inactive applications ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive")
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/apply/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByStatusLike(0);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ No inactive applications found");
				return new ResponseEntity<>("No inactive applications found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get all archived applications ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/apply/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByStatusLike(-1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ No archived applications found");
				return new ResponseEntity<>("No archived applications found.", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// *************** GET BY ID *************
	// ************* get active application by ID ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/{id}")
	public ResponseEntity<?> getActiveById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/getActivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, 1);
			if (application == null) {
				logger.info("++++++++++++++++ Active application not found");
				return new ResponseEntity<>("Active application not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found application - OK.");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive application by id************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/{id}")
	public ResponseEntity<?> getInactivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/inactive/getInactivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, 0);
			if (application == null) {
				logger.info("++++++++++++++++ Inactive application not found");
				return new ResponseEntity<>("Inactive application not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive application - OK.");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived application by id************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/{id}")
	public ResponseEntity<?> getArchivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/archived/getArchivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, -1);
			if (application == null) {
				logger.info("++++++++++++++++ Archived application not found");
				return new ResponseEntity<>("Archived applicaiton not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived application - OK.");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get application by id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/all/{id}")
	public ResponseEntity<?> getByIdAll(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/all/getByIdAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ApplyContactEntity application = applyContactRepository.findById(id).orElse(null);
			if (application == null) {
				logger.info("++++++++++++++++ Application not found");
				return new ResponseEntity<>("Application not found", HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found application - OK.");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// *************** GET BY OFFER *************
	// ************* get active applications by active offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/offerActive/{id}")
	public ResponseEntity<?> getActiveApplicationsByActiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/offerActive/getActiveApplicationsByActiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Active applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// ************* get inactive applications by active offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/offerActive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByActiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/inactive/offerActive/getInactiveApplicationsByActiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 0);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Inactive applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// ************* get archived applications by active offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/offerActive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByActiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/archived/offerActive/getArchivedApplicationsByActiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, -1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Archived applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get applications by active offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/offerActive/{id}")
	public ResponseEntity<?> getByActiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/offerActive/getByActiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOffer(offer);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get active applications by inactive offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/offerInactive/{id}")
	public ResponseEntity<?> getActiveApplicationsByInactiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/active/offerInactive/getActiveApplicationsByInactiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Active applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive applications by inactive offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/offerInactive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByInactiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/inactive/offerInactive/getInactiveApplicationsByInactiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 0);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived applications by inactive offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/offerInactive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByInactiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/archived/offerInactive/getArchivedApplicationsByInactiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, -1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get applications by inactive offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/offerInactive/{id}")
	public ResponseEntity<?> getByInactiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/offerInactive/getByInactiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOffer(offer);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get active applications by archived offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/offerArchived/{id}")
	public ResponseEntity<?> getActiveApplicationsByArchivedOffer(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/active/offerArchived/getActiveApplicationsByArchivedOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive applications by archived offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/offerArchived/{id}")
	public ResponseEntity<?> getInactiveApplicationsByArchivedOffer(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/inactive/offerArchived/getInactiveApplicationsByArchivedOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 0);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived applications by archived offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/offerArchived/{id}")
	public ResponseEntity<?> getArchivedApplicationsByArchivedOffer(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/archived/offerArchived/getArchivedApplicationsByArchivedOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, -1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get applications by archived offer id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/offerArchived/{id}")
	public ResponseEntity<?> getByArchivedOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/offerArchived/getByArchivedOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if (offer == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByOffer(offer);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	// *************** GET BY SEEK *************
	// ************* get active applications by active seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/seekActive/{id}")
	public ResponseEntity<?> getActiveApplicationsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/seekActive/getActiveApplicationsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Active applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive applications by active seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/seekActive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/inactive/seekActive/getInactiveApplicationsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 0);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Inactive applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived applications by active seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/seekActive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/archived/seekActive/getArchivedApplicationsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, -1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Archived applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get applications by active seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/seekActive/{id}")
	public ResponseEntity<?> getByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/seekActive/getByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeek(seek);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get active applications by inactive seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/seekInactive/{id}")
	public ResponseEntity<?> getActiveApplicationsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/seekInactive/getActiveApplicationsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Active applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive applications by inactive seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/seekInactive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/inactive/seekInactive/getInactiveApplicationsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 0);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived applications by inactive seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/seekInactive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/archived/seekInactive/getArchivedApplicationsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, -1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get applications by inactive seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/seekInactive/{id}")
	public ResponseEntity<?> getByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/seekInactive/getByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeek(seek);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get active applications by archived seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/seekArchived/{id}")
	public ResponseEntity<?> getActiveApplicationsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/seekArchived/getActiveApplicationsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get inactive applications by archived seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/seekArchived/{id}")
	public ResponseEntity<?> getInactiveApplicationsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/inactive/seekArchived/getInactiveApplicationsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 0);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get archived applications by archived seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived/seekArchived/{id}")
	public ResponseEntity<?> getArchivedApplicationsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info(
				"################ /jobster/apply/archived/seekArchived/getArchivedApplicationsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if (seek == null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, -1);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* get applications by archived seek id ************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/seekArchived/{id}")
	public ResponseEntity<?> getByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/seekArchived/getByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if (seek == null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
			}
			Iterable<ApplyContactEntity> applications = applyContactRepository.findBySeek(seek);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ************* GET MY APPLICATIONS WITH FILTER************
	
	//not using this one, use the pagable
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/myApplies")
	public ResponseEntity<?> getMyAppliesQuery(Principal principal, @RequestParam(required = false) Boolean commentable,
			@RequestParam(required = false) Boolean rejected, @RequestParam(required = false) Boolean connected,
			@RequestParam(required = false) Boolean expired) {
		logger.info("################ /jobster/apply/myApplies/getMyAppliesQuery started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Integer loggedInUserId = userAccountRepository.getByUsername(principal.getName()).getUser().getId();
			Iterable<ApplyContactEntity> applications = applyContactDao.findByQueryAndUser(loggedInUserId,1,
					rejected, connected, expired, commentable,null,null,null,null);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
//***************************************** pagination:	


	// @Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/allPaginated") // get all comments
	public ResponseEntity<?> getAllPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize, //Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, // Optional<Direction> direction,
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/apply/all/getAllPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Page<ApplyContactEntity> applicationsPage = applyContactDao.findAll(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if(Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ No applications found");
				return new ResponseEntity<>("No applications found.",HttpStatus.NOT_FOUND);
			}
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
	@RequestMapping(method = RequestMethod.GET, value="/activePaginated") // get all active comments
	public ResponseEntity<?> getAllActivePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/apply/getAllActivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByStatusLike(1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();

			if(Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ No active applications found");
				return new ResponseEntity<>("No activeapplications found.",HttpStatus.NOT_FOUND);
			}
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
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated") // get all inactive
	public ResponseEntity<?> getAllInactivePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/apply/getAllInactivePaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByStatusLike(0, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if(Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ No inactive applications found");
				return new ResponseEntity<>("No inactive applications found.",HttpStatus.NOT_FOUND);
			}
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
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated") // get all archived
	public ResponseEntity<?> getAllArchivedPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			Principal principal) {
		logger.info("################ /jobster/apply/getAllArchivedPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByStatusLike(-1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if(Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ No archived applications found");
				return new ResponseEntity<>("No archived applications found.",HttpStatus.NOT_FOUND);
			}
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
	@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/offerActive/{id}")
	public ResponseEntity<?> getActiveApplicationsByActiveOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id,
			Principal principal) {
		logger.info("################ /jobster/apply/active/offerActive/getActiveApplicationsByActiveOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer, 1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Active applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/offerActive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByActiveOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/inactive/offerActive/getInactiveApplicationsByActiveOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer, 0, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Inactive applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/offerActive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByActiveOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/archived/offerActive/getArchivedApplicationsByActiveOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer, -1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();			
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Archived applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/offerActivePaginated/{id}")
	public ResponseEntity<?> getByActiveOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/offerActive/getByActiveOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}			
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();	
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/offerInactive/{id}")
	public ResponseEntity<?> getActiveApplicationsByInactiveOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/offerInactive/getActiveApplicationsByInactiveOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer,1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();	
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Active applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/offerInactive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByInactiveOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/inactive/offerInactive/getInactiveApplicationsByInactiveOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer,0, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/offerInactive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByInactiveOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id,
			Principal principal) {
		logger.info("################ /jobster/apply/archived/offerInactive/getArchivedApplicationsByInactiveOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer,-1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/offerInactivePaginated/{id}")
	public ResponseEntity<?> getByInactiveOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/offerInactive/getByInactiveOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
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
	@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/offerArchived/{id}")
	public ResponseEntity<?> getActiveApplicationsByArchivedOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/active/offerArchived/getActiveApplicationsByArchivedOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer,1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/offerArchived/{id}")
	public ResponseEntity<?> getInactiveApplicationsByArchivedOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id,
			Principal principal) {
		logger.info("################ /jobster/apply/inactive/offerArchived/getInactiveApplicationsByArchivedOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer,0, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/offerArchived/{id}")
	public ResponseEntity<?> getArchivedApplicationsByArchivedOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/archived/offerArchived/getArchivedApplicationsByArchivedOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer,1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/offerArchivedPaginated/{id}")
	public ResponseEntity<?> getByArchivedOfferPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/offerArchived/getByArchivedOfferPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findByOfferAndStatusLike(offer, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found pplications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/seekActive/{id}")
	public ResponseEntity<?> getActiveApplicationsByActiveSeekPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/active/seekActive/getActiveApplicationsByActiveSeekPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek,1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Active applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found active applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/seekActive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByActiveSeekPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/inactive/seekActive/getInactiveApplicationsByActiveSeekPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek,0, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Inactive applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found inactive applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/seekActive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByActiveSeekPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id, 
			Principal principal) {
		logger.info("################ /jobster/apply/archived/seekActive/getArchivedApplicationsByActiveSeekPaginated started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek,-1, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Archived applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found archived applications application - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
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
			logger.info("################ /jobster/apply/seekActive/getByActiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek, pageable);
				Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found archived applications application - OK.");
				return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/seekInactive/{id}")
		public ResponseEntity<?> getActiveApplicationsByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/apply/active/seekInactive/getActiveApplicationsByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek,1, pageable);
				Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Active applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found active applications application - OK.");
				return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/seekInactive/{id}")
		public ResponseEntity<?> getInactiveApplicationsByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/apply/inactive/seekInactive/getInactiveApplicationsByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek,0, pageable);
				Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications application - OK.");
				return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/seekInactive/{id}")
		public ResponseEntity<?> getArchivedApplicationsByInactiveSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/apply/archived/seekInactive/getArchivedApplicationsByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek,-1, pageable);
				Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications application - OK.");
				return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
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
			logger.info("################ /jobster/apply/seekInactive/getByInactiveSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek, pageable);
				Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
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
		@RequestMapping(method = RequestMethod.GET, value = "/activePaginated/seekArchived/{id}")
		public ResponseEntity<?> getActiveApplicationsByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/apply/active/seekArchived/getActiveApplicationsByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek,1, pageable);
				Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications application - OK.");
				return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/inactivePaginated/seekArchived/{id}")
		public ResponseEntity<?> getInactiveApplicationsByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/apply/inactive/seekArchived/getInactiveApplicationsByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek,0, pageable);
				Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications application - OK.");
				return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		// @Secured("ROLE_ADMIN")
		@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/archivedPaginated/seekArchived/{id}")
		public ResponseEntity<?> getArchivedApplicationsByArchivedSeekPaginated(
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				@PathVariable Integer id, 
				Principal principal) {
			logger.info("################ /jobster/apply/archived/seekArchived/getArchivedApplicationsByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
				if(seek==null) {
					logger.info("++++++++++++++++ Seek not found");
					return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek,-1, pageable);
				Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found pplications application - OK.");
				return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
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
			logger.info("################ /jobster/apply/seekArchived/getByArchivedSeekPaginated started.");
			logger.info("Logged username: " + principal.getName());
			try {
				JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
				if(seek==null) {
					logger.info("++++++++++++++++ Offer not found");
					return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
				}
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<ApplyContactEntity> applicationsPage = applyContactRepository.findBySeekAndStatusLike(seek, pageable);
				Iterable<ApplyContactEntity> applications = applicationsPage.getContent();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found applications - OK.");
				return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		

//	TREBA LI PAGINACIJA?	
		@Secured("ROLE_ADMIN")
		//@JsonView(Views.Admin.class)
		@RequestMapping(method = RequestMethod.GET, value = "/myAppliesPaginated")
		public ResponseEntity<?> getMyAppliesQueryPaginated( 
				@RequestParam Optional<Integer> page,
				@RequestParam Optional<Integer> pageSize,
				@RequestParam Optional<Sort.Direction> direction, 
				@RequestParam Optional<String> sortBy,
				Principal principal, 
				@RequestParam(required = false) Integer status,
				@RequestParam(required = false) Boolean commentable, @RequestParam(required = false) Boolean rejected,
				@RequestParam(required = false) Boolean connected, @RequestParam(required = false) Boolean expired,
				@RequestParam(required = false) String connectionDateBottom,@RequestParam(required = false) String connectionDateTop,
				@RequestParam(required = false) String contactDateBottom,@RequestParam(required = false) String contactDateTop) {
			logger.info("################ /jobster/apply/myApplies/getMyAppliesQueryPagianted started.");
			logger.info("Logged username: " + principal.getName());
			try {
				Integer loggedInUserId = userAccountRepository.getByUsername(principal.getName()).getUser().getId();
				if (status != null) {
					if (!( status == 0 || status == 1)) {
						logger.info("++++++++++++++++ Status " + status + " is not acceptable");
						return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
					}
				}
				if(applyContactDao.stringDateFormatNotCorrect(connectionDateBottom, connectionDateTop, contactDateBottom, contactDateTop)){
					logger.info("++++++++++++++++ Date format not correct");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				
				PagedListHolder<ApplyContactEntity> applicationsPage = applyContactDao.findByQueryAndUser(loggedInUserId,status,
						rejected,connected,expired,commentable,connectionDateBottom,connectionDateTop,contactDateBottom,contactDateTop, pageable);
				Iterable <ApplyContactEntity> applications =  applicationsPage.getPageList();
				if (Iterables.isEmpty(applications)) {
					logger.info("++++++++++++++++ Applications not found");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				if(applicationsPage.getPageCount() < pageable.getPageNumber()) {
					logger.info("++++++++++++++++ Selected page out of bound");
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				logger.info("---------------- Found applications - OK.");
				return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
				
			} catch (Exception e) {
				logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
				return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}


	// ************* GET APPLICATIONS WITH FILTER************
	@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/applications")
	public ResponseEntity<?> getApplicationsByQuery(@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,Principal principal, @RequestParam(required = false) Integer status,
			@RequestParam(required = false) Boolean commentable, @RequestParam(required = false) Boolean rejected,
			@RequestParam(required = false) Boolean connected, @RequestParam(required = false) Boolean expired,
			@RequestParam(required = false) String connectionDateBottom,@RequestParam(required = false) String connectionDateTop,
			@RequestParam(required = false) String contactDateBottom,@RequestParam(required = false) String contactDateTop) {
		logger.info("################ /jobster/apply/applications/getApplicationsByQuery started.");
		logger.info("Logged username: " + principal.getName());
		try {
			if (status != null) {
				if (!(status == -1 || status == 0 || status == 1)) {
					logger.info("++++++++++++++++ Status " + status + " is not acceptable");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
			if(applyContactDao.stringDateFormatNotCorrect(connectionDateBottom, connectionDateTop, contactDateBottom, contactDateTop)){
				logger.info("++++++++++++++++ Date format not correct");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
			PagedListHolder<ApplyContactEntity> applicationsPage = applyContactDao.findByQuery(status, rejected, connected, expired,
					commentable, connectionDateBottom, connectionDateTop, contactDateBottom, contactDateTop, pageable);
			Iterable<ApplyContactEntity> applications = applicationsPage.getPageList();
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			if(applicationsPage.getPageCount() < pageable.getPageNumber()) {
				logger.info("++++++++++++++++ Selected page out of bound");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// ************* GET APPLICATIONS BY USER ID WITH FILTER************ NOT CORRECT
	/*@Secured("ROLE_ADMIN")
	// @JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
	public ResponseEntity<?> getMyAppliesQuery(Principal principal, @PathVariable Integer id, @RequestParam(required = false) Integer status,@RequestParam(required = false) Boolean commentable,
			@RequestParam(required = false) Boolean rejected, @RequestParam(required = false) Boolean connected,
			@RequestParam(required = false) Boolean expired) {
		logger.info("################ /jobster/apply/myApplies/getMyAppliesQuery started.");
		logger.info("Logged username: " + principal.getName());
		try {
			if (status != null) {
				if (status != -1 || status != 0 || status != 1) {
					logger.info("++++++++++++++++ Status " + status + " is not acceptable");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
			Iterable<ApplyContactEntity> applications = applyContactDao.findByQueryForUser(id,status,
					rejected, connected, expired, commentable);
			if (Iterables.isEmpty(applications)) {
				logger.info("++++++++++++++++ Applications not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- Found applications - OK.");
			return new ResponseEntity<Iterable<ApplyContactEntity>>(applications, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}") 
	public ResponseEntity<?> deactivateApplication(@PathVariable Integer id, Principal principal) {
		try {
			logger.info("################ /jobster/comment/deactivateApplication started.");
			logger.info("Logged username: " + principal.getName());
			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, 1);
			if (application == null) {
				logger.error("++++++++++++++++ Active application not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			UserEntity user = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (user == null) {
				logger.error("++++++++++++++++ User attempting to delete was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- User and comment found OK");
			applyContactDao.deleteApplication(user.getId(), application);
			logger.info("---------------- Application deactivated and updated by updated!!!");
						
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/undelete/{id}") 
	public ResponseEntity<?> activateApplication(@PathVariable Integer id, Principal principal) {
		try {
			logger.info("################ /jobster/comment/activateApplication started.");
			logger.info("Logged username: " + principal.getName());
			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, 0);
			if (application == null) {
				logger.error("++++++++++++++++ Inactive application not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			UserEntity user = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (user == null) {
				logger.error("++++++++++++++++ User attempting to undelete was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- User and comment found OK");
			applyContactDao.undeleteApplication(user.getId(), application);
			logger.info("---------------- Application activated and updated by updated!!!");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/archive/{id}") 
	public ResponseEntity<?> archiveApplication(@PathVariable Integer id, Principal principal) {
		try {
			logger.info("################ /jobster/comment/archiveApplication started.");
			logger.info("Logged username: " + principal.getName());
			ApplyContactEntity application = applyContactRepository.findById(id).orElse(null);
			if (application == null || application.getStatus() == -1) {
				logger.error("++++++++++++++++ Application not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			UserEntity user = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			if (user == null) {
				logger.error("++++++++++++++++ User attempting to archive was not found");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			logger.info("---------------- User and comment found OK");
			applyContactDao.archiveApplication(user.getId(), application);
			logger.info("---------------- Application archived and updated by updated!!!");
			return new ResponseEntity<ApplyContactEntity>(application, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: " + e.getLocalizedMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
