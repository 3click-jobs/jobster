package com.iktpreobuka.jobster.services;

import java.util.List;

import javax.validation.Valid;

import com.iktpreobuka.jobster.entities.JobTypeEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobTypeDTO;

public interface JobTypeDao {

	public Iterable<JobTypeEntity> findJobTypeByStatusLike(Integer status) throws Exception;

	public JobTypeEntity addNewJobType(UserEntity loggedUser, JobTypeDTO newJobType) throws Exception;
	
	public void modifyJobType(UserEntity loggedUser, JobTypeEntity jobType, JobTypeDTO updateJobType) throws Exception;

	public void deleteJobType(UserEntity loggedUser, JobTypeEntity jobType) throws Exception;

	public void undeleteJobType(UserEntity loggedUser, JobTypeEntity jobType) throws Exception;

	public void archiveJobType(UserEntity loggedUser, JobTypeEntity jobType) throws Exception;
	
	public void saveAll(@Valid List<JobTypeEntity> jobTypes) throws Exception;

}
