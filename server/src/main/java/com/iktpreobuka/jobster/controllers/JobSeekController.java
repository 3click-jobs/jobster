package com.iktpreobuka.jobster.controllers;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.jobster.controllers.util.RESTError;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobDayHoursDTO;
import com.iktpreobuka.jobster.entities.dto.JobSeekDTO;
import com.iktpreobuka.jobster.entities.dto.JobSeekPutDTO;
import com.iktpreobuka.jobster.entities.dto.JobSeekSearchDTO;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.services.JobSeekDao;

@Controller
@RestController
@RequestMapping(value = "/jobster/seek")
public class JobSeekController {

	@Autowired
	public JobSeekRepository jobSeekRepository;

	@Autowired
	public JobSeekDao jobSeekService;
	
	@Autowired
	public UserAccountRepository userAccountRepository;

//	@Autowired
//	private UserCustomValidator userValidator;
//
//	@InitBinder
//	protected void initBinder(final WebDataBinder binder) {
//		binder.addValidators(userValidator);
//	}

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());


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
	
	@RequestMapping(method = RequestMethod.GET, value = "/emptySearchWith")
	public JobSeekSearchDTO emptyJobSeekSearchDTOWithEmptyDayHours() {
		JobSeekSearchDTO emptyWith = new JobSeekSearchDTO();
		JobDayHoursDTO empty1 = new JobDayHoursDTO();
		JobDayHoursDTO empty2 = new JobDayHoursDTO();
		List<JobDayHoursDTO> list = new ArrayList<JobDayHoursDTO>();
		list.add(empty1);
		list.add(empty2);
		emptyWith.setJobDayHours(list);
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

	@RequestMapping(method = RequestMethod.PUT, value = "/modifySeek/{seekId}/staraMetoda")
	public ResponseEntity<?> modifySeekStaraMetoda(@Valid @RequestBody JobSeekDTO seek, @PathVariable Integer seekId,
			Principal principal, BindingResult result) {
		return jobSeekService.modifySeekStaraMetoda(seek, seekId, principal, result);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/modifySeek/{seekId}")
	public ResponseEntity<?> modifySeek(@Valid @RequestBody JobSeekPutDTO seek, @PathVariable Integer seekId,
			Principal principal, BindingResult result) {
		return jobSeekService.modifySeek(seek, seekId, principal, result);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public ResponseEntity<?> findByQuery(@Valid @RequestBody(required = false) JobSeekSearchDTO jobSeekSearch, Principal principal) {
		logger.info("Logged username: " + principal.getName());
		try {
			UserEntity loggedUser = userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser();
			logger.info("---------------- Logged user found");
			return jobSeekService.findByQuery(loggedUser, jobSeekSearch.getJobDayHours(), jobSeekSearch.getEmployeeId(), jobSeekSearch.getCityName(), jobSeekSearch.getCountryRegionName(), jobSeekSearch.getCountryName(), jobSeekSearch.getTypeId(), jobSeekSearch.getBeginningDate(), jobSeekSearch.getEndDate(), jobSeekSearch.getFlexibileDates(), jobSeekSearch.getPrice(), jobSeekSearch.getFlexibileDays());
		} catch (Exception e) {
			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
//	@RequestMapping(method = RequestMethod.GET, value = "/search")
//	public ResponseEntity<?> findByQuery(/*@RequestParam(required = false) Boolean flexibileHours,
//			@RequestParam(required = false) Integer fromHour, @RequestParam(required = false) Integer toHour, 
//			@RequestParam(required = false) Boolean IsMinMax, */
//			@Valid @RequestBody(required = false) List<JobDayHoursDTO> jobDayHours, @RequestParam(required = false) Integer employeeId, 
//			@RequestParam(required = false) String cityName, @RequestParam(required = false) String countryRegionName, 
//			@RequestParam(required = false) String countryName, @RequestParam(required = false) Integer typeId, 
//			@RequestParam(required = false) Date beginningDate, @RequestParam(required = false) Date endDate, 
//			@RequestParam(required = false) Boolean flexibileDates, @RequestParam(required = false) Double price, 
//			@RequestParam(required = false) Boolean flexibileDays, Principal principal) {
//		logger.info("Logged username: " + principal.getName());
//		try {
//			/*return jobSeekService.findByQuery(flexibileHours, fromHour, toHour, IsMinMax, employeeId, cityName, countryRegionName, countryName, typeId, beginningDate, endDate, flexibileDates, price, flexibileDays);*/
//			return jobSeekService.findByQuery(/*SeekSearhDTO.jobDayHours, SeekSearchDTO.employeeID...*/ jobDayHours, employeeId, cityName, countryRegionName, countryName, typeId, beginningDate, endDate, flexibileDates, price, flexibileDays);
//		} catch (Exception e) {
//			logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
//			return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

			
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
	public ResponseEntity<?> getAllWithElapse(@RequestParam Boolean expired) {
		return jobSeekService.getAllWithExpired(expired);
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
	
//pagination:
	
	
	//@Secured("ROLE_ADMIN")
	//@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.GET, value = "/AllPaginated")
	public ResponseEntity<?> getAllPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy) {
			logger.info("################ /jobster/seek/getAllPaginated started.");
			//logger.info("Logged username: " + principal.getName());
			try {
				logger.info("Checking database for JobSeek.");
				if (((jobSeekRepository.count() == 0))) {
					logger.info("Database empty.");
					return new ResponseEntity<String>("Database empty.", HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				logger.info("Error occured during 'Checking database'.");
				return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
			}
			try {
				Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
				Page<JobSeekEntity> jobSeekEntityPage= jobSeekService.getAll(pageable);
				Iterable<JobSeekEntity> jobSeeks = jobSeekEntityPage.getContent();
				logger.info("---------------- Finished OK.");
				return new ResponseEntity<Iterable<JobSeekEntity>>(jobSeeks, HttpStatus.OK);
				} catch(Exception e) {
					logger.error("++++++++++++++++ Exception occurred: " + e.getMessage());
					return new ResponseEntity<RESTError>(new RESTError(1, "Exception occurred: "+ e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
				}
	}



	@RequestMapping(method = RequestMethod.GET, value = "/employeePaginated/{id}")
	public ResponseEntity<?> getAllLikeEmployeePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllLikeEmployee(id, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/cityPaginated/{id}")
	public ResponseEntity<?> getAllLikeCityPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllLikeCity(id, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/jobTypePaginated/{id}")
	public ResponseEntity<?> getAllLikeJobType(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@PathVariable Integer id) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllLikeJobType(id, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/distancePaginated")
	public ResponseEntity<?> getAllJobSeekWhereDistanceIsAndLessThenPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam Integer distance) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllJobSeekWhereDistanceIsAndLessThen(distance, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/beginDatePaginated")
	public ResponseEntity<?> getAllWithBeginnigDatePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam Date beginDate) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllWithBeginnigDate(beginDate, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/endDatePaginated")
	public ResponseEntity<?> getAllWithEndDatePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam Date endDate) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllWithEndDate(endDate, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/flexDatesPaginated")
	public ResponseEntity<?> getAllWithFlexibileDatesPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam boolean flexDates) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllWithFlexibileDates(flexDates, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/pricePaginated")
	public ResponseEntity<?> getAllWherePriceIsAndMorePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam Double price) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllWherePriceIsAndMore(price, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/flexDaysPaginated")
	public ResponseEntity<?> getAllWithFlexibileDaysPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam boolean flexDays) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllWithFlexibileDays(flexDays, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/statusPaginated")
	public ResponseEntity<?> getAllWithStatusPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam Integer status) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllWithStatus(status, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/expiredPaginated")
	public ResponseEntity<?> getAllWithElapsePaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam Boolean expired) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllWithExpired(expired, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/createdByPaginated")
	public ResponseEntity<?> getAllByCreatedByPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam Integer createdBy) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllByCreatedBy(createdBy, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/updatedByPaginated")
	public ResponseEntity<?> getAllByUpdatedByPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam Integer updatedBy) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllByUpdatedBy(updatedBy, pageable);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/versionPaginated")
	public ResponseEntity<?> getAllByVersionPaginated(
			@RequestParam Optional<Integer> page,
			@RequestParam Optional<Integer> pageSize,
			@RequestParam Optional<Sort.Direction> direction, 
			@RequestParam Optional<String> sortBy,
			@RequestParam Integer version) {
		Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5), direction.orElse(Sort.Direction.ASC), sortBy.orElse("id"));
		return jobSeekService.getAllByVersion(version, pageable);
	}


}
