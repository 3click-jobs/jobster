package com.iktpreobuka.jobster.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.services.JobOfferDao;

@RestController
@RequestMapping(value="/offer")
public class JobOfferController {

	@Autowired
	public JobOfferRepository jobOfferRepository;
	
	@Autowired
	public JobOfferDao jobOfferService;
	
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
