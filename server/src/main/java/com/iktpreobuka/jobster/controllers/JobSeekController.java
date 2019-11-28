package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.entities.dto.JobSeekPostDto;
import com.iktpreobuka.jobster.entities.dto.JobSeekPutDto;
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
	public JobSeekPostDto empty() {
		return jobService.empty();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/emptyWith")
	public JobSeekPostDto emptyWithEmptyDayHours() {
		return jobService.emptyWithEmptyDayHours();
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/newseek")
	public ResponseEntity<?> addNewSeek(@Valid @RequestBody JobSeekPostDto seek, Principal principal, BindingResult result){
		return jobService.addNewSeek(seek, principal, result);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/modifySeek/{seekId}")
	public ResponseEntity<?> modifySeek(@Valid @RequestBody JobSeekPutDto seek, @PathVariable Integer seekId, Principal principal, BindingResult result){
		return jobService.modifySeek(seek, seekId, principal, result);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return jobService.getAll();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id){
		return jobService.getById(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/employee/{id}")
	public ResponseEntity<?> getAllLikeEmployee(@PathVariable Integer id){
		return jobService.getAllLikeEmployee(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/city/{id}")
	public ResponseEntity<?> getAllLikeCity(@PathVariable Integer id){
		return jobService.getAllLikeCity(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/jobType/{id}")
	public ResponseEntity<?> getAllLikeJobType(@PathVariable Integer id){
		return jobService.getAllLikeJobType(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/distance")
	public ResponseEntity<?> getAllJobSeekWhereDistanceIsAndLessThen(@RequestParam Integer distance){
		return jobService.getAllJobSeekWhereDistanceIsAndLessThen(distance);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/beginDate")
	public ResponseEntity<?> getAllWithBeginnigDate(@RequestParam Date beginDate){
		return jobService.getAllWithBeginnigDate(beginDate);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/endDate")
	public ResponseEntity<?> getAllWithEndDate(@RequestParam Date endDate){
		return jobService.getAllWithEndDate(endDate);
	}
	
			
	@RequestMapping(method = RequestMethod.GET, value = "/flexDates")
	public ResponseEntity<?> getAllWithFlexibileDates(@RequestParam boolean flexDates){
		return jobService.getAllWithFlexibileDates(flexDates);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/price")
	public ResponseEntity<?> getAllWherePriceIsAndMore(@RequestParam Double price){
		return jobService.getAllWherePriceIsAndMore(price);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/flexDays")
	public ResponseEntity<?> getAllWithFlexibileDays(@RequestParam boolean flexDays){
		return jobService.getAllWithFlexibileDays(flexDays);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/status")
	public ResponseEntity<?> getAllWithStatus(@RequestParam Integer status){
		return jobService.getAllWithStatus(status);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/elapse")
	public ResponseEntity<?> getAllWithElapse(@RequestParam Integer elapse){
		return jobService.getAllWithElapse(elapse);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/createdBy")
	public ResponseEntity<?> getAllByCreatedBy(@RequestParam Integer createdBy){
		return jobService.getAllByCreatedBy(createdBy);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/updatedBy")
	public ResponseEntity<?> getAllByUpdatedBy(@RequestParam Integer updatedBy){
		return jobService.getAllByUpdatedBy(updatedBy);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/version")
	public ResponseEntity<?> getAllByVersion(@RequestParam Integer version){
		return jobService. getAllByVersion(version);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete")
	public ResponseEntity<?> deleteById(@PathVariable Integer Id){
		return jobService.deleteById(Id);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/unDelete")
	public ResponseEntity<?> unDeleteById(@PathVariable Integer Id){
		return jobService.unDeleteById(Id);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/archive")
	public ResponseEntity<?> archiveById(@PathVariable Integer Id){
		return jobService.archiveById(Id);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/unArchive")
	public ResponseEntity<?> unArchiveById(@PathVariable Integer Id){
		return jobService.unArchiveById(Id);
	}

}
