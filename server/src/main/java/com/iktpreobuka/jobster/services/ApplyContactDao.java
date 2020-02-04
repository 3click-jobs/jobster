package com.iktpreobuka.jobster.services;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;

public interface ApplyContactDao {
	public Integer otherPartyFromApply(Integer idIn,ApplyContactEntity apply);


	Iterable<ApplyContactEntity> findByQueryAndUser(Integer loggedInUserId,Integer status, Boolean rejected, Boolean connected,
			Boolean expired, Boolean commentable, String connectionDateBottom, String connectionDateTop,
			String contactDateBottom, String contactDateTop);


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
	
	public PagedListHolder<ApplyContactEntity> findByQueryAndUser(Integer loggedInUserId,Integer status, Boolean rejected, Boolean connected,
			Boolean expired, Boolean commentable, String connectionDateBottom, String connectionDateTop,
			String contactDateBottom, String contactDateTop, Pageable pageable);



	public PagedListHolder<ApplyContactEntity> findByQuery(Integer status, Boolean rejected, Boolean connected,
			Boolean expired, Boolean commentable, String connectionDateBottom, String connectionDateTop,
			String contactDateBottom, String contactDateTop, Pageable pageable);


	void deleteApplication(Integer loggedUserId, ApplyContactEntity application) throws Exception;


	void undeleteApplication(Integer loggedUserId, ApplyContactEntity application) throws Exception;


	void archiveApplication(Integer loggedUserId, ApplyContactEntity application) throws Exception;


}
