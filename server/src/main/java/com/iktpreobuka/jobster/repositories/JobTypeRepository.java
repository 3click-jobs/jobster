package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.JobTypeEntity;

public interface JobTypeRepository extends CrudRepository<JobTypeEntity, Integer>{

	public JobTypeEntity getByJobTypeName(String typeName);
	
}
