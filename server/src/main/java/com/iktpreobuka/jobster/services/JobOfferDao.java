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
import com.iktpreobuka.jobster.entities.dto.JobDayHoursDTO;
import com.iktpreobuka.jobster.entities.dto.JobOfferDTO;

public interface JobOfferDao {
	
	public JobOfferEntity emptyJobOfferEntity();
	
	public ResponseEntity<?> addNewOffer(@Valid @RequestBody JobOfferDTO offer, Principal principal, BindingResult result);
	
	public ResponseEntity<?> modifyOffer(@Valid @RequestBody JobOfferDTO offer, @PathVariable Integer offerId, Principal principal, BindingResult result);
	
	public ResponseEntity<?> getAll();
	
	public ResponseEntity<?> getById(@PathVariable Integer id);
	
	public ResponseEntity<?> deleteById(@PathVariable Integer id);

	public ResponseEntity<?> archiveById(@PathVariable Integer id);

	public ResponseEntity<?> unDeleteById(@PathVariable Integer id);

	public ResponseEntity<?> unArchiveById(@PathVariable Integer id);
	
	public ResponseEntity<?> findByQuery(List<JobDayHoursDTO> jobDayHours, Integer employerId, 
			String cityName, String countryRegionName, String countryName, Integer distance,
			Integer typeId, Date beginningDate, Date endDate, Boolean flexibileDates, Double price, 
			Boolean flexibileDays) throws Exception;

}
