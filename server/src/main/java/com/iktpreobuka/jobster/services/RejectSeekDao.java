package com.iktpreobuka.jobster.services;

import com.iktpreobuka.jobster.entities.RejectSeekEntity;

public interface RejectSeekDao {

	public void deleteRejection(Integer loggedUserId, RejectSeekEntity rejection) throws Exception;


	public void undeleteRejection(Integer loggedUserId, RejectSeekEntity rejection) throws Exception;


	public void archiveRejection(Integer loggedUserId, RejectSeekEntity rejection) throws Exception;

}
