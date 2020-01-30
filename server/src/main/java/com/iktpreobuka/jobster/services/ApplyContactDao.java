package com.iktpreobuka.jobster.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;

public interface ApplyContactDao {
	public Integer otherPartyFromApply(Integer idIn,ApplyContactEntity apply);


	Iterable<ApplyContactEntity> findByQueryForLoggedInUser(Integer loggedInUserId, Boolean rejected, Boolean connected,
			Boolean expired,Boolean commentable);


	void markApplyAsExpiredBySeek(JobSeekEntity seek);


	void markApplyAsExpiredByOffer(JobOfferEntity offer);

//pagination:
	public Page<ApplyContactEntity> findAll(int page, int pageSize, Direction direction, String sortBy);

	public Page<ApplyContactEntity> findByQueryForLoggedInUser(Integer loggedInUserId, Boolean rejected,
			Boolean connected, Boolean expired, Boolean commentable, Pageable pageable);

}
