package com.iktpreobuka.jobster.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;


@Repository
public interface ApplyContactRepository extends CrudRepository<ApplyContactEntity, Integer>{

	
	ApplyContactEntity findByIdAndStatusLike(Integer id, Integer i);

	Iterable<ApplyContactEntity> findByStatusLike(int i);

	Iterable<ApplyContactEntity> findByOfferAndStatusLike(JobOfferEntity offer, int i);

	Iterable<ApplyContactEntity> findByOffer(JobOfferEntity offer);
}
