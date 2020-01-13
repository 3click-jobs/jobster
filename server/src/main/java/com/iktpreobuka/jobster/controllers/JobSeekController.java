package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.controllers.util.UserCustomValidator;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.dto.JobDayHoursDTO;
import com.iktpreobuka.jobster.entities.dto.JobSeekDTO;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.services.JobSeekDao;

@Controller
@RestController
@RequestMapping(value = "/jobster/seek")
public class JobSeekController {

	@Autowired
	public JobSeekRepository jobSeekRepository;

	@Autowired
	public JobSeekDao jobSeekService;

//	@Autowired
//	private UserCustomValidator userValidator;
//
//	@InitBinder
//	protected void initBinder(final WebDataBinder binder) {
//		binder.addValidators(userValidator);
//	}


	// @Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, value = "/emptyJobSeekEntity")
	public JobSeekEntity emptyJobSeekEntity() {
		return new JobSeekEntity();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/emptyJobSeekDto")
	public JobSeekDTO empty() {
		return new JobSeekDTO();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/emptyWith")
	public JobSeekDTO emptyWithEmptyDayHours() {
		JobSeekDTO emptyWith = new JobSeekDTO();
		JobDayHoursDTO empty1 = new JobDayHoursDTO();
		JobDayHoursDTO empty2 = new JobDayHoursDTO();
		List<JobDayHoursDTO> list = new ArrayList<JobDayHoursDTO>();
		list.add(empty1);
		list.add(empty2);
		emptyWith.setListJobDayHoursDto(list);
		return emptyWith;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/emptyPersonDto")
	public PersonDTO emptyPersonDto() {
		return new PersonDTO();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/newseek")
	public ResponseEntity<?> addNewSeek(@Valid @RequestBody JobSeekDTO seek, Principal principal,
			BindingResult result) {
		return jobSeekService.addNewSeek(seek, principal, result);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/modifySeek/{seekId}")
	public ResponseEntity<?> modifySeek(@Valid @RequestBody JobSeekDTO seek, @PathVariable Integer seekId,
			Principal principal, BindingResult result) {
		return jobSeekService.modifySeek(seek, seekId, principal, result);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		return jobSeekService.getAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		return jobSeekService.getById(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/employee/{id}")
	public ResponseEntity<?> getAllLikeEmployee(@PathVariable Integer id) {
		return jobSeekService.getAllLikeEmployee(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/city/{id}")
	public ResponseEntity<?> getAllLikeCity(@PathVariable Integer id) {
		return jobSeekService.getAllLikeCity(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/jobType/{id}")
	public ResponseEntity<?> getAllLikeJobType(@PathVariable Integer id) {
		return jobSeekService.getAllLikeJobType(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/distance")
	public ResponseEntity<?> getAllJobSeekWhereDistanceIsAndLessThen(@RequestParam Integer distance) {
		return jobSeekService.getAllJobSeekWhereDistanceIsAndLessThen(distance);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/beginDate")
	public ResponseEntity<?> getAllWithBeginnigDate(@RequestParam Date beginDate) {
		return jobSeekService.getAllWithBeginnigDate(beginDate);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/endDate")
	public ResponseEntity<?> getAllWithEndDate(@RequestParam Date endDate) {
		return jobSeekService.getAllWithEndDate(endDate);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/flexDates")
	public ResponseEntity<?> getAllWithFlexibileDates(@RequestParam boolean flexDates) {
		return jobSeekService.getAllWithFlexibileDates(flexDates);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/price")
	public ResponseEntity<?> getAllWherePriceIsAndMore(@RequestParam Double price) {
		return jobSeekService.getAllWherePriceIsAndMore(price);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/flexDays")
	public ResponseEntity<?> getAllWithFlexibileDays(@RequestParam boolean flexDays) {
		return jobSeekService.getAllWithFlexibileDays(flexDays);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/status")
	public ResponseEntity<?> getAllWithStatus(@RequestParam Integer status) {
		return jobSeekService.getAllWithStatus(status);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/elapse")
	public ResponseEntity<?> getAllWithElapse(@RequestParam Integer elapse) {
		return jobSeekService.getAllWithElapse(elapse);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/createdBy")
	public ResponseEntity<?> getAllByCreatedBy(@RequestParam Integer createdBy) {
		return jobSeekService.getAllByCreatedBy(createdBy);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/updatedBy")
	public ResponseEntity<?> getAllByUpdatedBy(@RequestParam Integer updatedBy) {
		return jobSeekService.getAllByUpdatedBy(updatedBy);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/version")
	public ResponseEntity<?> getAllByVersion(@RequestParam Integer version) {
		return jobSeekService.getAllByVersion(version);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		return jobSeekService.deleteById(id);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/unDelete/{id}")
	public ResponseEntity<?> unDeleteById(@PathVariable Integer id) {
		return jobSeekService.unDeleteById(id);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/archive/{id}")
	public ResponseEntity<?> archiveById(@PathVariable Integer id) {
		return jobSeekService.archiveById(id);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/unArchive/{id}")
	public ResponseEntity<?> unArchiveById(@PathVariable Integer id) {
		return jobSeekService.unArchiveById(id);
	}

}
