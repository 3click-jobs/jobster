package com.iktpreobuka.jobster.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobOfferDTO;
import com.iktpreobuka.jobster.entities.dto.JobOfferPutDTO;
import com.iktpreobuka.jobster.entities.dto.JobOfferSearchDTO;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.services.JobOfferDao;

@Controller
@RestController
@RequestMapping(value="/jobster/offer")
public class JobOfferController {

	@Autowired
	public JobOfferRepository jobOfferRepository;
	
	@Autowired
	public JobOfferDao jobOfferService;
	
	@Autowired
	public UserAccountRepository userAccountRepository;
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@RequestMapping(method = RequestMethod.POST, value = "/newoffer")
	public ResponseEntity<?> addNewOffer(@Valid @RequestBody JobOfferDTO offer, Principal principal,
			BindingResult result) {
		return jobOfferService.addNewOffer(offer, principal, result);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/modifyOffer/{offerId}/staraMetoda")
	public ResponseEntity<?> modifyOfferStaraMetoda(@Valid @RequestBody JobOfferDTO offer, @PathVariable Integer offerId,
			Principal principal, BindingResult result) {
		return jobOfferService.modifyOfferStaraMetoda(offer, offerId, principal, result);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/modifyOffer/{offerId}")
	public ResponseEntity<?> modifyOffer(@Valid @RequestBody JobOfferPutDTO offer, @PathVariable Integer offerId,
			Principal principal, BindingResult result) {
		return jobOfferService.modifyOffer(offer, offerId, principal, result);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public ResponseEntity<?> findByQuery(@Valid @RequestBody(required = false) JobOfferSearchDTO jobOfferSearchDTO, Principal principal) {
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			logger.info("---------------- Logged user found");
			return jobOfferService.findByQuery(loggedUser, jobOfferSearchDTO.getJobDayHours(), jobOfferSearchDTO.getEmployerId(), 
					jobOfferSearchDTO.getCityName(), jobOfferSearchDTO.getCountryRegionName(), jobOfferSearchDTO.getCountryName(), 
					jobOfferSearchDTO.getDistance(), jobOfferSearchDTO.getTypeId(), jobOfferSearchDTO.getBeginningDate(), 
					jobOfferSearchDTO.getEndDate(), jobOfferSearchDTO.getFlexibileDates(), jobOfferSearchDTO.getPrice(), 
					jobOfferSearchDTO.getFlexibileDays());
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/emptyJobOfferEntity")
	public JobOfferEntity emptyJobOfferEntity() {
		return jobOfferService.emptyJobOfferEntity();
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		return jobOfferService.deleteById(id);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/unDelete/{id}")
	public ResponseEntity<?> unDeleteById(@PathVariable Integer id){
		return jobOfferService.unDeleteById(id);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/archive/{id}")
	public ResponseEntity<?> archiveById(@PathVariable Integer id){
		return jobOfferService.archiveById(id);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/unArchive/{id}")
	public ResponseEntity<?> unArchiveById(@PathVariable Integer id){
		return jobOfferService.unArchiveById(id);
	}
}
