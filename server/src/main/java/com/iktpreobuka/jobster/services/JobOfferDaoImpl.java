package com.iktpreobuka.jobster.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;

import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;

@Service
public class JobOfferDaoImpl implements JobOfferDao {

	@Autowired
	private JobOfferRepository jobOfferRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

/////////////////// PRAZAN SEEK BEZ DAYANDHOURS///////////////////////

	@Override
	public JobOfferEntity emptyJobOfferEntity() {
		return new JobOfferEntity();
	}

/////////////////////////////////////////////GET ALL /////////////////////////////////////////

	@Override
	public ResponseEntity<?> getAll() {
		try {
			logger.info("Checking database for JobOffer.");
			if (((jobOfferRepository.count() == 0))) {
				logger.info("Database empty.");
				return new ResponseEntity<String>("Database empty.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		List<JobOfferEntity> list = (List<JobOfferEntity>) jobOfferRepository.findAll();
		logger.info("Returning result.");
		return new ResponseEntity<List<JobOfferEntity>>(list, HttpStatus.OK);
	}

////////////////////////////////////////////GET BY ID///////////////////////////////////////////

	@Override
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			logger.info("Looking for JobOffer.");
			JobOfferEntity wantedJobOffer = jobOfferRepository.findById(id).orElse(null);
			if (wantedJobOffer != null) {
				logger.info("JobOffer found.");
				return new ResponseEntity<JobOfferEntity>(wantedJobOffer, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}
		logger.info("JobOffer that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobOffer that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

//////////////////DELETE ////////////////////////////

	@Override
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		try {
			logger.info("Checking database.");
			if (((jobOfferRepository.count() == 0))) {
				logger.info("Database empty.");
				return new ResponseEntity<String>("Database empty.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		JobOfferEntity wantedJobOffer = new JobOfferEntity();
		try {
			logger.info("Looking for jobOffer.");
			wantedJobOffer = jobOfferRepository.findById(id).orElse(null);
			if (wantedJobOffer == null) {
				logger.info("jobOffer that you asked for doesn't exist.");
				return new ResponseEntity<String>("jobOffer that you asked for doesn't exist.", HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		try {
			logger.info("Changing activity.");
			if (wantedJobOffer.getStatus().equals(1)) {
				logger.info("Deleting entity.");
				wantedJobOffer.setStatusInactive();
//applyContactDaoImp.markApplyAsExpiredByOffer(wantedJobOffer); //DODAO ZA NIDZU
				logger.info("Entity deleted.");
			} else if (wantedJobOffer.getStatus().equals(0)) {
				logger.info("JobOffer status is already deleted.");
				return new ResponseEntity<String>("JobOffer status is already deleted.", HttpStatus.OK);
			} else if (wantedJobOffer.getStatus().equals(-1)) {
				logger.info("JobOffer is arhived it can't be deleted or undeleted.");
				return new ResponseEntity<String>("JobOffer is arhived it can't be deleted or undeleted.",
						HttpStatus.OK);
			} else {
				logger.info("JobOffer has unknown status, check status in datebase for jobOffer.");
				return new ResponseEntity<String>("JobOffer has unknown status, check status in datebase for jobOffer.",
						HttpStatus.OK);
			}
			logger.info("Saveing entity.");
			jobOfferRepository.save(wantedJobOffer);
			logger.info("jobOffer changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Deleting jobOffer.'");
			return new ResponseEntity<String>("Error occured during 'Deleting jobOffer'." + e, HttpStatus.BAD_REQUEST);
		}
		logger.info("Returning jobOffer.");
		return new ResponseEntity<JobOfferEntity>(wantedJobOffer, HttpStatus.OK);
	}

////////////////////// UNDELETE //////////////////////////////

	@Override
	public ResponseEntity<?> unDeleteById(@PathVariable Integer id) {
		try {
			logger.info("Checking database.");
			if (((jobOfferRepository.count() == 0))) {
				logger.info("Database empty.");
				return new ResponseEntity<String>("Database empty.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		JobOfferEntity wantedJobOffer = new JobOfferEntity();
		try {
			logger.info("Looking for jobOffer.");
			wantedJobOffer = jobOfferRepository.findById(id).orElse(null);
			if (wantedJobOffer == null) {
				logger.info("JobOffer that you asked for doesn't exist.");
				return new ResponseEntity<String>("JobOffer that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		try {
			logger.info("Changing activity.");
			if (wantedJobOffer.getStatus().equals(0)) {
				logger.info("Undeleteing entity.");
				wantedJobOffer.setStatusActive();
				logger.info("Entity deleted.");
			} else if (wantedJobOffer.getStatus().equals(1)) {
				logger.info("JobOffer status is already activ.");
				return new ResponseEntity<String>("JobOffer status is already activ.", HttpStatus.OK);
			} else if (wantedJobOffer.getStatus().equals(-1)) {
				logger.info("JobOffer is arhived it can't be deleted or undeleted.");
				return new ResponseEntity<String>("JobOffer is arhived it can't be deleted or undeleted.",
						HttpStatus.OK);
			} else {
				logger.info("JobOffer has unknown status, check status in datebase for jobOffer.");
				return new ResponseEntity<String>("JobOffer has unknown status, check status in datebase for jobOffer.",
						HttpStatus.OK);
			}
			logger.info("Saveing entity.");
			jobOfferRepository.save(wantedJobOffer);
			logger.info("jobOffer changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Undeleting jobOffer.'");
			return new ResponseEntity<String>("Error occured during 'Undeleting jobOffer'." + e,
					HttpStatus.BAD_REQUEST);
		}
		logger.info("Returning jobOffer.");
		return new ResponseEntity<JobOfferEntity>(wantedJobOffer, HttpStatus.OK);
	}

////////////////////// ARCHIVE //////////////////////////

	@Override
	public ResponseEntity<?> archiveById(@PathVariable Integer id) {
		try {
			logger.info("Checking database.");
			if (((jobOfferRepository.count() == 0))) {
				logger.info("Database empty.");
				return new ResponseEntity<String>("Database empty.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		JobOfferEntity wantedJobOffer = new JobOfferEntity();
		try {
			logger.info("Looking for jobOffer.");
			wantedJobOffer = jobOfferRepository.findById(id).orElse(null);
			if (wantedJobOffer == null) {
				logger.info("jobOffer that you asked for doesn't exist.");
				return new ResponseEntity<String>("JobOffer that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		try {
			logger.info("Changing activity.");
			if (wantedJobOffer.getStatus().equals(0) || wantedJobOffer.getStatus().equals(1)) {
				logger.info("Archiving entity.");
				wantedJobOffer.setStatusArchived();
//applyContactDaoImp.markApplyAsExpiredByOffer(wantedJobOffer); // DODAO ZA NIDZU
				logger.info("Entity archived.");
			} else if (wantedJobOffer.getStatus().equals(-1)) {
				logger.info("JobOffer status is already archived.");
				return new ResponseEntity<String>("JobOffer status is already archived.", HttpStatus.OK);
			} else {
				logger.info("JobOffer has unknown status, check status in datebase for jobOffer.");
				return new ResponseEntity<String>("JobOffer has unknown status, check status in datebase for jobOffer.",
						HttpStatus.OK);
			}
			logger.info("Saveing entity.");
			jobOfferRepository.save(wantedJobOffer);
			logger.info("jobOffer changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Archiveing jobOffer.'");
			return new ResponseEntity<String>("Error occured during 'Archiveing jobOffer'." + e,
					HttpStatus.BAD_REQUEST);
		}
		logger.info("Returning jobOffer.");
		return new ResponseEntity<JobOfferEntity>(wantedJobOffer, HttpStatus.OK);
	}

///////////////////// UNARCHIVE ///////////////////////////////

	@Override
	public ResponseEntity<?> unArchiveById(@PathVariable Integer id) {
		try {
			logger.info("Checking database.");
			if (((jobOfferRepository.count() == 0))) {
				logger.info("Database empty.");
				return new ResponseEntity<String>("Database empty.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		JobOfferEntity wantedJobOffer = new JobOfferEntity();
		try {
			logger.info("Looking for jobOffer.");
			wantedJobOffer = jobOfferRepository.findById(id).orElse(null);
			if (wantedJobOffer == null) {
				logger.info("JobOffer that you asked for doesn't exist.");
				return new ResponseEntity<String>("JobOffer that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		try {
			logger.info("Changing activity.");
			if (wantedJobOffer.getStatus().equals(-1)) {
				logger.info("Unarchiving entity.");
				wantedJobOffer.setStatusActive();
				logger.info("Entity unarchived.");
			} else if (wantedJobOffer.getStatus().equals(1) || wantedJobOffer.getStatus().equals(0)) {
				logger.info("JobOffer status is already active.");
				return new ResponseEntity<String>("JobOffer status is already active.", HttpStatus.OK);
			} else {
				logger.info("JobOffer has unknown status, check status in datebase for jobOffer.");
				return new ResponseEntity<String>("JobOffer has unknown status, check status in datebase for jobOffer.",
						HttpStatus.OK);
			}
			logger.info("Saveing entity.");
			jobOfferRepository.save(wantedJobOffer);
			logger.info("jobOffer changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Unarchiveing jobOffer.'");
			return new ResponseEntity<String>("Error occured during 'Unarchiveing jobOffer'." + e,
					HttpStatus.BAD_REQUEST);
		}
		logger.info("Returning jobOffer.");
		return new ResponseEntity<JobOfferEntity>(wantedJobOffer, HttpStatus.OK);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}
}
