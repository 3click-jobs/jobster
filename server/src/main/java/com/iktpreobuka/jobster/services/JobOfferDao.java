package com.iktpreobuka.jobster.services;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobDayHoursDTO;
import com.iktpreobuka.jobster.entities.dto.JobOfferDTO;
import com.iktpreobuka.jobster.entities.dto.JobOfferPutDTO;

public interface JobOfferDao {
	
	public JobOfferEntity emptyJobOfferEntity();
	
	public ResponseEntity<?> addNewOffer(@Valid @RequestBody JobOfferDTO offer, Principal principal, BindingResult result);
	
	public ResponseEntity<?> modifyOfferStaraMetoda(@Valid @RequestBody JobOfferDTO offer, Integer offerId, Principal principal,
			BindingResult result);
	
	public ResponseEntity<?> modifyOffer(@Valid @RequestBody JobOfferPutDTO offer, @PathVariable Integer offerId, Principal principal, BindingResult result);
	
	public ResponseEntity<?> getAll();
	
	public ResponseEntity<?> getById(@PathVariable Integer id);
	
	public ResponseEntity<?> deleteById(@PathVariable Integer id);

	public ResponseEntity<?> archiveById(@PathVariable Integer id);

	public ResponseEntity<?> unDeleteById(@PathVariable Integer id);

	public ResponseEntity<?> unArchiveById(@PathVariable Integer id);
	
	public ResponseEntity<?> findByQuery(UserEntity loggedUser, List<JobDayHoursDTO> jobDayHours, 
			Integer employerId, String cityName, String countryRegionName, String countryName, Integer distance,
			Integer typeId, Date beginningDate, Date endDate, Boolean flexibileDates, Double price, 
			Boolean flexibileDays) throws Exception;

	public ResponseEntity<?> findCounterOffer(UserEntity loggedUser, List<JobDayHoursDTO> jobDayHours, String cityName, String countryRegionName, String countryName, 
			Integer typeId, Integer numberOfEmployees, Date beginningDate, Date endDate, Boolean flexibileDates, Double price, 
			Boolean flexibileDays, String detailsLink) throws Exception;


}
