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
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobDayHoursDTO;
import com.iktpreobuka.jobster.entities.dto.JobOfferDTO;
import com.iktpreobuka.jobster.enumerations.EDay;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.JobDayHoursRepository;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.JobTypeRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;

@Service
public class JobOfferDaoImpl implements JobOfferDao {

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
	private JobOfferRepository jobOfferRepository;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

/////////////////// PRAZAN SEEK BEZ DAYANDHOURS///////////////////////

	@Override
	public JobOfferEntity emptyJobOfferEntity() {
		return new JobOfferEntity();
	}
//////////////////////////// POST /////////////////////////////////

	@Override
	public ResponseEntity<?> addNewOffer(@Valid @RequestBody JobOfferDTO offer, Principal principal,
			BindingResult result) {

		logger.info("Starting addNewOffer(). -------------------");
		JobOfferEntity newOffer = new JobOfferEntity();

		boolean offerSaved = false;
		boolean dayAndHoursSaved = false;
		List<JobDayHoursEntity> listJobDayHoursEntity = new ArrayList<JobDayHoursEntity>();

		try {

			if (principal.getName() == null) {
				logger.info("Error in geting userName.");
				return new ResponseEntity<String>("Error in geting userName.", HttpStatus.UNAUTHORIZED);
			}
			// Checking does entry data exist

			logger.info("Checking does entry data exist.'");
			if (offer == null) {
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

			if (offer.getCityName() == null || offer.getJobTypeName() == null || offer.getNumberOfEmployees() == null
					|| offer.getBeginningDate() == null || offer.getEndDate() == null || offer.getPrice() == null
					|| offer.getDetailsLink() == null || offer.getListJobDayHoursDto() == null
					|| offer.getCountryName() == null || offer.getIso2Code() == null || offer.getLongitude() == null
					|| offer.getLatitude() == null) {
				logger.info("Some atributes are null.");
				return new ResponseEntity<String>("Some atributes are null", HttpStatus.BAD_REQUEST);
			}
			if (offer.getCityName().equals(" ") || offer.getJobTypeName().equals(" ")
					|| offer.getNumberOfEmployees().equals(" ") || offer.getBeginningDate().equals(" ")
					|| offer.getEndDate().equals(" ") || offer.getPrice().equals(" ")
					|| offer.getDetailsLink().equals(" ") || offer.getListJobDayHoursDto().equals(" ")
					|| offer.getCountryName().equals(" ") || offer.getIso2Code().equals(" ")
					|| offer.getLongitude().equals(" ") || offer.getLatitude().equals("")
					|| offer.getCityName().equals("") || offer.getJobTypeName().equals("")
					|| offer.getNumberOfEmployees().equals("") || offer.getBeginningDate().equals("")
					|| offer.getEndDate().equals("") || offer.getPrice().equals("") || offer.getDetailsLink().equals("")
					|| offer.getListJobDayHoursDto().equals("") || offer.getCountryName().equals("")
					|| offer.getIso2Code().equals("") || offer.getLongitude().equals("")
					|| offer.getLatitude().equals("")) {
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
			if (userAccountRepository.findByUsername(principal.getName()) == null) {
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
			if (userAccountRepository.findByUsernameAndStatusLike(principal.getName(), 1).getUser() == null) {
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

/////////////////// DEO KODA MI NIJE JASAN ///////////// receno mi je samo da ubacim

			CityEntity city = new CityEntity();

			logger.info("Checking database for city.");
			if ((offer.getCityName() != null) || (!offer.getCityName().equals(""))
					|| (!offer.getCityName().equals(" "))) {
				if (((cityRepository.count()) == 0) || (cityRepository.getByCityName(offer.getCityName()) == null)) {
					//////// ODAVDE mi nije jasno
					logger.info("Creating city.");
					city = cityDao.addNewCityWithLoggedUser(offer.getCityName(), offer.getLongitude(),
							offer.getLatitude(), offer.getCountryRegionName(), offer.getCountryName(),
							offer.getIso2Code(), loggedUser);
					///////// DOVDE
				} else {
					city = cityRepository.getByCityName(offer.getCityName());
				}
			}
			logger.info("OK");

			// checking jobType

			if ((offer.getJobTypeName() != null) || (!offer.getJobTypeName().equals(""))
					|| (!offer.getJobTypeName().equals(" "))) {
				logger.info("Checking database for jobType.");
				if ((jobTypeRepository.count() == 0)
						|| (jobTypeRepository.getByJobTypeName(offer.getJobTypeName())) == null) {
					logger.info("JobType doesn't exist.");
					return new ResponseEntity<String>("JobType doesn't exist.", HttpStatus.BAD_REQUEST);
				}
			}

			// checking size of list of checked days, and are there duplicate days, and

			List<JobDayHoursDTO> listJobDayHoursPostDto = new ArrayList<JobDayHoursDTO>();
			listJobDayHoursPostDto = offer.getListJobDayHoursDto();

			if (listJobDayHoursPostDto.size() > 0) {
				logger.info("Checking size of list of checked days and are there duplicate days.");
				if (listJobDayHoursPostDto.size() > 7) {
					logger.info("List can contain 7 elements max.");
					return new ResponseEntity<String>("List can contain 7 elements max.", HttpStatus.BAD_REQUEST);
				}

				Integer monday = 0;
				Integer tuesday = 0;
				Integer wednesday = 0;
				Integer thursday = 0;
				Integer friday = 0;
				Integer saturday = 0;
				Integer sunday = 0;

				for (JobDayHoursDTO i : listJobDayHoursPostDto) {
					if (i.getFlexibileHours() == true && i.getIsMinMax() == true) {
						return new ResponseEntity<String>("You need to decide between 'flexibile hours' and 'MinMax'.",
								HttpStatus.BAD_REQUEST);
					}
					if (i.getFlexibileHours() == false && i.getIsMinMax() == false) {
						return new ResponseEntity<String>("You need to decide between 'flexibile hours' and 'MinMax'.",
								HttpStatus.BAD_REQUEST);
					}
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

						logger.info("Mapping atributs for JobOffer.");
						newOffer.setEmployer(loggedUser);
						newOffer.setCity(city);
						newOffer.setType(jobTypeRepository.getByJobTypeName(offer.getJobTypeName()));
						newOffer.setNumberOfEmployees(offer.getNumberOfEmployees());
						newOffer.setBeginningDate(offer.getBeginningDate());
						newOffer.setEndDate(offer.getEndDate());
						newOffer.setFlexibileDates(offer.getFlexibileDates());
						newOffer.setPrice(offer.getPrice());
						newOffer.setDetailsLink(offer.getDetailsLink());
						newOffer.setFlexibileDays(offer.getFlexibileDays());
						newOffer.setStatusActive();
						newOffer.setElapseActive();
						newOffer.setVersion(1);
						newOffer.setCreatedById(loggedUser.getId());
						logger.info("Saveing JobOffer to database.");
						newOffer = jobOfferRepository.save(newOffer);
						logger.info("OK");
						offerSaved = true;

						logger.info("Mapping days and hours.");
						Integer numberOfDays = 0;
						for (JobDayHoursDTO i : listJobDayHoursPostDto) {
							JobDayHoursEntity newDayAndHours = new JobDayHoursEntity();
							newDayAndHours.setDay(i.getDay());
							newDayAndHours.setFromHour(i.getFromHour());
							newDayAndHours.setToHour(i.getToHour());
							newDayAndHours.setFlexibileHours(i.getFlexibileHours());
							newDayAndHours.setIsMinMax(i.getIsMinMax());
							newDayAndHours.setStatusActive();
							newDayAndHours.setVersion(1);
							newDayAndHours.setOffer(newOffer);
							logger.info("Saveing day and hours for day" + i.getDay());
							jobDayHoursRepository.save(newDayAndHours);
							listJobDayHoursEntity.add(newDayAndHours);
							logger.info("OK");
							dayAndHoursSaved = true;
							numberOfDays++;
						}
						logger.info("There is " + numberOfDays + " day(s) added.");
						logger.info("Days and hours mapped.");
						logger.info("OK");
						logger.info("Atributs for JobOffer mapped.");

		} catch (Exception e) {
			logger.info("Error occurred and data that has been previously added needs to be removed from database.");
			if (offerSaved == true) {
				if (jobOfferRepository.findById(newOffer.getId()) != null) {
					jobOfferRepository.delete(newOffer);
					logger.info("Job Offer that has been previously added removed from database.");
				}
			}
			if (dayAndHoursSaved == true) {
				if (!(listJobDayHoursEntity.isEmpty())) {
					for (JobDayHoursEntity i : listJobDayHoursEntity) {
						if (jobDayHoursRepository.findById(i.getId()) != null) {
							jobDayHoursRepository.delete(i);
							logger.info("JobDayHours that has been previously added removed from database.");
						}
					}
				}
			}
			return new ResponseEntity<String>("Error occurrred.------------------- " + e.getMessage() + " " + e,
					HttpStatus.BAD_REQUEST);
		}

		logger.info("Returning new jobOffer.---------------");
		return new ResponseEntity<JobOfferEntity>(newOffer, HttpStatus.OK);
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
