package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.JobSeekEntity;

public interface JobSeekRepository extends CrudRepository<JobSeekEntity, Integer> {

	public JobSeekEntity getById(Integer id);
	
}