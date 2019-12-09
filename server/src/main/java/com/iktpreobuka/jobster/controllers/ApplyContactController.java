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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Iterables;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
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
	@RequestMapping(method = RequestMethod.GET) // get all comments
	public ResponseEntity<?> getAllActive(Principal principal) {
		logger.info("################ /jobster/apply/getAllActive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByStatusLike(1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/inactive") // get all inactive
	public ResponseEntity<?> getAllInactive(Principal principal) {
		logger.info("################ /jobster/apply/getAllInactive started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByStatusLike(0);
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
	@RequestMapping(method = RequestMethod.GET, value = "/archived") // get all archived
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/apply/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<ApplyContactEntity> applications = applyContactRepository.findByStatusLike(-1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/active/{id}") // get active by ID
	public ResponseEntity<?> getActivedById(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/getActivedById started.");
		logger.info("Logged username: " + principal.getName());
		try {
			ApplyContactEntity application = applyContactRepository.findByIdAndStatusLike(id, 1);
			if (application == null) {
				logger.info("++++++++++++++++ Active application not found");
				return new ResponseEntity<>("Active application not found",HttpStatus.NOT_FOUND);
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
				return new ResponseEntity<>("Inactive application not found",HttpStatus.NOT_FOUND);
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
				return new ResponseEntity<>("Archived applicaiton not found",HttpStatus.NOT_FOUND);
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
	@RequestMapping(method = RequestMethod.GET, value = "/all/{id}") // get by ID
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
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/offerActive/{id}")
	public ResponseEntity<?> getActiveApplicationsByActiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/offerActive/getActiveApplicationsByActiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/offerActive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByActiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/inactive/offerActive/getInactiveApplicationsByActiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 0);
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
	@RequestMapping(method = RequestMethod.GET, value = "/archived/offerActive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByActiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/archived/offerActive/getArchivedApplicationsByActiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, -1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/offerActive/{id}")
	public ResponseEntity<?> getByActiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/offerActive/getByActiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOffer(offer);
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
	@RequestMapping(method = RequestMethod.GET, value = "/active/offerInactive/{id}")
	public ResponseEntity<?> getActiveApplicationsByInactiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/offerInactive/getActiveApplicationsByInactiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/offerInactive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByInactiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/inactive/offerInactive/getInactiveApplicationsByInactiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 0);
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
	@RequestMapping(method = RequestMethod.GET, value = "/archived/offerInactive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByInactiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/archived/offerInactive/getArchivedApplicationsByInactiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, -1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/offerInactive/{id}")
	public ResponseEntity<?> getByInactiveOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/offerInactive/getByInactiveOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, 0);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOffer(offer);
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
	@RequestMapping(method = RequestMethod.GET, value = "/active/offerArchived/{id}")
	public ResponseEntity<?> getActiveApplicationsByArchivedOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/offerArchived/getActiveApplicationsByArchivedOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/offerArchived/{id}")
	public ResponseEntity<?> getInactiveApplicationsByArchivedOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/inactive/offerArchived/getInactiveApplicationsByArchivedOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, 0);
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
	@RequestMapping(method = RequestMethod.GET, value = "/archived/offerArchived/{id}")
	public ResponseEntity<?> getArchivedApplicationsByArchivedOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/archived/offerArchived/getArchivedApplicationsByArchivedOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOfferAndStatusLike(offer, -1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/offerArchived/{id}")
	public ResponseEntity<?> getByArchivedOffer(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/offerArchived/getByArchivedOffer started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobOfferEntity offer = jobOfferRepository.findByIdAndStatusLike(id, -1);
			if(offer==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findByOffer(offer);
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

	

	//OVDE KRECE PAKAO
	
	// @Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/active/seekActive/{id}")
	public ResponseEntity<?> getActiveApplicationsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/seekActive/getActiveApplicationsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/seekActive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/inactive/seekActive/getInactiveApplicationsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 0);
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
	@RequestMapping(method = RequestMethod.GET, value = "/archived/seekActive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/archived/seekActive/getArchivedApplicationsByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, -1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/seekActive/{id}")
	public ResponseEntity<?> getByActiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/seekActive/getByActiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeek(seek);
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
	@RequestMapping(method = RequestMethod.GET, value = "/active/seekInactive/{id}")
	public ResponseEntity<?> getActiveApplicationsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/seekInactive/getActiveApplicationsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/seekInactive/{id}")
	public ResponseEntity<?> getInactiveApplicationsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/inactive/seekInactive/getInactiveApplicationsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 0);
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
	@RequestMapping(method = RequestMethod.GET, value = "/archived/seekInactive/{id}")
	public ResponseEntity<?> getArchivedApplicationsByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/archived/seekInactive/getArchivedApplicationsByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, -1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/seekInactive/{id}")
	public ResponseEntity<?> getByInactiveSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/seekInactive/getByInactiveSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, 0);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeek(seek);
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
	@RequestMapping(method = RequestMethod.GET, value = "/active/seekArchived/{id}")
	public ResponseEntity<?> getActiveApplicationsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/active/seekArchived/getActiveApplicationsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/inactive/seekArchived/{id}")
	public ResponseEntity<?> getInactiveApplicationsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/inactive/seekArchived/getInactiveApplicationsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, 0);
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
	@RequestMapping(method = RequestMethod.GET, value = "/archived/seekArchived/{id}")
	public ResponseEntity<?> getArchivedApplicationsByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/archived/seekArchived/getArchivedApplicationsByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if(seek==null) {
				logger.info("++++++++++++++++ Seek not found");
				return new ResponseEntity<>("Seek not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeekAndStatusLike(seek, -1);
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
	@RequestMapping(method = RequestMethod.GET, value = "/seekArchived/{id}")
	public ResponseEntity<?> getByArchivedSeek(@PathVariable Integer id, Principal principal) {
		logger.info("################ /jobster/apply/seekArchived/getByArchivedSeek started.");
		logger.info("Logged username: " + principal.getName());
		try {
			JobSeekEntity seek = jobSeekRepository.findByIdAndStatusLike(id, -1);
			if(seek==null) {
				logger.info("++++++++++++++++ Offer not found");
				return new ResponseEntity<>("Offer not found",HttpStatus.NOT_FOUND);
			}
			Iterable <ApplyContactEntity> applications = applyContactRepository.findBySeek(seek);
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
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/myApplies")
	public ResponseEntity<?> getMyAppliesQuery( Principal principal, 
			@RequestParam(required = false) Boolean commentable,
			@RequestParam(required = false) Boolean rejected,
			@RequestParam(required = false) Boolean connected,
			@RequestParam(required = false) Boolean expired) {
		logger.info("################ /jobster/apply/myApplies/getMyAppliesQuery started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Integer loggedInUserId = userAccountRepository.getByUsername(principal.getName()).getUser().getId();
			Iterable <ApplyContactEntity> applications = applyContactDao.findByQueryForLoggedInUser(loggedInUserId,rejected,connected,expired,commentable);
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

}
