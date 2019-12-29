package com.iktpreobuka.jobster.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.iktpreobuka.jobster.entities.JobOfferEntity;

public interface JobOfferDao {

	public JobOfferEntity emptyJobOfferEntity();
	
	public ResponseEntity<?> getAll();
	
	public ResponseEntity<?> getById(@PathVariable Integer id);
	
	public ResponseEntity<?> deleteById(@PathVariable Integer id);

	public ResponseEntity<?> archiveById(@PathVariable Integer id);

	public ResponseEntity<?> unDeleteById(@PathVariable Integer id);

	public ResponseEntity<?> unArchiveById(@PathVariable Integer id);

}
