package com.iktpreobuka.jobster.controllers;

import java.io.InputStream;
import java.security.Principal;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Iterables;
import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.JobTypeEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobTypeDTO;
import com.iktpreobuka.jobster.repositories.JobTypeRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.security.Views;
import com.iktpreobuka.jobster.services.JobTypeDao;
import com.iktpreobuka.jobster.util.CsvUtils;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value= "/jobster/jobtypes")
public class JobTypeController {

	@Autowired
	private JobTypeDao jobTypeDao;

	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
//	@Autowired 
//	private UserCustomValidator userValidator;
//
//	@InitBinder
//	protected void initBinder(final WebDataBinder binder) { 
//		binder.addValidators(userValidator); 
//		}

	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private String createErrorMessage(BindingResult result) { 
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
		}

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Principal principal) {
		logger.info("################ /jobster/jobtypes/getAll started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<JobTypeEntity> jobTypes= jobTypeDao.findJobTypeByStatusLike(1);
			logger.info("---------------- Finished OK.");
			if (Iterables.isEmpty(jobTypes)) {
				logger.info("---------------- Job types not found.");
		        return new ResponseEntity<>("Job types not found.", HttpStatus.NOT_FOUND);
		      }
			return new ResponseEntity<Iterable<JobTypeEntity>>(jobTypes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/deleted")
	public ResponseEntity<?> getAllDeleted(Principal principal) {
		logger.info("################ /jobster/jobtypes/deleted/getAllDeleted started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<JobTypeEntity> jobTypes= jobTypeDao.findJobTypeByStatusLike(0);
			logger.info("---------------- Finished OK.");
			if (Iterables.isEmpty(jobTypes)) {
				logger.info("---------------- Deleted job types not found.");
		        return new ResponseEntity<>("Deleted job types not found.", HttpStatus.NOT_FOUND);
		      }
			return new ResponseEntity<Iterable<JobTypeEntity>>(jobTypes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/archived")
	public ResponseEntity<?> getAllArchived(Principal principal) {
		logger.info("################ /jobster/jobtypes/archived/getAllArchived started.");
		logger.info("Logged username: " + principal.getName());
		try {
			Iterable<JobTypeEntity> jobTypes= jobTypeDao.findJobTypeByStatusLike(-1);
			logger.info("---------------- Finished OK.");
			if (Iterables.isEmpty(jobTypes)) {
				logger.info("---------------- Archived job types not found.");
		        return new ResponseEntity<>("Archived job types not found.", HttpStatus.NOT_FOUND);
		      }
			return new ResponseEntity<Iterable<JobTypeEntity>>(jobTypes, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewJobType(@Valid @RequestBody JobTypeDTO newJobType, Principal principal, BindingResult result) {
		logger.info("################ /jobster/jobtypes/addNewJobType started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (newJobType.equals(null)) {
			logger.info("---------------- New Job type is null.");
	        return new ResponseEntity<>("New newJob type is null.", HttpStatus.BAD_REQUEST);
	      }
		if (newJobType.getJobTypeName() == null) {
			logger.info("---------------- Some or all atributes are null.");
			return new ResponseEntity<>("Some or all atributes are null", HttpStatus.BAD_REQUEST);
		}
		if (newJobType.getJobTypeName().equals(" ") || newJobType.getJobTypeName().equals("") ) {
			logger.info("---------------- Some or all atributes are blanks.");
			return new ResponseEntity<>("Some or all atributes are blanks", HttpStatus.BAD_REQUEST);
		}
		JobTypeEntity jobType = new JobTypeEntity();
		try {
			if (newJobType.getJobTypeName() != null && jobTypeRepository.getByJobTypeName(newJobType.getJobTypeName()) != null) {
				logger.info("---------------- Job type name already exists.");
		        return new ResponseEntity<>("Job type name already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			jobType = jobTypeDao.addNewJobType(loggedUser, newJobType);
			logger.info("Job type created.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(jobType, HttpStatus.OK);
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
	@RequestMapping(method = RequestMethod.GET, value = "/upload") 
	public ModelAndView index() {
		logger.info("################ /jobster/jobtypes/index started.");
        ModelAndView mav = new ModelAndView("upload");
        mav.addObject("hoa_version", "HOA v0.1");
        return mav;
    }
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/uploadStatus") 
	public ModelAndView uploadStatus() {
		logger.info("################ /jobster/jobtypes/uploadStatus started.");
        ModelAndView mav = new ModelAndView("uploadStatus");
        mav.addObject("hoa_version", "HOA v0.1");
        return mav;
	}
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/upload", consumes = "text/csv")
    public ResponseEntity<?> uploadSimple(@RequestBody InputStream body, Principal principal) {
		logger.info("################ /jobster/jobtypes/uploadSimple started.");
		logger.info("Logged user: " + principal.getName());
		try {
			jobTypeDao.saveAll(CsvUtils.read(JobTypeEntity.class, body));
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>("Upload successful", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadMultipart(@RequestParam("file") MultipartFile file, Principal principal) {
		logger.info("################ /jobster/jobtypes/uploadMultipart started.");
		logger.info("Logged user: " + principal.getName());
    	try {
    		jobTypeDao.saveAll(CsvUtils.read(JobTypeEntity.class, file.getInputStream()));
    		logger.info("---------------- Finished OK.");
			return new ResponseEntity<>("Upload successful", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("++++++++++++++++ This is an exception message: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	@Secured("ROLE_ADMIN")
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> modifyJobType(@PathVariable Integer id, @Valid @RequestBody JobTypeDTO updateJobType, Principal principal, BindingResult result) {
		logger.info("################ /jobster/jobtypes/{id}/modifyJobType started.");
		logger.info("Logged user: " + principal.getName());
		if (result.hasErrors()) { 
			logger.info("---------------- Validation has errors - " + createErrorMessage(result));
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST); 
			}
		if (updateJobType == null) {
			logger.info("---------------- Update job type data is null.");
	        return new ResponseEntity<>("Update job type data is null.", HttpStatus.BAD_REQUEST);
	      }
		if (updateJobType.getJobTypeName() == null ) {
			logger.info("---------------- All atributes are null.");
			return new ResponseEntity<>("All atributes are null", HttpStatus.BAD_REQUEST);
		}
		if (updateJobType.getJobTypeName() != null && (updateJobType.getJobTypeName().equals(" ") || updateJobType.getJobTypeName().equals("") ) ) {
			logger.info("---------------- Some or all atributes are blanks.");
			return new ResponseEntity<>("Some or all atributes are blanks", HttpStatus.BAD_REQUEST);
		}
		JobTypeEntity jobType = new JobTypeEntity();
		try {
			if (updateJobType.getJobTypeName() != null && jobTypeRepository.getByJobTypeName(updateJobType.getJobTypeName()) != null) {
				logger.info("---------------- Job type name already exists.");
		        return new ResponseEntity<>("Job type name already exists.", HttpStatus.NOT_ACCEPTABLE);
			}
			jobType = jobTypeRepository.findByIdAndStatusLike(id, 1);
			if (jobType == null) {
				logger.info("---------------- Job type not found.");
		        return new ResponseEntity<>("Job type not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Job type identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			if (updateJobType.getJobTypeName() != null ) {
				jobTypeDao.modifyJobType(loggedUser, jobType, updateJobType);
				logger.info("Job type modified.");
			}
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<>(jobType, HttpStatus.OK);
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
		logger.info("################ /jobster/jobtypes/archive/archive started.");
		logger.info("Logged user: " + principal.getName());
		JobTypeEntity jobType = new JobTypeEntity();
		try {
			jobType = jobTypeRepository.getById(id);
			if (jobType == null || jobType.getStatus() == -1) {
				logger.info("---------------- Job type not found.");
		        return new ResponseEntity<>("Job type not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Job type for archiving identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			jobTypeDao.archiveJobType(loggedUser, jobType);
			logger.info("Job type archived.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<JobTypeEntity>(jobType, HttpStatus.OK);
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
		logger.info("################ /jobster/jobtypes/undelete/{id}/unDelete started.");
		logger.info("Logged user: " + principal.getName());
		JobTypeEntity jobType = new JobTypeEntity();
		try {
			jobType = jobTypeRepository.findByIdAndStatusLike(id, 0);
			if (jobType == null) {
				logger.info("---------------- Job type not found.");
		        return new ResponseEntity<>("Job type not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Job type for undeleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			jobTypeDao.undeleteJobType(loggedUser, jobType);
			logger.info("Job type undeleted.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<JobTypeEntity>(jobType, HttpStatus.OK);
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
		logger.info("################ /jobster/jobtypes/{id}/delete started.");
		logger.info("Logged user: " + principal.getName());
		JobTypeEntity jobType = new JobTypeEntity();
		try {
			jobType = jobTypeRepository.findByIdAndStatusLike(id, 1);
			if (jobType == null) {
				logger.info("---------------- Job type not found.");
		        return new ResponseEntity<>("Job type not found.", HttpStatus.NOT_FOUND);
		      }
			logger.info("Job type for deleting identified.");
			UserEntity loggedUser = userAccountRepository.findUserByUsernameAndStatusLike(principal.getName(), 1);
			logger.info("Logged user identified.");
			jobTypeDao.deleteJobType(loggedUser, jobType);
			logger.info("Job type deleted.");
			logger.info("---------------- Finished OK.");
			return new ResponseEntity<JobTypeEntity>(jobType, HttpStatus.OK);
		} catch (NumberFormatException e) {
			logger.error("++++++++++++++++ Number format exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(2, "Number format exception occurred: "+ e.getLocalizedMessage()), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
}