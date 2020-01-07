package com.iktpreobuka.jobster.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.JobDayHoursEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobDayHoursPostDto;
import com.iktpreobuka.jobster.entities.dto.JobDayHoursPutDto;
import com.iktpreobuka.jobster.entities.dto.JobSeekPostDto;
import com.iktpreobuka.jobster.entities.dto.JobSeekPutDto;
import com.iktpreobuka.jobster.enumerations.EDay;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.JobDayHoursRepository;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.repositories.JobTypeRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;
import com.iktpreobuka.jobster.repositories.UserRepository;

@Service
public class JobSeekDaoImpl implements JobSeekDao {

//	@Autowired
//	private ApplyContactDaoImp applyContactDaoImp;

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

	///////////////////////// POST ///////////////////////////////////////

	@Override
	public ResponseEntity<?> addNewSeek(@Valid @RequestBody JobSeekPostDto seek, Principal principal,
			BindingResult result) {

		logger.info("Starting addNewSeek().---------------------");
		JobSeekEntity newSeek = new JobSeekEntity();

		try {

			// Checking does entry data exist

			logger.info("Checking does entry data exist.'");
			if (seek == null) {
				logger.info("Entry data missing.");
				return new ResponseEntity<String>("Entry data missing.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");

			// validating entry

			logger.info("Checking errors.'");
			if (result.hasErrors()) {
				logger.info("Errors exist.");
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");

			// Checking entered data

			if (seek.getCityName() == null || seek.getJobTypeName() == null || seek.getDistanceToJob() == null
					|| seek.getBeginningDate() == null || seek.getEndDate() == null || seek.getPrice() == null
					|| seek.getDetailsLink() == null || seek.getListJobDayHoursPostDto() == null
					|| seek.getCountryName() == null || seek.getIso2Code() == null || seek.getLongitude() == null
					|| seek.getLatitude() == null) {
				logger.info("Some atributes are null.");
				return new ResponseEntity<String>("Some atributes are null", HttpStatus.BAD_REQUEST);
			}
			if (seek.getCityName().equals(" ") || seek.getJobTypeName().equals(" ") || seek.getDistanceToJob().equals(" ")
					|| seek.getBeginningDate().equals(" ") || seek.getEndDate().equals(" ") || seek.getPrice().equals(" ")
					|| seek.getDetailsLink().equals(" ") || seek.getListJobDayHoursPostDto().equals(" ")
					|| seek.getCountryName().equals(" ") || seek.getIso2Code().equals(" ") || seek.getLongitude().equals(" ")
					|| seek.getLatitude().equals("") || seek.getCityName().equals("") || seek.getJobTypeName().equals("") || seek.getDistanceToJob().equals("")
					|| seek.getBeginningDate().equals("") || seek.getEndDate().equals("") || seek.getPrice().equals("")
					|| seek.getDetailsLink().equals("") || seek.getListJobDayHoursPostDto().equals("")
					|| seek.getCountryName().equals("") || seek.getIso2Code().equals("") || seek.getLongitude().equals("")
					|| seek.getLatitude().equals("")) {
				logger.info("Some atributes are blanks.");
				return new ResponseEntity<String>("Some atributes are blanks", HttpStatus.BAD_REQUEST);
			}

			// Checking logged account

			UserAccountEntity loggedAccount = new UserAccountEntity();

			logger.info("Checking userAccount database.");
			if (userAccountRepository.count() == 0) {
				logger.info("UserAccount database empty.");
				return new ResponseEntity<String>("UserAccount database empty.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking does userAccount exist.");
			if (userAccountRepository.findByUsername(principal.getName()).equals(null)) {
				logger.info("UserAccount doesn't exist.");
				return new ResponseEntity<String>("UserAccount doesn't exist.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking is userAccount deleted.");
			if (!(userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 0) == null)) {
				logger.info("UserAccount is deleted.");
				return new ResponseEntity<String>("UserAccount is deleted.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking is userAccount archived.");
			if (!(userAccountRepository.findByUsernameAndStatusLike(principal.getName(), -1) == null)) {
				logger.info("UserAccount is archived.");
				return new ResponseEntity<String>("UserAccount is archived.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking does userAccount have user.");
			if (userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser().equals(null)) {
				logger.info("UserAccout doesn't have user.");
				return new ResponseEntity<String>("UserAccout doesn't have user.", HttpStatus.BAD_REQUEST);
			} else {
				logger.info("OK");
				logger.info("UserAccount found.");
				loggedAccount = userAccountRepository.getByUsername(principal.getName());
				logger.info("OK");
			}

			// checking loggedUser

			UserEntity loggedUser = new UserEntity();
			loggedUser = loggedAccount.getUser();

			logger.info("Checking does user have status.");
			if (loggedUser.getStatus() == null) {
				logger.info("User doesn't have status.");
				return new ResponseEntity<String>("User doesn't have status.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking is user deleted.");
			if (loggedUser.getStatus() == 0) {
				logger.info("User is deleted.");
				return new ResponseEntity<String>("User is deleted.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking is user archived.");
			if (loggedUser.getStatus() == -1) {
				logger.info("User is archived.");
				return new ResponseEntity<String>("User is archived.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");

			// checking city

			CityEntity city = new CityEntity();

			logger.info("Checking database for city.");
			if (((cityRepository.count()) == 0) || (cityRepository.getByCityName(seek.getCityName())) == null) {
				logger.info("Creating city.");
				try {
					city = cityDao.addNewCityWithLoggedUser(seek.getCityName(), seek.getLongitude(), seek.getLatitude(),
							seek.getCountryRegionName(), seek.getCountryName(), seek.getIso2Code(), loggedUser);
				} catch (Exception e) {
					logger.info("Error occured during 'Checkin database'.");
					return new ResponseEntity<String>(
							"Error occured during 'Checkin database'." + e.getMessage() + " " + e,
							HttpStatus.BAD_REQUEST);
				}
			} else {
				city = cityRepository.getByCityName(seek.getCityName());
			}
			logger.info("OK");

			// checking jobType

			logger.info("Checking database for jobType.");
			if (((jobTypeRepository.count()) == 0)
					|| (jobTypeRepository.getByJobTypeName(seek.getJobTypeName())) == null) {
				logger.info("JobType doesn't exist.");
				return new ResponseEntity<String>("JobType doesn't exist.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");

			// checking size of list of checked days and are there duplicate days

			List<JobDayHoursPostDto> listJobDayHoursPostDto = new ArrayList<JobDayHoursPostDto>();
			listJobDayHoursPostDto = seek.getListJobDayHoursPostDto();

			if (listJobDayHoursPostDto.size() > 0) {
				logger.info("Checking size of list of checked days and are there duplicate days.");
				if (listJobDayHoursPostDto.size() > 7) {
					logger.info("List can contain 7 elements max.");
					return new ResponseEntity<String>("List can contain 7 elements max.", HttpStatus.OK);
				}
				Integer monday = 0;
				Integer tuesday = 0;
				Integer wednesday = 0;
				Integer thursday = 0;
				Integer friday = 0;
				Integer saturday = 0;
				Integer sunday = 0;
				for (JobDayHoursPostDto i : listJobDayHoursPostDto) {
					if (i.getDay().equals(EDay.DAY_MONDAY)) {
						monday++;
					}
					if (i.getDay().equals(EDay.DAY_TUESDAY)) {
						tuesday++;
					}
					if (i.getDay().equals(EDay.DAY_WEDNESDAY)) {
						wednesday++;
					}
					if (i.getDay().equals(EDay.DAY_THURSDAY)) {
						thursday++;
					}
					if (i.getDay().equals(EDay.DAY_FRIDAY)) {
						friday++;
					}
					if (i.getDay().equals(EDay.DAY_SATURDAY)) {
						saturday++;
					}
					if (i.getDay().equals(EDay.DAY_SUNDAY)) {
						sunday++;
					}
				}
				if (monday > 1 || tuesday > 1 || wednesday > 1 || thursday > 1 || friday > 1 || saturday > 1
						|| sunday > 1) {
					logger.info("You have duplicate days.");
					return new ResponseEntity<String>("You have duplicate days.", HttpStatus.BAD_REQUEST);
				}
			}
			logger.info("OK");

			// Mapping atributs

			logger.info("Mapping atributs.");
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
			newSeek.setStatusActive();
			newSeek.setElapseActive();
			newSeek.setVersion(1);
			newSeek.setCreatedById(loggedUser.getId());
			logger.info("Atributs mapped.");

			List<JobDayHoursEntity> listJobDaysAndHours = new ArrayList<JobDayHoursEntity>();

			Integer numberOfDays = 0;
			logger.info("Mapping days and hours.");
			for (JobDayHoursPostDto i : listJobDayHoursPostDto) {
				JobDayHoursEntity newDayAndHours = new JobDayHoursEntity();
				newDayAndHours.setDay(i.getDay());
				newDayAndHours.setFromHour(i.getFromHour());
				newDayAndHours.setToHour(i.getToHour());
				newDayAndHours.setFlexibileHours(i.getFlexibileHours());
				newDayAndHours.setIsMinMax(i.getIsMinMax());
				newDayAndHours.setStatusActive();
				newDayAndHours.setVersion(1);
				logger.info("Saveing day and hours for day" + i.getDay());///////////////////////////////////
				jobDayHoursRepository.save(newDayAndHours);
				logger.info("OK");
				logger.info("Adding new DayAndHours to list.");
				listJobDaysAndHours.add(newDayAndHours);
				logger.info("OK");
				numberOfDays++;
			}

			logger.info("There is " + numberOfDays + " day(s) added.");
			logger.info("Days and hours mapped.");
			logger.info("Adding list of days and hours to JobSeek.");
			newSeek.setDaysAndHours(listJobDaysAndHours);
			logger.info("OK");
			logger.info("Saveing JobSeek.");
			jobSeekRepository.save(newSeek);
			logger.info("OK");
			logger.info("Atributs mapped.");

		} catch (Exception e) {
			logger.info("Error ocured.");
			return new ResponseEntity<String>("Error ocured.------------------- " + e.getMessage() + " " + e,
					HttpStatus.BAD_REQUEST);
		}

		logger.info("Returning new jobSeek.---------------");
		return new ResponseEntity<JobSeekEntity>(newSeek, HttpStatus.OK);
	}

	///////////////////////////// PUT /////////////////////////////

	@Override
	public ResponseEntity<?> modifySeek(@Valid @RequestBody JobSeekPutDto seek, @PathVariable Integer seekId,
			Principal principal, BindingResult result) {

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

		// checking seek
		JobSeekEntity seekForModify = new JobSeekEntity();
		try {
			logger.info("Looking for pupil.");
			seekForModify = jobSeekRepository.findById(seekId).orElse(null);
			if (seekForModify == null) {
				logger.info("JobSeek that you asked for doesn't exist.");
				return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		// checking city
		CityEntity city = new CityEntity();
		if (seek.getCityName() != null) {
			try {
				logger.info("Checking database for city.");
				if (((cityRepository.count()) == 0) || (cityRepository.getByCityName(seek.getCityName())) == null) {
					logger.info("Creating city.");
					try {
						city = cityDao.addNewCityWithLoggedUser(seek.getCityName(), seek.getLongitude(),
								seek.getLatitude(), seek.getCountryRegionName(), seek.getCountryName(),
								seek.getIso2Code(), loggedUser);
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
				return new ResponseEntity<String>("Error occured during 'Checkin database'." + e,
						HttpStatus.BAD_REQUEST);
			}
		}

		// checking jobType
		if (seek.getJobTypeName() != null) {
			try {
				logger.info("Checking database for jobType.");
				if (((jobTypeRepository.count()) == 0)
						|| (jobTypeRepository.getByJobTypeName(seek.getJobTypeName())) == null) {
					logger.info("JobType doesn't exist.");
					return new ResponseEntity<String>("JobType doesn't exist.", HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				logger.info("Error occured during 'Checkin database'.");
				return new ResponseEntity<String>("Error occured during 'Checkin database'." + e,
						HttpStatus.BAD_REQUEST);
			}
		}

		// Mapping atributs
		try {
			logger.info("Mapping atributs.");
			seekForModify.setEmployee(loggedUser);
			if (seek.getCityName() != null
					&& (cityRepository.getByCityName(seek.getCityName()) != seekForModify.getCity())) {
				seekForModify.setCity(city);
				logger.info("City changed.");
			}
			if (seek.getJobTypeName() != null && seek.getJobTypeName() != seekForModify.getType().getJobTypeName()) {
				seekForModify.setType(jobTypeRepository.getByJobTypeName(seek.getJobTypeName()));
				logger.info("Job type name changed.");
			}
			if (seek.getDistanceToJob() != null && seek.getDistanceToJob() != seekForModify.getDistanceToJob()) {
				seekForModify.setDistanceToJob(seek.getDistanceToJob());
				logger.info("Distance to job changed.");
			}
			if (seek.getBeginningDate() != null && seek.getBeginningDate() != seekForModify.getBeginningDate()) {
				seekForModify.setBeginningDate(seek.getBeginningDate());
				logger.info("Beginning date changed.");
			}
			if (seek.getEndDate() != null && seek.getEndDate() != seekForModify.getEndDate()) {
				seekForModify.setEndDate(seek.getEndDate());
				logger.info("End date changed.");
			}
			if (seek.getFlexibileDates() != null && seek.getFlexibileDates() != seekForModify.getFlexibileDates()) {
				seekForModify.setFlexibileDates(seek.getFlexibileDates());
				logger.info("Flexibility for dates changed.");
			}
			if (seek.getPrice() != null && seek.getPrice() != seekForModify.getPrice()) {
				seekForModify.setPrice(seek.getPrice());
				logger.info("Price changed.");
			}
			if (seek.getDetailsLink() != null && seek.getDetailsLink() != seekForModify.getDetailsLink()) {
				seekForModify.setDetailsLink(seek.getDetailsLink());
				logger.info("Details changed");
			}
			if (seek.getFlexibileDays() != null && seek.getFlexibileDays() != seekForModify.getFlexibileDays()) {
				seekForModify.setFlexibileDays(seek.getFlexibileDays());
				logger.info("Flexibility for days changed.");
			}
			seekForModify.setCreatedById(loggedUser.getId());

			if (!seek.getListJobDayHoursPutDto().isEmpty()) {
				List<JobDayHoursPutDto> listJobDayHoursPutDto = new ArrayList<JobDayHoursPutDto>();
				listJobDayHoursPutDto = seek.getListJobDayHoursPutDto();

				JobDayHoursEntity newDaysAndHours = new JobDayHoursEntity();
				List<JobDayHoursEntity> listJobDaysAndHours = new ArrayList<JobDayHoursEntity>();

				logger.info("Mapping days and hours.");
				for (JobDayHoursPutDto i : listJobDayHoursPutDto) {
					if (i.getDay() == null) {
						logger.info("Missing data. You need to put 'day'.");
						return new ResponseEntity<String>("You need to put 'day'.", HttpStatus.BAD_REQUEST);
					}
					if (i.getDay() != null) {
						newDaysAndHours.setDay(i.getDay());
					}

					if (i.getFromHour() == null) {
						logger.info("Missing data. You need to put 'from hour'.");
						return new ResponseEntity<String>("You need to put 'from hour'.", HttpStatus.BAD_REQUEST);
					}
					if (i.getFromHour() != null) {
						newDaysAndHours.setFromHour(i.getFromHour());
					}

					if (i.getToHour() == null) {
						logger.info("Missing data. You need to put 'to hour'.");
						return new ResponseEntity<String>("You need to put 'to hour'.", HttpStatus.BAD_REQUEST);
					}
					if (i.getToHour() != null) {
						newDaysAndHours.setToHour(i.getToHour());
					}

					if (i.getFlexibileHours() == null && i.getIsMinMax() == null) {
						newDaysAndHours.setFlexibileHours(true);
						newDaysAndHours.setIsMinMax(false);
					}

					if (i.getFlexibileHours() == true && i.getIsMinMax() == true) {
						return new ResponseEntity<String>("You need decide between 'flexibile hours' and 'MinMax'.",
								HttpStatus.BAD_REQUEST);
					}

					if (i.getFlexibileHours() == false && i.getIsMinMax() == false) {
						return new ResponseEntity<String>("You need decide between 'flexibile hours' and 'MinMax'.",
								HttpStatus.BAD_REQUEST);
					}

					if (i.getFlexibileHours() != null || i.getIsMinMax() != null)
						newDaysAndHours.setFlexibileHours(i.getFlexibileHours());
					newDaysAndHours.setIsMinMax(i.getIsMinMax());
					listJobDaysAndHours.add(newDaysAndHours);
				}
				logger.info("Days and hours changed.");
				logger.info("Saveing days and hours.");
				jobDayHoursRepository.saveAll(listJobDaysAndHours);
				logger.info("Adding all list of days and hours to JobSeek.");
				seekForModify.setDaysAndHours(listJobDaysAndHours);
			}
			logger.info("Saveing JobSeek.");
			jobSeekRepository.save(seekForModify);
			logger.info("Atributs mapped.");
		} catch (Exception e) {
			logger.info("Error ocured during mapping atributs.");
			return new ResponseEntity<String>("Error ocured during mapping atributs.", HttpStatus.BAD_REQUEST);
		}
		logger.info("Returning new jobSeek.");
		return new ResponseEntity<JobSeekEntity>(seekForModify, HttpStatus.OK);
	}

/////////////////////////////////////////////GET ALL /////////////////////////////////////////

	@Override
	public ResponseEntity<?> getAll() {
		try {
			logger.info("Checking database for JobSeek.");
			if (((jobSeekRepository.count() == 0))) {
				logger.info("Database empty.");
				return new ResponseEntity<String>("Database empty.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		List<JobSeekEntity> list = (List<JobSeekEntity>) jobSeekRepository.findAll();
		logger.info("Returning result.");
		return new ResponseEntity<List<JobSeekEntity>>(list, HttpStatus.OK);
	}

////////////////////////////////////////////GET BY ID///////////////////////////////////////////

	@Override
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		try {
			logger.info("Looking for JobSeek.");
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

///////////////////////////////////////GET BY EMPLOYEE /////////////////////////////////////

	@Override
	public ResponseEntity<?> getAllLikeEmployee(@PathVariable Integer id) {
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

		try {
			logger.info("Looking for jobs with selected employee");
			List<JobSeekEntity> wantedJobSeeks = jobSeekRepository.getAllByEmployeeId(id);
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);

	}

////////////////////////////////////////// GET BY JOBTYPE ////////////////////////////////////////

	@Override
	public ResponseEntity<?> getAllLikeJobType(@PathVariable Integer id) {
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

		try {
			logger.info("Looking for jobs with selected jobType");
			List<JobSeekEntity> wantedJobSeeks = jobSeekRepository.getAllByTypeId(id);
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);

	}

///////////////////////////////////////GET BY CITY /////////////////////////////////////

	@Override
	public ResponseEntity<?> getAllLikeCity(@PathVariable Integer id) {
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

		try {
			logger.info("Looking for jobs with selected city");
			List<JobSeekEntity> wantedJobSeeks = jobSeekRepository.getAllByCityId(id);
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);

	}

///////////////////////////////////////GET BY DISTANCE TO JOB /////////////////////////////////////

	@Override
	public ResponseEntity<?> getAllJobSeekWhereDistanceIsAndLessThen(@RequestParam Integer distance) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs with 'distance' less then defined.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getDistanceToJob() <= distance) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

	///////////////////// GET BY BEGINNING DATE /////////////////////////

	@Override
	public ResponseEntity<?> getAllWithBeginnigDate(@RequestParam Date beginDate) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'beginnigDate'.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getBeginningDate() == beginDate) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

///////////////////// GET BY END DATE /////////////////////////

	@Override
	public ResponseEntity<?> getAllWithEndDate(@RequestParam Date endDate) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'endDate'.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getEndDate() == endDate) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

///////////////////// GET BY FLEXIBILE DATES /////////////////////////

	@Override
	public ResponseEntity<?> getAllWithFlexibileDates(@RequestParam boolean flexDates) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'flexibileDates'.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getFlexibileDates() == flexDates) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

///////////////////// GET BY PRICE /////////////////////////

	@Override
	public ResponseEntity<?> getAllWherePriceIsAndMore(@RequestParam Double price) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'price' and 'price' more then defined.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getPrice() >= price) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

///////////////////// GET BY FLEXIBILE DATES /////////////////////////

	@Override
	public ResponseEntity<?> getAllWithFlexibileDays(@RequestParam boolean flexDays) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'flexibileDays'.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getFlexibileDays() == flexDays) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

///////////////////// GET BY STATUS /////////////////////////

	@Override
	public ResponseEntity<?> getAllWithStatus(@RequestParam Integer status) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'status'.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getStatus() == status) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

///////////////////// GET BY ELAPSE /////////////////////////

	@Override
	public ResponseEntity<?> getAllWithElapse(@RequestParam Integer elapse) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'elapse'.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getElapse() == elapse) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

///////////////////// GET BY CREATED BY /////////////////////////

	@Override
	public ResponseEntity<?> getAllByCreatedBy(@RequestParam Integer createdBy) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'createdBy'.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getCreatedById() == createdBy) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

///////////////////// GET BY CREATED BY /////////////////////////

	@Override
	public ResponseEntity<?> getAllByUpdatedBy(@RequestParam Integer updatedBy) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'updatedBy'.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getUpdatedById() == updatedBy) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

///////////////////// GET BY VERSION /////////////////////////

	@Override
	public ResponseEntity<?> getAllByVersion(@RequestParam Integer version) {
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
		try {
			List<JobSeekEntity> wantedJobSeeks = new ArrayList<JobSeekEntity>();
			logger.info("Looking for jobs defined with 'version'.");
			for (JobSeekEntity i : jobSeekRepository.findAll()) {
				if (i.getVersion() == version) {
					wantedJobSeeks.add(i);
				}
			}
			if (!wantedJobSeeks.isEmpty()) {
				logger.info("Returning JobSeeks.");
				return new ResponseEntity<List<JobSeekEntity>>(wantedJobSeeks, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'.", HttpStatus.BAD_REQUEST);
		}

		logger.info("JobSeek that you asked for doesn't exist.");
		return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
	}

	////////////////// DELETE ////////////////////////////

	@Override
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
				logger.info("Deleting entity.");
				wantedJobSeek.setStatusInactive();
				// applyContactDaoImp.markApplyAsExpiredBySeek(wantedJobSeek); //DODAO ZA NIDZU
				logger.info("Entity deleted.");
			} else if (wantedJobSeek.getStatus().equals(0)) {
				logger.info("JobSeek status is already deleted.");
				return new ResponseEntity<String>("JobSeek status is already deleted.", HttpStatus.OK);
			} else if (wantedJobSeek.getStatus().equals(-1)) {
				logger.info("JobSeek is arhived it can't be deleted or undeleted.");
				return new ResponseEntity<String>("JobSeek is arhived it can't be deleted or undeleted.",
						HttpStatus.OK);
			} else {
				logger.info("JobSeek has unknown status, check status in datebase for jobSeek.");
				return new ResponseEntity<String>("JobSeek has unknown status, check status in datebase for jobSeek.",
						HttpStatus.OK);
			}
			logger.info("Saveing entity.");
			jobSeekRepository.save(wantedJobSeek);
			logger.info("jobSeek changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Deleting jobSeek.'");
			return new ResponseEntity<String>("Error occured during 'Deleting jobSeek'." + e, HttpStatus.BAD_REQUEST);
		}
		logger.info("Returning jobSeek.");
		return new ResponseEntity<JobSeekEntity>(wantedJobSeek, HttpStatus.OK);
	}

	////////////////////// UNDELETE //////////////////////////////

	@Override
	public ResponseEntity<?> unDeleteById(@PathVariable Integer id) {
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
				logger.info("JobSeek that you asked for doesn't exist.");
				return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		try {
			logger.info("Changing activity.");
			if (wantedJobSeek.getStatus().equals(0)) {
				logger.info("Undeleteing entity.");
				wantedJobSeek.setStatusActive();
				logger.info("Entity deleted.");
			} else if (wantedJobSeek.getStatus().equals(1)) {
				logger.info("JobSeek status is already activ.");
				return new ResponseEntity<String>("JobSeek status is already activ.", HttpStatus.OK);
			} else if (wantedJobSeek.getStatus().equals(-1)) {
				logger.info("JobSeek is arhived it can't be deleted or undeleted.");
				return new ResponseEntity<String>("JobSeek is arhived it can't be deleted or undeleted.",
						HttpStatus.OK);
			} else {
				logger.info("JobSeek has unknown status, check status in datebase for jobSeek.");
				return new ResponseEntity<String>("JobSeek has unknown status, check status in datebase for jobSeek.",
						HttpStatus.OK);
			}
			logger.info("Saveing entity.");
			jobSeekRepository.save(wantedJobSeek);
			logger.info("jobSeek changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Undeleting jobSeek.'");
			return new ResponseEntity<String>("Error occured during 'Undeleting jobSeek'." + e, HttpStatus.BAD_REQUEST);
		}
		logger.info("Returning jobSeek.");
		return new ResponseEntity<JobSeekEntity>(wantedJobSeek, HttpStatus.OK);
	}

	////////////////////// ARCHIVE //////////////////////////

	@Override
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
				return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		try {
			logger.info("Changing activity.");
			if (wantedJobSeek.getStatus().equals(0) || wantedJobSeek.getStatus().equals(1)) {
				logger.info("Archiving entity.");
				wantedJobSeek.setStatusArchived();
				// applyContactDaoImp.markApplyAsExpiredBySeek(wantedJobSeek); // DODAO ZA NIDZU
				logger.info("Entity archived.");
			} else if (wantedJobSeek.getStatus().equals(-1)) {
				logger.info("JobSeek status is already archived.");
				return new ResponseEntity<String>("JobSeek status is already archived.", HttpStatus.OK);
			} else {
				logger.info("JobSeek has unknown status, check status in datebase for jobSeek.");
				return new ResponseEntity<String>("JobSeek has unknown status, check status in datebase for jobSeek.",
						HttpStatus.OK);
			}
			logger.info("Saveing entity.");
			jobSeekRepository.save(wantedJobSeek);
			logger.info("jobSeek changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Archiveing jobSeek.'");
			return new ResponseEntity<String>("Error occured during 'Archiveing jobSeek'." + e, HttpStatus.BAD_REQUEST);
		}
		logger.info("Returning jobSeek.");
		return new ResponseEntity<JobSeekEntity>(wantedJobSeek, HttpStatus.OK);
	}

	///////////////////// UNARCHIVE ///////////////////////////////

	@Override
	public ResponseEntity<?> unArchiveById(@PathVariable Integer id) {
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
				logger.info("JobSeek that you asked for doesn't exist.");
				return new ResponseEntity<String>("JobSeek that you asked for doesn't exist.", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.info("Error occured during 'Checking database'.");
			return new ResponseEntity<String>("Error occured during 'Checking database'." + e, HttpStatus.BAD_REQUEST);
		}
		try {
			logger.info("Changing activity.");
			if (wantedJobSeek.getStatus().equals(-1)) {
				logger.info("Unarchiving entity.");
				wantedJobSeek.setStatusActive();
				logger.info("Entity unarchived.");
			} else if (wantedJobSeek.getStatus().equals(1) || wantedJobSeek.getStatus().equals(0)) {
				logger.info("JobSeek status is already active.");
				return new ResponseEntity<String>("JobSeek status is already active.", HttpStatus.OK);
			} else {
				logger.info("JobSeek has unknown status, check status in datebase for jobSeek.");
				return new ResponseEntity<String>("JobSeek has unknown status, check status in datebase for jobSeek.",
						HttpStatus.OK);
			}
			logger.info("Saveing entity.");
			jobSeekRepository.save(wantedJobSeek);
			logger.info("jobSeek changed.");
		} catch (Exception e) {
			logger.info("Error occured during 'Unarchiveing jobSeek.'");
			return new ResponseEntity<String>("Error occured during 'Unarchiveing jobSeek'." + e,
					HttpStatus.BAD_REQUEST);
		}
		logger.info("Returning jobSeek.");
		return new ResponseEntity<JobSeekEntity>(wantedJobSeek, HttpStatus.OK);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

}
