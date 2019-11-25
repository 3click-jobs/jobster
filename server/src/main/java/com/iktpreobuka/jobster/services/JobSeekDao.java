package com.iktpreobuka.jobster.services;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import com.iktpreobuka.jobster.entities.dto.JobSeekDto;

public interface JobSeekDao {
	
	public JobSeekDto empty();
	
	public JobSeekDto emptyWithEmptyDayHours();
	
	public ResponseEntity<?> addNewSeek(@Valid JobSeekDto seek, Principal principal, BindingResult result);

	public ResponseEntity<?> deleteById(Integer id);

	public ResponseEntity<?> archiveById(Integer id);

	public ResponseEntity<?> getById(Integer id);

	public ResponseEntity<?> unDeleteById(Integer id);

	public ResponseEntity<?> unArchiveById(Integer id);



	//public ResponseEntity<?> modifySeekById(@Valid @RequestBody NekiDto job, BindingResult result, @PathVariable Integer id);//put
		

	//public ResponseEntity<?> deleteById(@PathVariable Integer id);//delete
	
	//public ResponseEntity<?> restorById(@PathVariable Integer id);//delete
	
	//public ResponseEntity<?> archiveById(@PathVariable Integer id);//delete
	
	//public ResponseEntity<?> unArchiveById(@PathVariable Integer id);//delete
	
	
	//public ResponseEntity<?>  getById(@PathVariable Integer id);//get



	

}
