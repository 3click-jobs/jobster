package com.iktpreobuka.jobster.services;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.JobTypeEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobTypeDTO;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.repositories.JobTypeRepository;

@Service
public class JobTypeDaoImpl implements JobTypeDao {

	@Autowired
	private JobTypeRepository jobTypeRepository;

	@Autowired
	private JobSeekRepository jobSeekRepository;

	@Autowired
	private JobOfferRepository jobOfferRepository;
	

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	
	@Override
	public Iterable<JobTypeEntity> findJobTypeByStatusLike(Integer status) throws Exception {
		logger.info("findJobTypeByStatusLike finished.");
		return jobTypeRepository.findByStatusLike(status);
	}
	
	@Override
	public JobTypeEntity addNewJobType(UserEntity loggedUser, JobTypeDTO newJobType) throws Exception {
		logger.info("addNewJobType started.");
		if ( newJobType.getJobTypeName() == null  ) {
			throw new Exception("Some data is null.");
		}
		logger.info("addNewJobType validation Ok.");
		JobTypeEntity jobType = new JobTypeEntity();
		try {
			jobType.setJobTypeName(newJobType.getJobTypeName());
			jobType.setStatusActive();
			jobType.setCreatedById(loggedUser.getId());
			jobTypeRepository.save(jobType);
			logger.info("Job type created.");
			logger.info("addNewJobType finished.");
		} catch (Exception e) {
			throw new Exception("addNewJobType save failed." + e.getLocalizedMessage());
		}
		return jobType;
	}
	
	@Override
	public void modifyJobType(UserEntity loggedUser, JobTypeEntity jobType, JobTypeDTO updateJobType) throws Exception {
		logger.info("modifyJobType started.");
		if (updateJobType.getJobTypeName() == null ) {
			throw new Exception("All data is null.");
		}
		try {
			if (updateJobType.getJobTypeName() != null && !updateJobType.getJobTypeName().equals(" ") && !updateJobType.getJobTypeName().equals("") && !updateJobType.getJobTypeName().equals(jobType.getJobTypeName())) {
				jobType.setJobTypeName(updateJobType.getJobTypeName());
				jobType.setUpdatedById(loggedUser.getId());
				jobTypeRepository.save(jobType);
			}
			logger.info("modifyJobType finished.");
		} catch (Exception e) {
			throw new Exception("modifyJobType faild on saving." + e.getLocalizedMessage());
		}
	}

	@Override
	public void deleteJobType(UserEntity loggedUser, JobTypeEntity jobType) throws Exception {
		logger.info("deleteJobType started.");
		try {
			for (JobOfferEntity jo : jobType.getOffers()) {
				if (jo.getStatus() == 1) {
					jo.setStatusInactive();
					jo.setUpdatedById(loggedUser.getId());
					jobOfferRepository.save(jo);
				}
			}
			for (JobSeekEntity js : jobType.getSeeks()) {
				if (js.getStatus() == 1) {
					js.setStatusInactive();
					js.setUpdatedById(loggedUser.getId());
					jobSeekRepository.save(js);
				}
			}
			jobType.setStatusInactive();
			jobType.setUpdatedById(loggedUser.getId());
			jobTypeRepository.save(jobType);
			logger.info("deleteJobType finished.");
		} catch (Exception e) {
			throw new Exception("deleteJobType failed on saving." + e.getLocalizedMessage());
		}

	}

	@Override
	public void undeleteJobType(UserEntity loggedUser, JobTypeEntity jobType) throws Exception {
		logger.info("undeleteJobType started.");
		try {
			jobType.setStatusActive();
			jobType.setUpdatedById(loggedUser.getId());
			jobTypeRepository.save(jobType);
			logger.info("undeleteJobType finished.");
		} catch (Exception e) {
			throw new Exception("undeleteJobType failed on saving." + e.getLocalizedMessage());
		}	
	}

	@Override
	public void archiveJobType(UserEntity loggedUser, JobTypeEntity jobType) throws Exception {
		logger.info("archiveJobType started.");
		try {
			for (JobOfferEntity jo : jobType.getOffers()) {
					jo.setStatusArchived();
					jo.setUpdatedById(loggedUser.getId());
					jobOfferRepository.save(jo);
			}
			for (JobSeekEntity js : jobType.getSeeks()) {
					js.setStatusArchived();
					js.setUpdatedById(loggedUser.getId());
					jobSeekRepository.save(js);
			}
			jobType.setStatusArchived();
			jobType.setUpdatedById(loggedUser.getId());
			jobTypeRepository.save(jobType);
			logger.info("archiveJobType finished.");
		} catch (Exception e) {
			throw new Exception("archiveJobType failed on saving." + e.getLocalizedMessage());
		}		

	}
	
	@Override
	public void saveAll(@Valid List<JobTypeEntity> jobTypes) throws Exception {
		logger.info("saveAll started.");
		try {
			jobTypeRepository.saveAll(jobTypes);
			logger.info("saveAll finished.");
		} catch (Exception e) {
			throw new Exception("saveAll failed on saving." + e.getLocalizedMessage());
		}
	}
	
}
