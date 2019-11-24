package com.iktpreobuka.jobster.controllers;

import java.security.Principal;

import javax.validation.Valid;

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
	public ResponseEntity<?> addNewSeek(@Valid @RequestBody JobSeekDto seek, Principal principal, BindingResult result){
		return jobService.addNewSeek(seek, principal, result);
	}

}
