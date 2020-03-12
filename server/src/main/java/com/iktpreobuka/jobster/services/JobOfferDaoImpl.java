package com.iktpreobuka.jobster.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

import com.iktpreobuka.jobster.entities.ApplyContactEntity;
import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.JobDayHoursEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.UserAccountEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.JobDayHoursDTO;
import com.iktpreobuka.jobster.entities.dto.JobOfferDTO;
import com.iktpreobuka.jobster.entities.dto.JobOfferPutDTO;
import com.iktpreobuka.jobster.enumerations.EDay;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.JobDayHoursRepository;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.JobTypeRepository;
import com.iktpreobuka.jobster.repositories.UserAccountRepository;

@Service
public class JobOfferDaoImpl implements JobOfferDao {

	@PersistenceContext
	EntityManager em;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private CountryRegionRepository countryRegionRepository;

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

	@SuppressWarnings("unlikely-arg-type")
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
			logger.info("OK");

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
//					if (i.getFlexibileHours() == true && i.getIsMinMax() == true) {
//						return new ResponseEntity<String>("You need to decide between 'flexibile hours' and 'MinMax'.",
//								HttpStatus.BAD_REQUEST);
//					}
//					if (i.getFlexibileHours() == false && i.getIsMinMax() == false) {
//						return new ResponseEntity<String>("You need to decide between 'flexibile hours' and 'MinMax'.",
//								HttpStatus.BAD_REQUEST);
//					}
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
			
			// Checking does dates match
			
	        Date begining = offer.getBeginningDate();
	        Date end = offer.getEndDate();
	        Date created = Calendar.getInstance().getTime();
	        
	        Calendar c = new GregorianCalendar();
	        c.setTime(created);
	        c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            created = c.getTime();
			
			logger.info("Checking does dates match.");
			if(begining.compareTo(end) >= 0) {
				logger.info("Beginning date must be younger than end date.");
				return new ResponseEntity<String>("Beginning date must be younger than end date.", HttpStatus.BAD_REQUEST);
			}
			if(begining.compareTo(created) < 0) {
				logger.info("Beginning date must be todays or future date.");
				return new ResponseEntity<String>("Beginning date must be todays or future date.", HttpStatus.BAD_REQUEST);
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
			newOffer.setDateCreated(Calendar.getInstance().getTime());
			newOffer.setFlexibileDates(offer.getFlexibileDates());
			newOffer.setPrice(offer.getPrice());
			newOffer.setDetailsLink(offer.getDetailsLink());
			newOffer.setFlexibileDays(offer.getFlexibileDays());
			newOffer.setCounterOffer(false);
			newOffer.setStatusActive();
			newOffer.setExpired(false);
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
	
	
///////////////////////////// PUT ////////////////////////////
	
	@Override
	public ResponseEntity<?> modifyOffer(@Valid JobOfferPutDTO offer, Integer offerId, Principal principal,
			BindingResult result) {
		
		logger.info("Starting modifyOffer().---------------------");

		JobOfferEntity offerForModify = new JobOfferEntity();

		JobOfferEntity copyOfOriginalJobOfferEntity = new JobOfferEntity();

		boolean offerSaved = false;

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

			if (offer.getDetailsLink() == null) {
				logger.info("DetailsLink are null.");
				return new ResponseEntity<String>("DetailsLink are null.", HttpStatus.BAD_REQUEST);
			}
			if (offer.getDetailsLink().equals(" ") || offer.getDetailsLink().equals("")) {
				logger.info("DetailsLink are blanks.");
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

// checking offerForModify

			logger.info("Looking for jobOffer you want to change.");

			logger.info("Checking jobOffer database.");
			if (jobOfferRepository.count() == 0) {
				logger.info("JobOffer database empty.");
				return new ResponseEntity<String>("JobOffer database empty.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking does jobOffer exist.");
			if (jobOfferRepository.findById(offerId) == null) {
				logger.info("JobOffer doesn't exist.");
				return new ResponseEntity<String>("JobOffer doesn't exist.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking is jobOffer deleted.");
			if (!(jobOfferRepository.findByIdAndStatusLike(offerId, 0) == null)) {
				logger.info("JobOffer is deleted.");
				return new ResponseEntity<String>("JobOffer is deleted.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking is jobOffer archived.");
			if (!(jobOfferRepository.findByIdAndStatusLike(offerId, -1) == null)) {
				logger.info("JobOffer is archived.");
				return new ResponseEntity<String>("JobOffer is archived.", HttpStatus.BAD_REQUEST);
			} else {
				logger.info("OK");
				offerForModify = jobOfferRepository.findById(offerId).orElse(null);
				if (offerForModify == null) {
					logger.info("JobOffer that you asked for doesn't exist.");
					return new ResponseEntity<String>("JobOffer that you asked for doesn't exist.",
							HttpStatus.BAD_REQUEST);
				}
			}

// Making copy of offerForModify

			copyOfOriginalJobOfferEntity = offerForModify;

// Mapping atributs

			logger.info("Checking are details changed.");
			if (!(offer.getDetailsLink().equals(offerForModify.getDetailsLink()))) {
				logger.info("Mapping atributs.");
				offerForModify.setDetailsLink(offer.getDetailsLink());
				logger.info("Setting update details.");
				offerForModify.setUpdatedById(loggedUser.getId());
				offerForModify.setDateUpdated(Calendar.getInstance().getTime());
				logger.info("Update details set.");
				logger.info("Details changed");
				logger.info("Saveing JobOffer.");
				jobOfferRepository.save(offerForModify);
				offerSaved = true;
				logger.info("Atributs mapped.");
			}
			
		} catch (Exception e) {
			logger.info("Error occurred and data that has been previously added needs to be removed from database.");
			if (offerSaved == true) {
				if (jobOfferRepository.findById(offerForModify.getId()) != null) {
					offerForModify = copyOfOriginalJobOfferEntity;
					jobOfferRepository.save(offerForModify);
					logger.info("Job Offer that has been previously modified has been returned to previus state.");
				}
			}
			return new ResponseEntity<String>("Error occurrred.------------------- " + e.getMessage() + " " + e,
					HttpStatus.BAD_REQUEST);

		}
		logger.info("Returning new jobOffer.");
		return new ResponseEntity<JobOfferEntity>(offerForModify, HttpStatus.OK);
	}

///////////////////////////// PUT STARA METODA/////////////////////////////

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public ResponseEntity<?> modifyOfferStaraMetoda(@Valid @RequestBody JobOfferDTO offer, @PathVariable Integer offerId,
			Principal principal, BindingResult result) {

		logger.info("Starting modifyOffer().-------------------");

		JobOfferEntity offerForModify = new JobOfferEntity();

		JobDayHoursEntity newDayToCreate = new JobDayHoursEntity();

		JobOfferEntity copyOfOriginalJobOfferEntity = new JobOfferEntity();

		List<JobDayHoursEntity> copyOfOriginalListOfJobDayHoursEntitys = new ArrayList<JobDayHoursEntity>();

		boolean offerSaved = false;
		boolean dayAndHoursSaved = false;

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

			// checking offerForModify

			logger.info("Looking for jobOffer you want to change.");

			logger.info("Checking jobOffer database.");
			if (jobOfferRepository.count() == 0) {
				logger.info("JobOffer database empty.");
				return new ResponseEntity<String>("JobOffer database empty.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking does jobOffer exist.");
			if (jobOfferRepository.findById(offerId) == null) {
				logger.info("JobOffer doesn't exist.");
				return new ResponseEntity<String>("JobOffer doesn't exist.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking is jobOffer deleted.");
			if (!(jobOfferRepository.findByIdAndStatusLike(offerId, 0) == null)) {
				logger.info("JobOffer is deleted.");
				return new ResponseEntity<String>("JobOffer is deleted.", HttpStatus.BAD_REQUEST);
			}
			logger.info("OK");
			logger.info("Checking is jobOffer archived.");
			if (!(jobOfferRepository.findByIdAndStatusLike(offerId, -1) == null)) {
				logger.info("JobOffer is archived.");
				return new ResponseEntity<String>("JobOffer is archived.", HttpStatus.BAD_REQUEST);
			} else {
				logger.info("OK");
				offerForModify = jobOfferRepository.findById(offerId).orElse(null);
				if (offerForModify == null) {
					logger.info("JobOffer that you asked for doesn't exist.");
					return new ResponseEntity<String>("JobOffer that you asked for doesn't exist.",
							HttpStatus.BAD_REQUEST);
				}
			}

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
			logger.info("OK");

			// Making copy of offerForModify and list of JobDayHours that offerForModify has

			copyOfOriginalJobOfferEntity = offerForModify;
			copyOfOriginalListOfJobDayHoursEntitys = offerForModify.getDaysAndHours();

			// Mapping atributs

			logger.info("Mapping atributs.");
			// offerForModify.setEmployee(loggedUser);

			logger.info("Checking is city changed.");
			if (((offer.getCityName() != null) || (!offer.getCityName().equals(""))
					|| (!offer.getCityName().equals(" ")))
					&& (cityRepository.getByCityName(offer.getCityName()) != offerForModify.getCity())) {
				offerForModify.setCity(city);
				logger.info("City changed.");
			}
			logger.info("Checking is job type name changed.");
			if (((offer.getJobTypeName() != null) || (!offer.getJobTypeName().equals(""))
					|| (!offer.getJobTypeName().equals(" ")))
					&& (!offer.getJobTypeName().equalsIgnoreCase(offerForModify.getType().getJobTypeName()))) {
				offerForModify.setType(jobTypeRepository.getByJobTypeName(offer.getJobTypeName()));
				logger.info("Job type name changed.");
			}
			logger.info("Checking is distance to job changed.");
			if (((offer.getNumberOfEmployees() != null) || (!offer.getNumberOfEmployees().equals(""))
					|| (!offer.getNumberOfEmployees().equals(" ")))
					&& (offer.getNumberOfEmployees() != offerForModify.getNumberOfEmployees())) {
				offerForModify.setNumberOfEmployees(offer.getNumberOfEmployees());
				logger.info("Distance to job changed.");
			}
			/////////////// ovo popraviti (registruje promenu i kada nema promene izmedju
			/////////////// JSON-a koje se koristio za POST i JSON-a koji se koristio za
			/////////////// PUT)
			logger.info("Checking is beginning date changed.");
			if (((offer.getBeginningDate() != null) || (!offer.getBeginningDate().equals(""))
					|| (!offer.getBeginningDate().equals(" ")))
					&& (offer.getBeginningDate() != offerForModify.getBeginningDate())) {
				offerForModify.setBeginningDate(offer.getBeginningDate());
				logger.info("Beginning date changed.");
			}
			/////////////// ovo popraviti (registruje promenu i kada nema promene izmedju
			/////////////// JSON-a koje se koristio za POST i JSON-a koji se koristio za
			/////////////// PUT)
			logger.info("Checking is end date changed.");
			if (((offer.getEndDate() != null) || (!offer.getEndDate().equals("")) || (!offer.getEndDate().equals(" ")))
					&& (offer.getEndDate() != offerForModify.getEndDate())) {
				offerForModify.setEndDate(offer.getEndDate());
				logger.info("End date changed.");
			}
			logger.info("Checking is flexibility for dates changed.");
			if (((offer.getFlexibileDates() != null) || (!offer.getFlexibileDates().equals(""))
					|| (!offer.getFlexibileDates().equals(" ")))
					&& (offer.getFlexibileDates() != offerForModify.getFlexibileDates())) {
				offerForModify.setFlexibileDates(offer.getFlexibileDates());
				logger.info("Flexibility for dates changed.");
			}
			/////////////// ovo popraviti (registruje promenu i kada nema promene izmedju
			/////////////// JSON-a koje se koristio za POST i JSON-a koji se koristio za
			/////////////// PUT)
			logger.info("Checking is price changed.");
			if (((offer.getPrice() != null) || (!offer.getPrice().equals("")) || (!offer.getPrice().equals(" ")))
					&& (offer.getPrice() != offerForModify.getPrice())) {
				offerForModify.setPrice(offer.getPrice());
				logger.info("Price changed.");
			}
			/////////////// ovo popraviti (registruje promenu i kada nema promene izmedju
			/////////////// JSON-a koje se koristio za POST i JSON-a koji se koristio za
			/////////////// PUT)
			logger.info("Checking are details changed.");
			if (((offer.getDetailsLink() != null) || (!offer.getDetailsLink().equals(""))
					|| (!offer.getDetailsLink().equals(" ")))
					&& (offer.getDetailsLink().equals(offerForModify.getDetailsLink()))) {
				offerForModify.setDetailsLink(offer.getDetailsLink());
				logger.info("Details changed");
			}
			logger.info("Checking is flexibility for days changed.");
			if (((offer.getFlexibileDays() != null) || (!offer.getFlexibileDays().equals(""))
					|| (!offer.getFlexibileDays().equals(" ")))
					&& (offer.getFlexibileDays() != offerForModify.getFlexibileDays())) {
				offerForModify.setFlexibileDays(offer.getFlexibileDays());
				logger.info("Flexibility for days changed.");
			}
			logger.info("Setting update details.");
			offerForModify.setUpdatedById(loggedUser.getId());
			offerForModify.setDateCreated(Calendar.getInstance().getTime());
			logger.info("Update details set.");

///////////// Checking NEW list for jobDayHours

			// --------------------- NOVA LISTA -------------------------------
			List<JobDayHoursDTO> listJobDayHoursDto = new ArrayList<JobDayHoursDTO>();

			listJobDayHoursDto = offer.getListJobDayHoursDto();

			logger.info("Checking is list for days and hours empty.");
			if (listJobDayHoursDto.size() > 0) {
				logger.info("List has elements.");

				logger.info("Checking number of elements of list.");
				if (listJobDayHoursDto.size() > 7) {
					logger.info("You can choose 7 days max.");
					return new ResponseEntity<String>("You can choose 7 days max.", HttpStatus.BAD_REQUEST);
				}
				logger.info("OK.");

				Integer monday = 0;
				Integer tuesday = 0;
				Integer wednesday = 0;
				Integer thursday = 0;
				Integer friday = 0;
				Integer saturday = 0;
				Integer sunday = 0;

				logger.info("Checking input data for daysAndHours.");
				for (JobDayHoursDTO i : listJobDayHoursDto) {
					if ((i.getDay() == null) || (i.getDay().equals("")) || (i.getDay().equals(" "))) {
						logger.info("Missing data. You need to put 'day'.");
						return new ResponseEntity<String>("You need to put 'day'.", HttpStatus.BAD_REQUEST);
					}
					if ((i.getFromHour() == null) || (i.getFromHour().equals("")) || (i.getFromHour().equals(" "))) {
						logger.info("Missing data. You need to put 'from hour'.");
						return new ResponseEntity<String>("You need to put 'from hour'.", HttpStatus.BAD_REQUEST);
					}
					if ((i.getToHour() == null) || (i.getToHour().equals("")) || (i.getToHour().equals(" "))) {
						logger.info("Missing data. You need to put 'to hour'.");
						return new ResponseEntity<String>("You need to put 'to hour'.", HttpStatus.BAD_REQUEST);
					}
//					if (i.getFlexibileHours() == null && i.getIsMinMax() == null) {
//						return new ResponseEntity<String>("You need to decide between 'flexibile hours' and 'MinMax'.",
//								HttpStatus.BAD_REQUEST);
//					}
//					if (i.getFlexibileHours() == true && i.getIsMinMax() == true) {
//						return new ResponseEntity<String>("You need to decide between 'flexibile hours' and 'MinMax'.",
//								HttpStatus.BAD_REQUEST);
//					}
//					if (i.getFlexibileHours() == false && i.getIsMinMax() == false) {
//						return new ResponseEntity<String>("You need to decide between 'flexibile hours' and 'MinMax'.",
//								HttpStatus.BAD_REQUEST);
//					}
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

				// --------------------- STARA LISTA ------------------------------
				// provera da li je doslo do izmene sati ili drugih atributa za neki dan
				// ako jeste na stari entitet mapirati nove atribute
				// takodje provera i da li treba vec neki postojeci dan staviti da bude
				// neaktivan

				List<JobDayHoursEntity> oldListJobDayHours = new ArrayList<JobDayHoursEntity>();
				oldListJobDayHours = offerForModify.getDaysAndHours();

				boolean missing;
				for (JobDayHoursEntity i : oldListJobDayHours) {
					missing = true;
					for (JobDayHoursDTO j : listJobDayHoursDto) {
						if (i.getDay() == j.getDay()) {
							missing = false;
							i.setFromHour(j.getFromHour());
							i.setToHour(j.getToHour());
							i.setFlexibileHours(j.getFlexibileHours());
							i.setIsMinMax(j.getIsMinMax());
							i.setStatusActive();
							logger.info("Saving corected day." + i.getDay());
							jobDayHoursRepository.save(i);
							dayAndHoursSaved = true;
							logger.info("Corected day saved.");
						}
					}
					if (missing == true) {
						logger.info("Deleting unwanted day." + i.getDay());
						i.setStatusInactive();
						jobDayHoursRepository.save(i);
						dayAndHoursSaved = true;
						logger.info("Unwanted day deleted.");
					}
				}

				// --------------------- NOVA LISTA ------------------------------
				// provera da li ima neki novi dan koji treba napraviti
				// i ako ima pravljenje novog dana

				Integer difrenDayCount;
				Integer allDaysCount;
				for (JobDayHoursDTO z : listJobDayHoursDto) {
					difrenDayCount = 0;
					allDaysCount = 0;
					for (JobDayHoursEntity y : oldListJobDayHours) {
						allDaysCount++;
						if (z.getDay() != y.getDay()) {
							difrenDayCount++;
						}
					}
					if (difrenDayCount == allDaysCount) {
						newDayToCreate.setDay(z.getDay());
						newDayToCreate.setFromHour(z.getFromHour());
						newDayToCreate.setToHour(z.getToHour());
						newDayToCreate.setFlexibileHours(z.getFlexibileHours());
						newDayToCreate.setIsMinMax(z.getIsMinMax());
						newDayToCreate.setStatusActive();
						newDayToCreate.setOffer(offerForModify);
						logger.info("Saving new created day." + z.getDay());
						jobDayHoursRepository.save(newDayToCreate);
						dayAndHoursSaved = true;
						logger.info("New created day saved.");
					}
				}
				logger.info("Saveing JobOffer.");
				jobOfferRepository.save(offerForModify);
				offerSaved = true;
				logger.info("Atributs mapped.");
			}
		} catch (Exception e) {
			logger.info("Error occurred and data that has been previously added needs to be removed from database.");
			if (dayAndHoursSaved == true) {
				logger.info("Correcting change JobDaysAndHours.");
				// ovaj array bi mozda mogao praviti problem
				Integer[] ids = new Integer[6];
				Integer position = 0;
				if (!(offerForModify.getDaysAndHours().isEmpty())) {
					for (JobDayHoursEntity i : offerForModify.getDaysAndHours()) {
						for (JobDayHoursEntity j : copyOfOriginalListOfJobDayHoursEntitys) {
							if (i.getId().equals(j.getId())) {
								i = j;
								jobDayHoursRepository.save(i);
								logger.info(position + 1 + " jobDayAndHours has been returned to previous state");
								ids[position] = i.getId();
								position++;
							}
						}
					}
					logger.info("Deleting added JobDaysAndHours.");
					JobDayHoursEntity jobDayHoursForDelete = new JobDayHoursEntity();
					for (Integer x = 0; x <= ids.length; x++) {
						Integer countForCurcles = 0;
						Integer countDifrentIds = 0;
						for (JobDayHoursEntity y : offerForModify.getDaysAndHours()) {
							jobDayHoursForDelete = y;
							countForCurcles++;
							// ovde bi mozda array mogao da napravi problem
							if (ids[x] != null && ids[x] != y.getId()) {
								countDifrentIds++;
							}
						}
						if (countForCurcles == countDifrentIds) {
							jobDayHoursRepository.delete(jobDayHoursForDelete);
							logger.info(x + 1 + " jobDayAndHours has been deleted");
						}
					}
				}
			}
			if (offerSaved == true) {
				if (jobOfferRepository.findById(offerForModify.getId()) != null) {
					offerForModify = copyOfOriginalJobOfferEntity;
					jobOfferRepository.save(offerForModify);
					logger.info("Job Offer that has been previously modified has been returned to previus state.");
				}
			}
			return new ResponseEntity<String>("Error occurrred.------------------- " + e.getMessage() + " " + e,
					HttpStatus.BAD_REQUEST);

		}
		logger.info("Returning new jobOffer.");
		return new ResponseEntity<JobOfferEntity>(offerForModify, HttpStatus.OK);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> findByQuery(UserEntity loggedUser, List<JobDayHoursDTO> jobDayHours, Integer employerId, 
			String cityName, String countryRegionName, String countryName, Integer distance,
			Integer typeId, Date beginningDate, Date endDate, Boolean flexibileDates, Double price, 
			Boolean flexibileDays) throws Exception {
		
		logger.info("++++++++++++++++ Service for finding JobSeeks");
		String sql = "select DISTINCT jo from JobOfferEntity jo join jo.daysAndHours dh join jo.type t join jo.city c join jo.employer e where jo.status =1 and jo.expired = false and dh.status = 1 and t.status = 1 and c.status = 1 and e.status = 1" + 
					" AND jo.id  NOT IN (select ro.rejectedOffer.id from RejectOfferEntity ro where ro.user.id = " + loggedUser.getId() + ")" + 
					" AND jo.id  NOT IN (select ac.offer.id from ApplyContactEntity ac where ac.seek.employee.id = " + loggedUser.getId() + " AND ac.seek.counterSeek = true AND ac.offer.counterOffer = false)";
		logger.info("++++++++++++++++ Basic query created");
				
		if (employerId != null) {
			sql = sql + " and jo.employer.id = " + employerId;
			logger.info("++++++++++++++++ Added condition for employer applications");
		}
		
		if (cityName != null && countryName != null) {
			CityEntity city = new CityEntity();
			CountryEntity country = new CountryEntity();
			CountryRegionEntity countryRegion = new CountryRegionEntity();
			try {
				country = countryRepository.findByCountryNameIgnoreCase(countryName);
			} catch (Exception e) {
				throw new Exception("CountryRepository failed.");
			}
			if (country == null) {
				logger.info("Country doesn't exist in database.");
				return new ResponseEntity<String>("Country doesn't exist in database.", HttpStatus.NOT_FOUND);
			}
			try {
				countryRegion = countryRegionRepository.findByCountryRegionNameAndCountry(countryRegionName, country);
			} catch (Exception e) {
				throw new Exception("CountryRegionRepository failed.");
			}
			if (countryRegion == null) {
				logger.info("Country region doesn't exist in database.");
				return new ResponseEntity<String>("Country region doesn't exist in database.", HttpStatus.NOT_FOUND);
			}
			try {
				city = cityRepository.findByCityNameAndRegion(cityName, countryRegion);
			} catch (Exception e) {
				throw new Exception("CityRepository failed.");
			}
			if (city == null) {
				logger.info("City doesn't exist in database.");
				return new ResponseEntity<String>("City doesn't exist in database.", HttpStatus.NOT_FOUND);
			}
			logger.info("City founded.");
			sql = sql + " and (jo.city.id = " + city.getId() + " or " + city.getId() + " IN(select cd.fromCity.id from CityDistanceEntity cd where cd.kmDistance <= " + distance + " and cd.toCity.id = jo.city.id) or " + city.getId() + " IN(select cd.toCity.id from CityDistanceEntity cd where cd.kmDistance <= " + distance + " and cd.fromCity.id = jo.city.id))";
			logger.info("++++++++++++++++ Added condition for city applications");
		}
		
		if (typeId != null) {
			sql = sql + " and jo.type.id = " + typeId;
			logger.info("++++++++++++++++ Added condition for type applications");
		}
		
		if (flexibileDates != null && flexibileDates == true) {
			if (beginningDate != null && endDate != null) {
				sql = sql + " and ((jo.flexibileDates = true" +
						" and (('" + beginningDate + "' BETWEEN jo.beginningDate and jo.endDate and '" + endDate + "' BETWEEN jo.beginningDate and jo.endDate) or " +
						"('" + beginningDate + "' < jo.beginningDate and '" + endDate + "' BETWEEN jo.beginningDate and jo.endDate) or " +
						"('" + endDate + "' > jo.endDate and '" + beginningDate + "' BETWEEN jo.beginningDate and jo.endDate) or " +
						"(jo.beginningDate BETWEEN '" + beginningDate + "' and '" + endDate + "' and jo.endDate BETWEEN '" + beginningDate + "' and '" + endDate + "'))) or " +
						"(jo.flexibileDates = false" +
						" and jo.beginningDate BETWEEN '" + beginningDate + "' and '" + endDate + "' and jo.endDate BETWEEN '" + beginningDate + "' and '" + endDate + "'))";
				logger.info("++++++++++++++++ Added condition for beginningDate and endDate applications");
			} else if (beginningDate != null) {
				sql = sql + " and ((jo.flexibileDates = true" +
						" and '" + beginningDate + "' < jo.endDate) or " +
						"(jo.flexibileDates = false" +
						" and jo.beginningDate >= '" + beginningDate + "'))";
				logger.info("++++++++++++++++ Added condition for beginningDate applications");
			} else if (endDate != null) {
				sql = sql + " and ((jo.flexibileDates = true" +
						" and '" + endDate + "' > jo.beginningDate) or " +
						"(jo.flexibileDates = false" +
						" and jo.endDate <= '" + endDate + "'))";
				logger.info("++++++++++++++++ Added condition for endDate applications");
			}
			logger.info("++++++++++++++++ Added condition for TRUE flexibileDates applications");
		} else {
			if (beginningDate != null && endDate != null) {
				sql = sql + " and ((jo.flexibileDates = true" +
						" and '" + beginningDate + "' BETWEEN jo.beginningDate and jo.endDate and '" + endDate + "' BETWEEN jo.beginningDate and jo.endDate) or " +
						"(jo.flexibileDates = false" +
						" and jo.beginningDate = '" + beginningDate + "' and jo.endDate = '" + endDate + "'))";
				logger.info("++++++++++++++++ Added condition for beginningDate and endDate applications");
			} else if (beginningDate != null) {
				sql = sql + " and ((jo.flexibileDates = true" +
						" and '" + beginningDate + "' BETWEEN jo.beginningDate and jo.endDate) or " +
						"(jo.flexibileDates = false" +
						" and jo.beginningDate = '" + beginningDate + "'))";
				logger.info("++++++++++++++++ Added condition for beginningDate applications");
			} else if (endDate != null) {
				sql = sql + " and ((jo.flexibileDates = true" +
						" and '" + endDate + "' BETWEEN jo.beginningDate and jo.endDate) or " +
						"(jo.flexibileDates = false" +
						" and jo.endDate = '" + endDate + "'))";
				logger.info("++++++++++++++++ Added condition for endDate applications");
			}
			logger.info("++++++++++++++++ Added condition for FALSE flexibileDates applications");
		}
		
		if (price != null) {
			sql = sql + " and jo.price >= " + price;
			logger.info("++++++++++++++++ Added condition for price applications");
		}
		
		if (flexibileDays != null && flexibileDays == true) {
			Integer counter = 0;
			if (jobDayHours != null && !jobDayHours.isEmpty()) {
				sql = sql + " AND ((jo.flexibileDays = true and (";
				counter = jobDayHours.size();
				for (JobDayHoursDTO jdh : jobDayHours) {
					sql = sql + "(dh.day = '" + jdh.getDay() + "' and ((dh.isMinMax = " + jdh.getIsMinMax() + " and ";
					if (jdh.getFlexibileHours() != null && jdh.getFlexibileHours() == true) {
						sql = sql + "((dh.flexibileHours = true AND ((" +
								jdh.getFromHour() + " <= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " >= dh.toHour) OR (" +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour) OR (" +
								jdh.getFromHour() + " < dh.fromHour AND " +
								jdh.getToHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " >= dh.fromHour) OR (" +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " > dh.toHour))) OR " +
								"(dh.flexibileHours = false AND " +
								jdh.getFromHour() + " <= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " >= dh.toHour)))";								
					} else {
						sql = sql + "((dh.flexibileHours = true AND " +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " <= dh.toHour) OR " +
								"(dh.flexibileHours = false AND " +
								jdh.getFromHour() + " = dh.fromHour AND " +
								jdh.getToHour() + " = dh.toHour)))";
					}
					if (jdh.getIsMinMax() != null && jdh.getIsMinMax() == true && jdh.getFromHour() != null && jdh.getToHour() != null) {
						sql = sql + " OR (dh.isMinMax <> " + jdh.getIsMinMax() + " and dh.isMinMax = false";
						if (jdh.getFlexibileHours() != null && jdh.getFlexibileHours() == false) {
							sql = sql + " and " +
								jdh.getFromHour() + " <= (dh.toHour - dh.fromHour) AND " +
								jdh.getToHour() + " >= (dh.toHour - dh.fromHour)";
						}
						sql = sql + ")";
					} else if (jdh.getIsMinMax() != null && jdh.getIsMinMax() == false && jdh.getFromHour() != null && jdh.getToHour() != null) {
						sql = sql + " OR (dh.isMinMax <> " + jdh.getIsMinMax() + " and dh.isMinMax = true" +
								" and ((dh.flexibileHours = true) or (dh.flexibileHours = false and " +
								"dh.fromHour <= " + (jdh.getToHour() - jdh.getFromHour()) + " AND " +
								"dh.toHour >= " + (jdh.getToHour() - jdh.getFromHour()) + ")))";
					}	
					counter--;
					if (counter >= 1) {
						sql = sql + ")) OR ";
					} else {
						sql = sql + "))))";
					}
				}
				logger.info("++++++++++++++++ Added condition for TRUE flexibileDays, jobDayHours and TRUE jo.flexibileDays applications");

				sql = sql + " OR (jo.flexibileDays = false and (";
				counter = jobDayHours.size();
				for (JobDayHoursDTO jdh : jobDayHours) {
					sql = sql + "(dh.day = '" + jdh.getDay() + "' and ((dh.isMinMax = " + jdh.getIsMinMax() + " and ";
					if (jdh.getFlexibileHours() != null && jdh.getFlexibileHours() == true) {
						sql = sql + "((dh.flexibileHours = true AND ((" +
								jdh.getFromHour() + " <= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " >= dh.toHour) OR (" +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour) OR (" +
								jdh.getFromHour() + " < dh.fromHour AND " +
								jdh.getToHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " >= dh.fromHour) OR (" +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " > dh.toHour))) OR " +
								"(dh.flexibileHours = false AND " +
								jdh.getFromHour() + " <= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " >= dh.toHour)))";
					} else {
						sql = sql + "((dh.flexibileHours = true AND " +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " <= dh.toHour) OR " +
								"(dh.flexibileHours = false AND " +
								jdh.getFromHour() + " = dh.fromHour AND " +
								jdh.getToHour() + " = dh.toHour)))";
					}
					if (jdh.getIsMinMax() != null && jdh.getIsMinMax() == true && jdh.getFromHour() != null && jdh.getToHour() != null) {
						sql = sql + " OR (dh.isMinMax <> " + jdh.getIsMinMax() + " and dh.isMinMax = false";
						if (jdh.getFlexibileHours() != null && jdh.getFlexibileHours() == false) {
							sql = sql + " and " +
								jdh.getFromHour() + " <= (dh.toHour - dh.fromHour) AND " +
								jdh.getToHour() + " >= (dh.toHour - dh.fromHour)";
						}
						sql = sql + ")";
					} else if (jdh.getIsMinMax() != null && jdh.getIsMinMax() == false && jdh.getFromHour() != null && jdh.getToHour() != null) {
						sql = sql + " OR (dh.isMinMax <> " + jdh.getIsMinMax() + " and dh.isMinMax = true" +
								" and ((dh.flexibileHours = true) or (dh.flexibileHours = false and " +
								"dh.fromHour <= " + (jdh.getToHour() - jdh.getFromHour()) + " AND " +
								"dh.toHour >= " + (jdh.getToHour() - jdh.getFromHour()) + ")))";
					}	
					counter--;
					if (counter >= 1) {
						sql = sql + ")) OR ";
					} else {
						sql = sql + ")))))";
					}
				}
				logger.info("++++++++++++++++ Added condition for TRUE flexibileDays, jobDayHours and FALSE jo.flexibileDays applications");
				
			}

			sql = sql + " GROUP BY jo.id HAVING (COUNT(*) = (select COUNT(*) from JobDayHoursEntity jdh where jdh.status = 1 and jdh.offer.id = jo.id) AND jo.flexibileDays = false) OR (COUNT(*) > 0 AND jo.flexibileDays = true)";

			logger.info("++++++++++++++++ Added condition for TRUE flexibileDays applications");
				
		} else {
			Integer counter = 0;
			Boolean tt = jobDayHours != null;
			logger.info("+++++++" + tt.toString());
			if (jobDayHours != null && !jobDayHours.isEmpty()) {
				sql = sql + " AND ((jo.flexibileDays = true and (";
				counter = jobDayHours.size();
				for (JobDayHoursDTO jdh : jobDayHours) {
					sql = sql + "(dh.day = '" + jdh.getDay() + "' and ((dh.isMinMax = " + jdh.getIsMinMax() + " and ";
					if (jdh.getFlexibileHours() != null && jdh.getFlexibileHours() == true) {
						sql = sql + "((dh.flexibileHours = true AND ((" +
								jdh.getFromHour() + " <= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " >= dh.toHour) OR (" + 
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour) OR (" + 
								jdh.getFromHour() + " < dh.fromHour AND " +
								jdh.getToHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " >= dh.fromHour) OR (" + 
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " > dh.toHour))) OR " +
								"(dh.flexibileHours = false AND " +
								jdh.getFromHour() + " <= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " >= dh.toHour)))";
					} else {
						sql = sql + "((dh.flexibileHours = true AND " +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " <= dh.toHour) OR " +
								"(dh.flexibileHours = false AND " +
								jdh.getFromHour() + " = dh.fromHour AND " +
								jdh.getToHour() + " = dh.toHour)))";
					}
					if (jdh.getIsMinMax() != null && jdh.getIsMinMax() == true && jdh.getFromHour() != null && jdh.getToHour() != null) {
						sql = sql + " OR (dh.isMinMax <> " + jdh.getIsMinMax() + " and dh.isMinMax = false";
						if (jdh.getFlexibileHours() != null && jdh.getFlexibileHours() == false) {
							sql = sql + " and " +
								jdh.getFromHour() + " <= (dh.toHour - dh.fromHour) AND " +
								jdh.getToHour() + " >= (dh.toHour - dh.fromHour)";
						}
						sql = sql + ")";
					} else if (jdh.getIsMinMax() != null && jdh.getIsMinMax() == false && jdh.getFromHour() != null && jdh.getToHour() != null) {
						sql = sql + " OR (dh.isMinMax <> " + jdh.getIsMinMax() + " and dh.isMinMax = true" +
								" and ((dh.flexibileHours = true) or (dh.flexibileHours = false and " +
								"dh.fromHour <= " + (jdh.getToHour() - jdh.getFromHour()) + " AND " +
								"dh.toHour >= " + (jdh.getToHour() - jdh.getFromHour()) + ")))";
					}	
					counter--;
					if (counter >= 1) {
						sql = sql + ")) OR ";
					} else {
						sql = sql + "))))";
					}
				}
				logger.info("++++++++++++++++ Added condition for FALSE flexibileDays, jobDayHours and TRUE jo.flexibileDays applications");

				sql = sql + " OR (jo.flexibileDays = false and " + jobDayHours.size() + " = (select COUNT(*) from JobDayHoursEntity jdh where jdh.status = 1 and jdh.offer.id = jo.id) and (";
				counter = jobDayHours.size();
				for (JobDayHoursDTO jdh : jobDayHours) {
					sql = sql + "(dh.day = '" + jdh.getDay() + "' and ((dh.isMinMax = " + jdh.getIsMinMax() + " and ";
					if (jdh.getFlexibileHours() != null && jdh.getFlexibileHours() == true) {
						sql = sql + "((dh.flexibileHours = true AND ((" +
								jdh.getFromHour() + " <= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " >= dh.toHour) OR (" +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour) OR (" +
								jdh.getFromHour() + " < dh.fromHour AND " +
								jdh.getToHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " >= dh.fromHour) OR (" +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " <= dh.toHour AND " +
								jdh.getToHour() + " > dh.toHour))) OR " +
								"(dh.flexibileHours = false AND " +
								jdh.getFromHour() + " <= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " >= dh.toHour)))";
					} else {
						sql = sql + "((dh.flexibileHours = true AND " +
								jdh.getFromHour() + " >= dh.fromHour AND " +
								jdh.getFromHour() + " < dh.toHour AND " +
								jdh.getToHour() + " > dh.fromHour AND " +
								jdh.getToHour() + " <= dh.toHour) OR " +
								"(dh.flexibileHours = false AND " +
								jdh.getFromHour() + " = dh.fromHour AND " +
								jdh.getToHour() + " = dh.toHour)))";
					}
					if (jdh.getIsMinMax() != null && jdh.getIsMinMax() == true && jdh.getFromHour() != null && jdh.getToHour() != null) {
						sql = sql + " OR (dh.isMinMax <> " + jdh.getIsMinMax() + " and dh.isMinMax = false";
						if (jdh.getFlexibileHours() != null && jdh.getFlexibileHours() == false) {
							sql = sql + " and " +
								jdh.getFromHour() + " <= (dh.toHour - dh.fromHour) AND " +
								jdh.getToHour() + " >= (dh.toHour - dh.fromHour)";
						}
						sql = sql + ")";
					} else if (jdh.getIsMinMax() != null && jdh.getIsMinMax() == false && jdh.getFromHour() != null && jdh.getToHour() != null) {
						sql = sql + " OR (dh.isMinMax <> " + jdh.getIsMinMax() + " and dh.isMinMax = true" +
								" and ((dh.flexibileHours = true) or (dh.flexibileHours = false and " +
								"dh.fromHour <= " + (jdh.getToHour() - jdh.getFromHour()) + " AND " +
								"dh.toHour >= " + (jdh.getToHour() - jdh.getFromHour()) + ")))";
					}	
					counter--;
					if (counter >= 1) {
						sql = sql + ")) OR ";
					} else {
						sql = sql + ")))))";
					}
				}
				logger.info("++++++++++++++++ Added condition for FALSE flexibileDays, jobDayHours and FALSE jo.flexibileDays applications");

				sql = sql + " GROUP BY jo.id HAVING (COUNT(*) = (select COUNT(*) from JobDayHoursEntity jdh where jdh.status = 1 and jdh.offer.id = jo.id) AND COUNT(*) = " + jobDayHours.size() + " AND jo.flexibileDays = false) OR (COUNT(*) >= " + jobDayHours.size() + " AND jo.flexibileDays = true)";

			} else {
				
				sql = sql + " GROUP BY jo.id HAVING (COUNT(*) = (select COUNT(*) from JobDayHoursEntity jdh where jdh.status = 1 and jdh.offer.id = jo.id) AND jo.flexibileDays = false) OR (jo.flexibileDays = true)";
				
			}

			logger.info("++++++++++++++++ Added condition for FALSE flexibileDays applications");
			
		}
		
		Query query = em.createQuery(sql);
		logger.info("++++++++++++++++ Query created");
//		logger.info(sql);
		Iterable<ApplyContactEntity> result = query.getResultList();
		logger.info("++++++++++++++++ Result of the query returned ok");
		return new ResponseEntity<Iterable<ApplyContactEntity>>(result, HttpStatus.OK);
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> findCounterOffer(UserEntity loggedUser, List<JobDayHoursDTO> jobDayHours, String cityName, String countryRegionName, String countryName, 
			Integer typeId, Integer numberOfEmployees, Date beginningDate, Date endDate, Boolean flexibileDates, Double price, 
			Boolean flexibileDays, String detailsLink) throws Exception {
		logger.info("++++++++++++++++ Service for finding Counter JobOffer");
		String sql = "select DISTINCT jo from JobOfferEntity jo join jo.daysAndHours dh join jo.type t join jo.city c "
				+ "join jo.employee e where jo.status =1 and jo.expired = false and dh.status = 1 and t.status = 1 and"
				+ " c.status = 1 and e.status = 1 and jo.counterOffer = true";
		logger.info("++++++++++++++++ Basic query created");

		if (loggedUser != null) {
			sql = sql + " and jo.employee.id = " + loggedUser.getId();
			logger.info("++++++++++++++++ Added condition for employee applications");
		}
		
		if (cityName != null && countryName != null) {
			CityEntity city = new CityEntity();
			CountryEntity country = new CountryEntity();
			CountryRegionEntity countryRegion = new CountryRegionEntity();
			try {
				country = countryRepository.findByCountryNameIgnoreCase(countryName);
			} catch (Exception e) {
				throw new Exception("CountryRepository failed.");
			}
			if (country == null) {
				logger.info("Country doesn't exist in database.");
				return new ResponseEntity<String>("Country doesn't exist in database.", HttpStatus.NOT_FOUND);
			}
			try {
				countryRegion = countryRegionRepository.findByCountryRegionNameAndCountry(countryRegionName, country);
			} catch (Exception e) {
				throw new Exception("CountryRegionRepository failed.");
			}
			if (countryRegion == null) {
				logger.info("Country region doesn't exist in database.");
				return new ResponseEntity<String>("Country region doesn't exist in database.", HttpStatus.NOT_FOUND);
			}
			try {
				city = cityRepository.findByCityNameAndRegion(cityName, countryRegion);
			} catch (Exception e) {
				throw new Exception("CityRepository failed.");
			}
			if (city == null) {
				logger.info("City doesn't exist in database.");
				return new ResponseEntity<String>("City doesn't exist in database.", HttpStatus.NOT_FOUND);
			}
			logger.info("City founded.");
			sql = sql + " and jo.city.id = " + city.getId();
			logger.info("++++++++++++++++ Added condition for city applications");
		}
			
		if (numberOfEmployees != null) {
			sql = sql + " and jo.numberOfEmployees = " + numberOfEmployees;
			logger.info("++++++++++++++++ Added condition for distanceToJob applications");
		}

		if (typeId != null) {
			sql = sql + " and jo.type.id = " + typeId;
			logger.info("++++++++++++++++ Added condition for type applications");
		}
		
		if (beginningDate != null) {
			sql = sql + " and jo.beginningDate = " + beginningDate;
			logger.info("++++++++++++++++ Added condition for beginningDate applications");
		}
		
		if (endDate != null) {
			sql = sql + " and jo.endDate = " + endDate;
			logger.info("++++++++++++++++ Added condition for endDate applications");
		}
		
		if (flexibileDates != null) {
			sql = sql + " and jo.flexibileDates = " + flexibileDates;
			logger.info("++++++++++++++++ Added condition for flexibileDates applications");
		}
		
		if (price != null) {
			sql = sql + " and jo.price = " + price;
			logger.info("++++++++++++++++ Added condition for price applications");
		}
		
		if (detailsLink != null) {
			sql = sql + " and jo.detailsLink = " + detailsLink;
			logger.info("++++++++++++++++ Added condition for detailsLink applications");
		}
		
		if (flexibileDays != null) {
			sql = sql + " and jo.flexibileDays = " + flexibileDays;
			logger.info("++++++++++++++++ Added condition for flexibileDays applications");
		}
		
		if (jobDayHours != null && !jobDayHours.isEmpty()) {
			Integer counter = jobDayHours.size();
			sql = sql + " and (";
			for (JobDayHoursDTO jdh : jobDayHours) {
				sql = sql + "(dh.day = '" + jdh.getDay() + "' and dh.isMinMax = " + jdh.getIsMinMax() + 
						" and dh.fromHour = " + jdh.getFromHour() + " and dh.toHour = " + jdh.getToHour()+ 
						" and dh.flexibileHours = " + jdh.getFlexibileHours();
				counter--;
				if (counter >= 1) {
					sql = sql + ") OR ";
				} else {
					sql = sql + "))";
				}
			}
			sql = sql + " GROUP BY jo.id HAVING (COUNT(*) = (select COUNT(*) from JobDayHoursEntity jdh where "
					+ "jdh.status = 1 and jdh.offer.id = jo.id) AND COUNT(*) = " + jobDayHours.size() + ")";
			logger.info("++++++++++++++++ Added condition for jobDayHours applications");
		}
		
		Query query = em.createQuery(sql);
		logger.info("++++++++++++++++ Query created");
//		logger.info(sql);
		Iterable<ApplyContactEntity> result = query.getResultList();
		logger.info("++++++++++++++++ Result of the query returned ok");
		return new ResponseEntity<Iterable<ApplyContactEntity>>(result, HttpStatus.OK);

	}

}
