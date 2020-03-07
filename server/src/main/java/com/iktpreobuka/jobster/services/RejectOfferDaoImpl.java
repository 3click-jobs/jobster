package com.iktpreobuka.jobster.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.RejectOfferEntity;
import com.iktpreobuka.jobster.repositories.RejectOfferRepository;


@Service
public class RejectOfferDaoImpl implements RejectOfferDao {

	@Autowired
	RejectOfferRepository rejectOfferRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	public void deleteRejection(Integer loggedUserId, RejectOfferEntity rejection) throws Exception {
		logger.info("++++++++++++++++ Service for deleting rejection started");
		try {
			rejection.setStatusInactive();
			rejection.setUpdatedById(loggedUserId);
			rejectOfferRepository.save(rejection);
			logger.info("---------------- Service for deleting rejection OK");
		} catch (Exception e) {
			throw new Exception("DeleteRejection failed on saving." + e.getLocalizedMessage());
		}
	}


	public void undeleteRejection(Integer loggedUserId, RejectOfferEntity rejection) throws Exception {
		logger.info("++++++++++++++++ Service for undeleting rejection started");
		try {
			rejection.setStatusActive();
			rejection.setUpdatedById(loggedUserId);
			rejectOfferRepository.save(rejection);
			logger.info("---------------- Service for undeleting rejection OK");
		} catch (Exception e) {
			throw new Exception("UndeleteRejection failed on saving." + e.getLocalizedMessage());
		}
	}


	public void archiveRejection(Integer loggedUserId, RejectOfferEntity rejection) throws Exception {
		logger.info("++++++++++++++++ Service for archiving rejection started");
		try {
			rejection.setStatusArchived();
			rejection.setUpdatedById(loggedUserId);
			rejectOfferRepository.save(rejection);
			logger.info("---------------- Service for archiving rejection OK");
		} catch (Exception e) {
			throw new Exception("ArchiveRejection failed on saving." + e.getLocalizedMessage());
		}
	}

}