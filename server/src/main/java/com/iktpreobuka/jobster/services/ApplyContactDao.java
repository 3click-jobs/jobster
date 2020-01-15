package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;

public interface ApplyContactDao {
	public Integer otherPartyFromApply(Integer idIn,ApplyContactEntity apply);


	Iterable<ApplyContactEntity> findByQueryForLoggedInUser(Integer loggedInUserId, Boolean rejected, Boolean connected,
			Boolean expired,Boolean commentable);


	void markApplyAsExpiredBySeek(JobSeekEntity seek);


	void markApplyAsExpiredByOffer(JobOfferEntity offer);


	Iterable<ApplyContactEntity> findByQuery(Boolean rejected, Boolean connected, Boolean expired, Boolean commentable);
}
