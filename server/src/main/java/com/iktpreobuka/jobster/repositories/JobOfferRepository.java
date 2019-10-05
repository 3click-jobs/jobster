package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.jobster.entities.JobOfferEntity;

public interface JobOfferRepository extends CrudRepository<JobOfferEntity, Integer> {

	public JobOfferEntity getById(Integer id);

	public JobOfferEntity findByIdAndStatusLike(int parseInt, int i);

	public JobOfferEntity findByEmployerAndStatusLike(int parseInt, int i);
	
}
