package com.iktpreobuka.jobster.services;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.iktpreobuka.jobster.entities.dto.JobSeekPostDto;
import com.iktpreobuka.jobster.entities.dto.JobSeekPutDto;

public interface JobSeekDao {
	
	public JobSeekPostDto empty();
	
	public JobSeekPostDto emptyWithEmptyDayHours();
	
	public ResponseEntity<?> addNewSeek(@Valid @RequestBody JobSeekPostDto seek, Principal principal, BindingResult result);

	public ResponseEntity<?> modifySeek(@Valid @RequestBody JobSeekPutDto seek, @PathVariable Integer seekId, Principal principal,
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

	





}
