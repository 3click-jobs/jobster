package com.iktpreobuka.jobster.services;

import java.security.Principal;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.iktpreobuka.jobster.entities.dto.JobSeekDTO;



public interface JobSeekDao {
	
	public ResponseEntity<?> addNewSeek(@Valid @RequestBody JobSeekDTO seek, Principal principal, BindingResult result);

	public ResponseEntity<?> modifySeek(@Valid @RequestBody JobSeekDTO seek, @PathVariable Integer seekId, Principal principal,
			BindingResult result);
	
	public ResponseEntity<?> deleteById(@PathVariable Integer id);

	public ResponseEntity<?> archiveById(@PathVariable Integer id);

	public ResponseEntity<?> getById(@PathVariable Integer id);

	public ResponseEntity<?> unDeleteById(@PathVariable Integer id);

	public ResponseEntity<?> unArchiveById(@PathVariable Integer id);

	public ResponseEntity<?> getAll();
	
	public ResponseEntity<?> getAllLikeEmployee(@PathVariable Integer id);
	
	public ResponseEntity<?> getAllLikeCity(@PathVariable Integer id);

	public ResponseEntity<?> getAllLikeJobType(@PathVariable Integer id);

	public ResponseEntity<?> getAllJobSeekWhereDistanceIsAndLessThen(@RequestParam Integer distance);

	public ResponseEntity<?> getAllWithBeginnigDate(@RequestParam Date beginDate);

	public ResponseEntity<?> getAllWithEndDate(@RequestParam Date endDate);

	public ResponseEntity<?> getAllWithFlexibileDates(@RequestParam boolean flexDates);

	public ResponseEntity<?> getAllWherePriceIsAndMore(@RequestParam Double price);

	public ResponseEntity<?> getAllWithFlexibileDays(@RequestParam boolean flexDays);

	public ResponseEntity<?> getAllWithStatus(@RequestParam Integer status);

	public ResponseEntity<?> getAllWithElapse(@RequestParam Integer elapse);

	public ResponseEntity<?> getAllByCreatedBy(@RequestParam Integer createdBy);

	public ResponseEntity<?> getAllByUpdatedBy(@RequestParam Integer updatedBy);

	public ResponseEntity<?> getAllByVersion(@RequestParam Integer version);

	

	





}
