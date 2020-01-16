package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;

public interface ApplyContactDao {
	public Integer otherPartyFromApply(Integer idIn,ApplyContactEntity apply);


	Iterable<ApplyContactEntity> findByQueryForLoggedInUser(Integer loggedInUserId, Integer status, Boolean rejected, Boolean connected,
			Boolean expired,Boolean commentable);


	void markApplyAsExpiredBySeek(JobSeekEntity seek);


	void markApplyAsExpiredByOffer(JobOfferEntity offer);


	Iterable<ApplyContactEntity> findByQuery(Integer status, Boolean rejected, Boolean connected, Boolean expired, Boolean commentable);


	public boolean stringDateFormatNotCorrect(String connectionDateBottom, String connectionDateTop,
			String contactDateBottom, String contactDateTop);


	public Iterable<ApplyContactEntity> findByQuery(Integer status, Boolean rejected, Boolean connected,
			Boolean expired, Boolean commentable, String connectionDateBottom, String connectionDateTop,
			String contactDateBottom, String contactDateTop);
}
