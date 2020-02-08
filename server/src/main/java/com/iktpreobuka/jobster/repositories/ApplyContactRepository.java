package com.iktpreobuka.jobster.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;


@Repository
public interface ApplyContactRepository extends PagingAndSortingRepository<ApplyContactEntity, Integer>{

	
	ApplyContactEntity findByIdAndStatusLike(Integer id, Integer i);

	Iterable<ApplyContactEntity> findByStatusLike(int i);

	Iterable<ApplyContactEntity> findByOfferAndStatusLike(JobOfferEntity offer, int i);

	Iterable<ApplyContactEntity> findByOffer(JobOfferEntity offer);

	Iterable<ApplyContactEntity> findBySeekAndStatusLike(JobSeekEntity seek, int i);
	
	Iterable<ApplyContactEntity> findBySeek(JobSeekEntity seek);
	
	//pagination:
	public Page<ApplyContactEntity> findByStatusLike(int i, Pageable pageable);
	Page<ApplyContactEntity> findByOfferAndStatusLike(JobOfferEntity offer, int i, Pageable pageable);
	Page<ApplyContactEntity> findByOfferAndStatusLike(JobOfferEntity offer, Pageable pageable);
	Page<ApplyContactEntity> findBySeekAndStatusLike(JobSeekEntity seek, int i, Pageable pageable);
	Page<ApplyContactEntity> findBySeekAndStatusLike(JobSeekEntity seek, Pageable pageable);


}
