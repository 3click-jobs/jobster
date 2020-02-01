package com.iktpreobuka.jobster.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

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

//pagination:
	public Page<ApplyContactEntity> findAll(int page, int pageSize, Direction direction, String sortBy);
	public Page<ApplyContactEntity> findByQueryForLoggedInUser(Integer loggedInUserId, Boolean rejected,
			Boolean connected, Boolean expired, Boolean commentable, Pageable pageable);


}
