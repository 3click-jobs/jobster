package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.JobTypeEntity;

public interface JobTypeRepository extends CrudRepository<JobTypeEntity, Integer>{

	public JobTypeEntity getByJobTypeName(String typeName);
	
	public Iterable<JobTypeEntity> findByStatusLike(Integer status);

	public JobTypeEntity findByIdAndStatusLike(Integer id, Integer status);

	public JobTypeEntity getById(Integer id);

	public JobTypeEntity getByJobTypeNameAndStatusLike(String jobTypeName, Integer status);
	
}
