package com.iktpreobuka.jobster.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.jobster.entities.CityEntity;
import com.iktpreobuka.jobster.entities.CountryEntity;
import com.iktpreobuka.jobster.entities.CountryRegionEntity;
import com.iktpreobuka.jobster.entities.JobOfferEntity;
import com.iktpreobuka.jobster.entities.JobSeekEntity;
import com.iktpreobuka.jobster.entities.PersonEntity;
import com.iktpreobuka.jobster.entities.UserEntity;
import com.iktpreobuka.jobster.entities.dto.PersonDTO;
import com.iktpreobuka.jobster.enumerations.EGender;
import com.iktpreobuka.jobster.repositories.CityRepository;
import com.iktpreobuka.jobster.repositories.CountryRegionRepository;
import com.iktpreobuka.jobster.repositories.CountryRepository;
import com.iktpreobuka.jobster.repositories.JobOfferRepository;
import com.iktpreobuka.jobster.repositories.JobSeekRepository;
import com.iktpreobuka.jobster.repositories.PersonRepository;


@Service
public class PersonDaoImpl implements PersonDao {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private CountryRegionRepository countryRegionRepository;
	
	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private JobSeekRepository jobSeekRepository;

	@Autowired
	private JobOfferRepository jobOfferRepository;


    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());


	@Override
	public UserEntity addNewPerson(PersonDTO newPerson) throws Exception {
			if (newPerson.getFirstName() == null || newPerson.getLastName() == null || newPerson.getGender() == null || newPerson.getBirthDate() == null || newPerson.getEmail() == null || newPerson.getMobilePhone() == null || newPerson.getCity() == null || newPerson.getCountry() == null || newPerson.getIso2Code() == null || newPerson.getLatitude() == null || newPerson.getLongitude() == null ) {
				throw new Exception("Some data is null.");
			}
			logger.info("addNewPerson validation Ok.");
			UserEntity user = new PersonEntity();
			CityEntity city = new CityEntity();
			CountryEntity country = new CountryEntity();
			CountryRegionEntity countryRegion = new CountryRegionEntity();
			try {
				country = countryRepository.findByCountryNameIgnoreCase(newPerson.getCountry());
			} catch (Exception e1) {
				throw new Exception("CountryRepository failed.");
			}
			logger.info("CountryRepository Ok.");
			if (country != null && !country.getIso2Code().equalsIgnoreCase(newPerson.getIso2Code())) {
				throw new Exception("Wrong country name or ISO country code.");
			}
			Boolean newCountryRegion = false;
			Boolean newCountry = false;
			Boolean newCity = false;
			try {
				if(country != null) {
					logger.info("Country founded.");
					countryRegion = countryRegionRepository.findByCountryRegionNameAndCountry(newPerson.getCountryRegion(), country);
					if (countryRegion != null) {
						logger.info("CountryRegion founded.");
						city = cityRepository.findByCityNameAndRegion(newPerson.getCity(), countryRegion);
						logger.info("City founded.");
					} else {
						city = null;
						newCountryRegion = true;
					}
				} else {
					city = null;
					newCountry = true;
					newCountryRegion = true;
				}
				if( city == null) {
					city = cityDao.addNewCity(newPerson.getCity(), newPerson.getLongitude(), newPerson.getLatitude(), newPerson.getCountryRegion(), newPerson.getCountry(), newPerson.getIso2Code());
					newCity = true;
					logger.info("City created.");
				}
			} catch (Exception e2) {
				throw new Exception(e2.getLocalizedMessage());
			}
			try {
				((PersonEntity) user).setFirstName(newPerson.getFirstName());
				((PersonEntity) user).setLastName(newPerson.getLastName());
				((PersonEntity) user).setGender(EGender.valueOf(newPerson.getGender()));
			    Date bithDate = formatter.parse(newPerson.getBirthDate());
			    ((PersonEntity) user).setBirthDate(bithDate);
			    user.setCity(city);
			    user.setEmail(newPerson.getEmail());
			    user.setMobilePhone(newPerson.getMobilePhone());
			    user.setAbout(newPerson.getAbout());
			    user.setNumberOfRatings(0);
			    user.setRating(0.0);
				user.setStatusActive();
				personRepository.save(user);
				logger.info("User created.");
				//user = personRepository.findByEmailAndStatusLike(newPerson.getEmail(), 1);
				user.setCreatedById(user.getId());
				personRepository.save(user);
				logger.info("User CreatedById added.");
				if(newCity == true) {
					city.setCreatedById(user.getId());
					cityRepository.save(city);
					logger.info("City CreatedById added.");
					if(newCountryRegion == true) {
						if(newCountry == true) {
							country = countryRepository.findByCountryNameAndIso2Code(newPerson.getCountry(), newPerson.getIso2Code());
							country.setCreatedById(user.getId());
							countryRepository.save(country);
							logger.info("Country CreatedById added.");
						}
						countryRegion = countryRegionRepository.findByCountryRegionNameAndCountry(newPerson.getCountryRegion(), country);
						countryRegion.setCreatedById(user.getId());
						countryRegionRepository.save(countryRegion);
						logger.info("CountryRegion CreatedById added.");
					}
				}
				logger.info("addNewPerson finished.");
			} catch (Exception e) {
				throw new Exception("addNewPerson save failed." + e.getLocalizedMessage());
			}
			return user;
	}

	@Override
	public void modifyPerson(UserEntity loggedUser, PersonEntity person, PersonDTO updatePerson) throws Exception {
		if (updatePerson.getFirstName() == null && updatePerson.getLastName() == null && updatePerson.getGender() == null && updatePerson.getBirthDate() == null && updatePerson.getEmail() == null && updatePerson.getMobilePhone() == null && ( updatePerson.getCity() == null || updatePerson.getCountry() == null || updatePerson.getIso2Code() == null || updatePerson.getLatitude() == null || updatePerson.getLongitude() == null) && updatePerson.getAbout() == null ) {
			throw new Exception("All data is null.");
		}
		try {
			Integer i = 0;
			if (updatePerson.getFirstName() != null && !updatePerson.getFirstName().equals(" ") && !updatePerson.getFirstName().equals("") && !updatePerson.getFirstName().equals(person.getFirstName())) {
				person.setFirstName(updatePerson.getFirstName());
				i++;
			}
			if (updatePerson.getLastName() != null && !updatePerson.getLastName().equals(person.getLastName()) && !updatePerson.getLastName().equals(" ") && !updatePerson.getLastName().equals("")) {
				person.setLastName(updatePerson.getLastName());
				i++;
			}
			if (updatePerson.getGender() != null && EGender.valueOf(updatePerson.getGender()) != person.getGender() && (EGender.valueOf(updatePerson.getGender()) == EGender.GENDER_FEMALE || EGender.valueOf(updatePerson.getGender()) == EGender.GENDER_MALE)) {
				person.setGender(EGender.valueOf(updatePerson.getGender()));
				i++;
			}
			if (updatePerson.getBirthDate() != null && !formatter.parse(updatePerson.getBirthDate()).equals(person.getBirthDate()) && !updatePerson.getBirthDate().equals(" ") && !updatePerson.getBirthDate().equals("")) {
			    Date employmentDate = formatter.parse(updatePerson.getBirthDate());
			    person.setBirthDate(employmentDate);
				i++;
			}
			if (updatePerson.getEmail() != null && !updatePerson.getEmail().equals(person.getEmail()) && !updatePerson.getEmail().equals(" ") && !updatePerson.getEmail().equals("")) {
				person.setEmail(updatePerson.getEmail());
				i++;
			}
			if (updatePerson.getMobilePhone() != null && !updatePerson.getMobilePhone().equals(person.getMobilePhone()) && !updatePerson.getMobilePhone().equals(" ") && !updatePerson.getMobilePhone().equals("")) {
				person.setMobilePhone(updatePerson.getMobilePhone());
				i++;
			}
			if (updatePerson.getAbout() != null && !updatePerson.getAbout().equals(person.getAbout()) && !updatePerson.getAbout().equals(" ") && !updatePerson.getAbout().equals("")) {
				person.setAbout(updatePerson.getAbout());
				i++;
			}
			if (updatePerson.getCity() != null && !updatePerson.getCity().equals(" ") && !updatePerson.getCity().equals("") && updatePerson.getCountry() != null && !updatePerson.getCountry().equals(" ") && !updatePerson.getCountry().equals("") && updatePerson.getIso2Code() != null && !updatePerson.getIso2Code().equals(" ") && !updatePerson.getIso2Code().equals("") ) {
				CityEntity city = new CityEntity();
				CountryRegionEntity countryRegion = new CountryRegionEntity();
				CountryEntity country = countryRepository.findByCountryNameAndIso2Code(updatePerson.getCountry(), updatePerson.getIso2Code());
				if(country != null) {
					countryRegion = countryRegionRepository.findByCountryRegionNameAndCountry(updatePerson.getCountryRegion(), country);
					if (countryRegion != null) {
						city = cityRepository.findByCityNameAndRegion(updatePerson.getCity(), countryRegion);
					} else {
						city = null;
					}
				} else {
					city = null;
				}
				if( city == null) {
					city = cityDao.addNewCityWithLoggedUser(updatePerson.getCity(), updatePerson.getLongitude(), updatePerson.getLatitude(), updatePerson.getCountryRegion(), updatePerson.getCountry(), updatePerson.getIso2Code(), loggedUser);
				}
				if(!city.equals(person.getCity())) {
					person.setCity(city);
					i++;
				}
			}
			if (i>0) {
				person.setUpdatedById(loggedUser.getId());
				personRepository.save(person);
			}
		} catch (Exception e) {
			throw new Exception("modifyPerson faild on saving." + e.getLocalizedMessage());
		}
	}

	@Override
	public void deletePerson(UserEntity loggedUser, PersonEntity person) throws Exception {
		try {
			for (JobOfferEntity jo : person.getJobOffers()) {
				if (jo.getStatus() == 1) {
					jo.setStatusInactive();
					jo.setUpdatedById(loggedUser.getId());
					jobOfferRepository.save(jo);
				}
			}
			for (JobSeekEntity js : person.getJobSeeks()) {
				if (js.getStatus() == 1) {
					js.setStatusInactive();
					js.setUpdatedById(loggedUser.getId());
					jobSeekRepository.save(js);
				}
			}
			person.setStatusInactive();
			person.setUpdatedById(loggedUser.getId());
			personRepository.save(person);
		} catch (Exception e) {
			throw new Exception("deletePerson failed on saving." + e.getLocalizedMessage());
		}
	}
	
	@Override
	public void undeletePerson(UserEntity loggedUser, PersonEntity person) throws Exception {
		try {
			person.setStatusActive();
			person.setUpdatedById(loggedUser.getId());
			personRepository.save(person);
		} catch (Exception e) {
			throw new Exception("undeletePerson failed on saving." + e.getLocalizedMessage());
		}		
	}
	
	@Override
	public void archivePerson(UserEntity loggedUser, PersonEntity person) throws Exception {
		try {
			for (JobOfferEntity jo : person.getJobOffers()) {
					jo.setStatusArchived();
					jo.setUpdatedById(loggedUser.getId());
					jobOfferRepository.save(jo);
			}
			for (JobSeekEntity js : person.getJobSeeks()) {
					js.setStatusArchived();
					js.setUpdatedById(loggedUser.getId());
					jobSeekRepository.save(js);
			}
			person.setStatusArchived();
			person.setUpdatedById(loggedUser.getId());
			personRepository.save(person);
		} catch (Exception e) {
			throw new Exception("ArchivePerson failed on saving." + e.getLocalizedMessage());
		}		
	}

}
