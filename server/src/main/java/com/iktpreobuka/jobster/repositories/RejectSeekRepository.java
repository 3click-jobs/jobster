package com.iktpreobuka.jobster.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.RejectSeekEntity;
import com.iktpreobuka.jobster.entities.UserEntity;

@Repository
public interface RejectSeekRepository extends PagingAndSortingRepository<RejectSeekEntity, Integer> {

	RejectSeekEntity findByIdAndStatusLike(Integer id, Integer i);

	Iterable<RejectSeekEntity> findByStatusLike(int i);

	Iterable<RejectSeekEntity> findByRejectedSeekAndStatusLike(JobSeekEntity rejectedSeek, int i);

	Iterable<RejectSeekEntity> findByRejectedSeek(JobSeekEntity rejectedSeek);

	Iterable<RejectSeekEntity> findByUserAndStatusLike(UserEntity user, int i);
	
	Iterable<RejectSeekEntity> findByUser(UserEntity user);
	
	//pagination:
	public Page<RejectSeekEntity> findByStatusLike(int i, Pageable pageable);
	Page<RejectSeekEntity> findByRejectedSeekAndStatusLike(JobSeekEntity rejectedSeek, int i, Pageable pageable);
	Page<RejectSeekEntity> findByRejectedSeekAndStatusLike(JobSeekEntity rejectedSeek, Pageable pageable);
	Page<RejectSeekEntity> findByUserAndStatusLike(UserEntity user, int i, Pageable pageable);
	Page<RejectSeekEntity> findByUserAndStatusLike(UserEntity user, Pageable pageable);

	
}
