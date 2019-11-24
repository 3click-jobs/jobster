package com.iktpreobuka.jobster.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.JobDayHoursEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobDayHoursDto;
import com.iktpreobuka.jobster.entities.dto.JobSeekDto;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.JobDayHoursRepository;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.repositories.JobTypeRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;

@Service
public class JobSeekDaoImpl implements JobSeekDao {

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CityDao cityDao;

	@Autowired
	private JobTypeRepository jobTypeRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private JobDayHoursRepository jobDayHoursRepository;

	@Autowired
	private JobSeekRepository jobSeekRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Override // da bi mogao da vidim kako izgleda JobSeekDto bez dana
	public JobSeekDto empty() {
		return new JobSeekDto();
	}

	@Override // da bi mogao da vidim kako izgleda JobSeekDto sa dva dodata dana
	public JobSeekDto emptyWithEmptyDayHours() {
		JobSeekDto emptyWith = new JobSeekDto();
		JobDayHoursDto empty1 = new JobDayHoursDto();
		JobDayHoursDto empty2 = new JobDayHoursDto();
		List<JobDayHoursDto> list = new ArrayList<JobDayHoursDto>();
		list.add(empty1);
		list.add(empty2);
		emptyWith.setListJobDayHoursDto(list);
		return emptyWith;
	}

	@Override
	public ResponseEntity<?> addNewSeek(@Valid @RequestBody JobSeekDto seek, Principal principal,
			BindingResult result) {

		// validating entry
		try {
			logger.info("Checking errors.'");
			if (result.hasErrors()) {
				logger.info("Errors exist.");
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking errors.'");
			return new ResponseEntity<String>("Error occured during 'Checking errors.'" + e, HttpStatus.BAD_REQUEST);
		}

		// Checking logged account
		UserEntity loggedUser = new UserEntity();
		try {
			logger.info("Checking database for account.");
			if (((userAccountRepository.count()) == 0)
					|| (userAccountRepository.findUserByUsername(principal.getName())) == null) {
				logger.info("UserAccount doesn't exist.");
				return new ResponseEntity<String>("UserAccount doesn't exist.", HttpStatus.BAD_REQUEST);
			} else {
				loggedUser = userAccountRepository.findUserByUsername(principal.getName());
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checkin database'.");
			return new ResponseEntity<String>("Error occured during 'Checkin database'." + e, HttpStatus.BAD_REQUEST);
		}

		// checking city
		CityEntity city = new CityEntity();
		try {
			logger.info("Checking database for city.");
			if (((cityRepository.count()) == 0) || (cityRepository.getByCityName(seek.getCityName())) == null) {
				logger.info("Creating city.");
				try {
					city = cityDao.addNewCityWithLoggedUser(seek.getCityName(), seek.getLongitude(), seek.getLatitude(), seek.getCountryRegionName(), seek.getCountryName(), seek.getIso2Code(), loggedUser);
				} catch (Exception e) {
					logger.info("Error occured during 'Checkin database'.");
					return new ResponseEntity<String>("Error occured during 'Checkin database'." + e,
							HttpStatus.BAD_REQUEST);
				}
			} else {
				city = cityRepository.getByCityName(seek.getCityName());
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checkin database'.");
			return new ResponseEntity<String>("Error occured during 'Checkin database'." + e, HttpStatus.BAD_REQUEST);
		}

		// checking jobType
		try {
			logger.info("Checking database for jobType.");
			if (((jobTypeRepository.count()) == 0)
					|| (jobTypeRepository.getByJobTypeName(seek.getJobTypeName())) == null) {
				logger.info("JobType doesn't exist.");
				return new ResponseEntity<String>("JobType doesn't exist.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checkin database'.");
			return new ResponseEntity<String>("Error occured during 'Checkin database'." + e, HttpStatus.BAD_REQUEST);
		}

		JobSeekEntity newSeek = new JobSeekEntity();

		newSeek.setEmployee(loggedUser);
		newSeek.setCity(city);
		newSeek.setType(jobTypeRepository.getByJobTypeName(seek.getJobTypeName()));

		newSeek.setDistanceToJob(seek.getDistanceToJob());
		newSeek.setBeginningDate(seek.getBeginningDate());
		newSeek.setEndDate(seek.getEndDate());
		newSeek.setFlexibileDates(seek.getFlexibileDates());
		newSeek.setPrice(seek.getPrice());
		newSeek.setDetailsLink(seek.getDetailsLink());
		newSeek.setFlexibileDays(seek.getFlexibileDays());
		newSeek.setCreatedById(loggedUser.getId());

		List<JobDayHoursDto> listJobDayHoursDto = new ArrayList<JobDayHoursDto>();
		listJobDayHoursDto = seek.getListJobDayHoursDto();

		JobDayHoursEntity newDaysAndHours = new JobDayHoursEntity();
		List<JobDayHoursEntity> listJobDaysAndHours = new ArrayList<JobDayHoursEntity>();

		for (JobDayHoursDto i : listJobDayHoursDto) {// ovo nisam siguran da li ce dodavati nove clanove ili ce samo
														// zalepiti poslednji
			newDaysAndHours.setDay(i.getDay());
			newDaysAndHours.setFromHour(i.getFromHour());
			newDaysAndHours.setToHour(i.getToHour());
			newDaysAndHours.setFlexibileHours(i.getFlexibileHours());
			newDaysAndHours.setIsMinMax(i.getIsMinMax());
			listJobDaysAndHours.add(newDaysAndHours);
		}

		jobDayHoursRepository.saveAll(listJobDaysAndHours);
		newSeek.setDaysAndHours(listJobDaysAndHours);
		jobSeekRepository.save(newSeek);

		

		return new ResponseEntity<JobSeekDto>(seek, HttpStatus.OK);
	}

////////////////////////////////////////////GET BY ID///////////////////////////////////////////

	@Override
	public ResponseEntity<?> getById(@PathVariable Integer id) {

		try {
			logger.info("Looking for pupil.");
			JobSeekEntity wantedJobSeek = jobSeekRepository.findById(id).orElse(null);
			if (wantedJobSeek != null) {
				logger.info("JobSeek found.");
				return new ResponseEntity<JobSeekEntity>(wantedJobSeek, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}
		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);

	}

	////////////////// DELETE and UNDELETE ////////////////////////////

	@Override // da li sam dobro razumeno 1 i 0 i -1???????
	public ResponseEntity<?> deleteById(@PathVariable Integer id) {
		try {
			logger.info("Checking database.");
			if (((jobSeekRepository.count() == 0))) {
				logger.info("Database empty.");
				return new ResponseEntity<String>("Database empty.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		JobSeekEntity wantedJobSeek = new JobSeekEntity();
		try {
			logger.info("Looking for jobSeek.");
			wantedJobSeek = jobSeekRepository.findById(id).orElse(null);
			if (wantedJobSeek == null) {
				logger.info("jobSeek that you asked for doesn't exist.");
				return new ResponseEntity<String>("jobSeek that you asked for doesn't exist.", HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		try {
			logger.info("Changing activity.");
			if (wantedJobSeek.getStatus().equals(1)) {
				wantedJobSeek.setStatusInactive();
			}
			if (wantedJobSeek.getStatus().equals(0)) {
				wantedJobSeek.setStatusActive();
			}
			if (wantedJobSeek.getStatus().equals(-1)) {
				return new ResponseEntity<String>("jobSeek is arhived it can't be deleted or undeleted.",
						HttpStatus.OK);
			}
			jobSeekRepository.save(wantedJobSeek);
			logger.info("jobSeek changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Deleting jobSeek.'");
			return new ResponseEntity<String>("Error occured during 'Deleting jobSeek'." + e, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JobSeekEntity>(wantedJobSeek, HttpStatus.BAD_REQUEST);
	}

	////////////////////// ARCHICE and UNARCHIVE //////////////////////////

	@Override // da li sam dobro razumeno 1 i 0 i -1???????
	public ResponseEntity<?> archiveById(@PathVariable Integer id) {
		try {
			logger.info("Checking database.");
			if (((jobSeekRepository.count() == 0))) {
				logger.info("Database empty.");
				return new ResponseEntity<String>("Database empty.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		JobSeekEntity wantedJobSeek = new JobSeekEntity();
		try {
			logger.info("Looking for jobSeek.");
			wantedJobSeek = jobSeekRepository.findById(id).orElse(null);
			if (wantedJobSeek == null) {
				logger.info("jobSeek that you asked for doesn't exist.");
				return new ResponseEntity<String>("jobSeek that you asked for doesn't exist.", HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		try {
			logger.info("Changing activity.");
			if (wantedJobSeek.getStatus().equals(0) || wantedJobSeek.getStatus().equals(1)) {
				wantedJobSeek.setStatusArchived();
			} else if (wantedJobSeek.getStatus().equals(-1)) {
				wantedJobSeek.setStatusActive();
			} else {
				wantedJobSeek.setStatusArchived();
			}
			jobSeekRepository.save(wantedJobSeek);
			logger.info("jobSeek changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Deleting jobSeek.'");
			return new ResponseEntity<String>("Error occured during 'Deleting jobSeek'." + e, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<JobSeekEntity>(wantedJobSeek, HttpStatus.BAD_REQUEST);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}
