package com.iktpreobuka.jobster.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.RejectOfferEntity;
import com.iktpreobuka.jobster.entities.UserEntity;

@Repository
public interface RejectOfferRepository extends PagingAndSortingRepository<RejectOfferEntity, Integer> {

	RejectOfferEntity findByIdAndStatusLike(Integer id, Integer i);

	Iterable<RejectOfferEntity> findByStatusLike(int i);

	Iterable<RejectOfferEntity> findByRejectedOfferAndStatusLike(JobOfferEntity rejectedSeek, int i);

	Iterable<RejectOfferEntity> findByRejectedOffer(JobOfferEntity rejectedSeek);

	Iterable<RejectOfferEntity> findByUserAndStatusLike(UserEntity user, int i);
	
	Iterable<RejectOfferEntity> findByUser(UserEntity user);
	
	//pagination:
	public Page<RejectOfferEntity> findByStatusLike(int i, Pageable pageable);
	Page<RejectOfferEntity> findByRejectedOfferAndStatusLike(JobOfferEntity rejectedSeek, int i, Pageable pageable);
	Page<RejectOfferEntity> findByRejectedOfferAndStatusLike(JobOfferEntity rejectedSeek, Pageable pageable);
	Page<RejectOfferEntity> findByUserAndStatusLike(UserEntity user, int i, Pageable pageable);
	Page<RejectOfferEntity> findByUserAndStatusLike(UserEntity user, Pageable pageable);


}
