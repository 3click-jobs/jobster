package com.iktpreobuka.jobster.services;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.iktpreobuka.jobster.entities.dto.JobSeekDto;

public interface JobSeekDao {
	
	public JobSeekDto empty();
	
	public JobSeekDto emptyWithEmptyDayHours();
	
	public ResponseEntity<?> addNewSeek(Principal principal, @Valid @RequestBody JobSeekDto seek, BindingResult result);//post



	//public ResponseEntity<?> modifySeekById(@Valid @RequestBody NekiDto job, BindingResult result, @PathVariable Integer id);//put
		

	//public ResponseEntity<?> deleteById(@PathVariable Integer id);//delete
	
	//public ResponseEntity<?> restorById(@PathVariable Integer id);//delete
	
	//public ResponseEntity<?> archiveById(@PathVariable Integer id);//delete
	
	//public ResponseEntity<?> unArchiveById(@PathVariable Integer id);//delete
	
	
	//public ResponseEntity<?>  getById(@PathVariable Integer id);//get



	

}
