package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.RejectOfferEntity;

public interface RejectOfferDao {

	public void deleteRejection(Integer loggedUserId, RejectOfferEntity rejection) throws Exception;


	public void undeleteRejection(Integer loggedUserId, RejectOfferEntity rejection) throws Exception;


	public void archiveRejection(Integer loggedUserId, RejectOfferEntity rejection) throws Exception;

}
