package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.JobSeekEntity;

@Repository
public interface JobSeekRepository extends CrudRepository<JobSeekEntity, Integer> {

	public JobSeekEntity getById(Integer id);
	
}