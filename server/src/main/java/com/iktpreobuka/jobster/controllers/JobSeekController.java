package com.iktpreobuka.jobster.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.entities.dto.JobSeekDto;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.services.JobSeekDao;

@RestController
@RequestMapping(value="/seek")
public class JobSeekController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public JobSeekRepository jobSeekRepository;
	
	@Autowired
	public JobSeekDao jobService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/empty")
	public JobSeekDto empty() {
		return jobService.empty();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/emptyWith")
	public JobSeekDto emptyWithEmptyDayHours() {
		return jobService.emptyWithEmptyDayHours();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/newseek")
	public ResponseEntity<?> addNewSeek(Principal principal, @Valid @RequestBody JobSeekDto seek, BindingResult result){
		return jobService.addNewSeek(principal, seek, result);
	}

}
